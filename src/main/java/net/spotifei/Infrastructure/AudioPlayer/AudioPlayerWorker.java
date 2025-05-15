package net.spotifei.Infrastructure.AudioPlayer;

import net.spotifei.Models.Music;

import javax.sound.sampled.*;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.*;

/**
 * Worker assíncrono que gerencia as músicas sendo tocadas em background
 * @implNote Não crie instâncias de MusicPlayerWorker se você não
 * quiser erros catastróficos
 */
public class AudioPlayerWorker extends SwingWorker<String, Long> implements AudioControls{

    private final LinkedBlockingQueue<AudioCommand> commandQueue = new LinkedBlockingQueue<>();
    private Thread progressUpdateThread;
    private ProgressPublisherRunnable progressPublisherRunnable;
    private long musicMicrosecondNow = 0;
    private boolean isPlaying = false;
    private boolean shutdownWorker = false;
    private Clip clip;
    private float volume = 0.0f;
    // está sendo usado CopyOnWriteArrayList pq é a única list thread safe do java, o resto buga em alguns casos
    private final List<AudioUpdateListener> listeners = new CopyOnWriteArrayList<>();

    private final float DEFAULT_VOLUME = -23.0f;

    @FunctionalInterface
    private interface AudioCommand{
        void execute() throws Exception;
    }

    // FUNÇÕES RELACIONADAS AOS LISTENERS (conexões para outros lugares do código)

    public void addListener(AudioUpdateListener listener){
        if (listener != null && !listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeListener(AudioUpdateListener listener){
        if (listener != null){
            listeners.remove(listener);
        }
    }

    public void notifyEndOfMusic(){
        for(AudioUpdateListener listener : listeners){
            listener.onEndOfMusic();
        }
    }

    public void notifyOnMusicUpdate(long musicTime, long musicTotalTime){
        for(AudioUpdateListener listener : listeners){
            listener.onMusicProgressUpdate(musicTime, musicTotalTime);
        }
    }

    public void notifyOnPlayingStatusUpdate(boolean isPlaying){
        for (AudioUpdateListener listener : listeners){
            listener.onMusicPlayingStatusUpdate(isPlaying);
        }
    }

    public void notifyOnMusicSelected(Music music){
        for (AudioUpdateListener listener : listeners){
            listener.onSelectMusic(music);
        }
    }

    // FUNÇÕES DO WORKER

    @Override
    protected void done() {
        try {
            get(); // da throw em qualquer excessão lançada durante o worker
        } catch (Exception e) {
            clip.close();
            e.getCause(); // Da throw novamente na Exception
        }
    }

    @Override
    protected String doInBackground() throws Exception {
        clip = AudioSystem.getClip();
        // listener para checar quando for o fim da música
        clip.addLineListener(this::handleEndOfMusic);

        logInfo("Worker tocador de aúdio iniciado!");
        while (!shutdownWorker) { // Infinite loop planejado p
            try {
                AudioCommand command = commandQueue.take(); // Trava a Thread até que consiga obter um elemento da Queue
                command.execute();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IllegalArgumentException e) {
                logError("Um comando executado no worker de aúdio falhou!", e);
            }
        }

        if (clip != null && clip.isOpen()) clip.close();

        return "Worker de aúdio encerrado.";
    }

    @Override
    protected void process(List<Long> chunks) {
        // muitas verificacoes mas é para ter certeza que nao vai bugar

        if (clip != null && clip.isOpen() && clip.isActive()
                && clip.isRunning() && clip.getMicrosecondLength() > 0) {

            long musicCurrentLength = chunks.get(chunks.size() - 1); // obter o length da música pelos chunks recebidos
            long musicTotalLength = clip.getMicrosecondLength();
            notifyOnMusicUpdate(musicCurrentLength, musicTotalLength);

            chunks.clear(); // limpar memoria
        }
    }

    // FUNCOES DOS CONTROLES DE AUDIO


    @Override
    public void selectMusic(Music music) {
        notifyOnMusicSelected(music);
    }

    /**
     * Toca uma música com o audioinputstream fornecido
     * @param musicAudioByteArray byte[] contendo o aúdio da música
     * @throws Exception Retorna qualquer excessão gerada ao tentar gerar o inputStream da música
     * ao tentar tocar a música
     */
    @Override
    public void playMusic(byte[] musicAudioByteArray) throws InterruptedException {
        commandQueue.put(() -> handlePlayMusic(musicAudioByteArray));
    }

    /**
     * Pausa a música
     */
    public void pause() throws InterruptedException {
        commandQueue.put(() -> handlePauseMusic(true));
    }

    /**
     * Despausa a música
     */
    @Override
    public void resume() throws InterruptedException {
        commandQueue.put(() -> handlePauseMusic(false));
    }

    /**
     * Altera o tempo da música sendo tocada
     * @param musicTimePercentage Tempo da música em porcentagem
     * setado (vai tocar a música a partir desse tempo)
     */
    public void seek(float musicTimePercentage) throws InterruptedException {
        commandQueue.put(() -> handleSeek(musicTimePercentage));
    }

    /**
     * Altera o volume da música sendo tocada
     * @param volume Número float entre 0.1f e 1.0f
     */
    public void setVolume(float volume) throws InterruptedException {
        if (volume < 0.0f || volume > 1.0f) {
            logWarn("Volume fora do intervao 0.0-1.0: " + volume + "!");
            volume = Math.max(0.0f, Math.min(1.0f, volume));
        }
        float finalVolume = volume;
        commandQueue.put(() -> handleSetVolume(finalVolume));
    }

    public void shutdown() throws InterruptedException {
        shutdownWorker = false;
        commandQueue.put(() -> {});
    }

    // FUNCOES DA LÓGICA DOS CONTROLES DE AÚDIO

    private void handleEndOfMusic(LineEvent event){
        // verificacao para checar se musica finalizou
        if (event.getType() == LineEvent.Type.STOP && clip != null && isPlaying) {
            if (clip.getMicrosecondPosition() >= clip.getMicrosecondLength() - 1000) {
                boolean wasPlaying = isPlaying;
                isPlaying = false;
                stopProgressUpdateThread();
                notifyOnMusicUpdate(clip.getMicrosecondLength(), clip.getMicrosecondLength() + 1);
                if (wasPlaying) {
                    notifyEndOfMusic();
                }
            }
        } else if (event.getType() == LineEvent.Type.CLOSE) {
            isPlaying = false;
            stopProgressUpdateThread();
        }
    }

    private void handleSetVolume(float volume){
        if (clip == null || !clip.isOpen()) return;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        float finalVolume = (float) (Math.log10(volume) * 60) -5f;
        if (finalVolume < -80.0f) {
            logWarn("Volume recebido foi menor do que o permitido (-80.0f): " + finalVolume + "!" +
                    "\n O valor foi redefinido para -80.0f.");
            finalVolume = -80.0f;
        }
        this.volume = finalVolume; // coloca o volume que o usuario definiu
        // Multiplica por 1.5 para reduzir o volume (dividir aumenta por algum motivo)
        gainControl.setValue(finalVolume);
    }

    private void forceSetVolume(float volume){
        if (clip == null || !clip.isOpen()) {
            logWarn("Tentativa de forçar volume enquanto clip não foi inicializado!");
            return;
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        logDebug("Volume setado de forma forçada para: " + volume + "");
        gainControl.setValue(volume);
    }

    private void handleSeek(float musicTimePercentage){

        long microseconds = (long) (clip.getMicrosecondLength() * musicTimePercentage);

        if (microseconds > clip.getMicrosecondLength()){
            microseconds = clip.getMicrosecondLength() - 1;
        }
        clip.setMicrosecondPosition(microseconds);
        musicMicrosecondNow = microseconds;
        if (isPlaying){
            clip.start();
        }
    }

    private void handlePauseMusic(Boolean forceStatus){
        boolean shouldPause = isPlaying;
        if (forceStatus != null){
            shouldPause = forceStatus;
        }
        if (shouldPause) {
            musicMicrosecondNow = clip.getMicrosecondPosition();
            clip.stop();
            isPlaying = false;
            stopProgressUpdateThread();
        }
        else {
            clip.setMicrosecondPosition(musicMicrosecondNow);
            clip.start();
            isPlaying = true;
            startProgressUpdateThread();
        }
        notifyOnPlayingStatusUpdate(isPlaying); // notificar o listener de alterar status
    }

    public void publishProgress(long microseconds) {
        if (shutdownWorker) return;
        publish(microseconds);
    }

    private void handlePlayMusic(byte[] audioByteArray) throws Exception{
        if (clip == null){
            throw new IllegalStateException("O clip não foi inicializado!");
        }

        if (clip.isOpen()){
            isPlaying = false;
            stopProgressUpdateThread();
            notifyOnPlayingStatusUpdate(false);
            clip.stop();
            clip.close();
            clip.flush();
        }

        try{

            // obtem o audiostream do arquivo opus convertido
            AudioInputStream audioStream = OpusConverter.convertOpusBytesToAudioInputStream(audioByteArray);
            clip.open(audioStream);

            if (volume == 0.0f && clip.getControl(FloatControl.Type.MASTER_GAIN) != null){
                volume = DEFAULT_VOLUME;
                forceSetVolume(volume);
            } else{
                forceSetVolume(volume);
            }

            musicMicrosecondNow = 0;
            clip.setMicrosecondPosition(0);
            isPlaying = true;
            clip.start();

            startProgressUpdateThread();
            notifyOnPlayingStatusUpdate(true);
        }  catch (Exception e) {
            isPlaying = false;
            logError("Erro ao tocar música!", e);
            throw e;
        }
    }

    // FUNCOES DO THREAD PARA DAR UPATE NO SLIDER DE MUSICA

    private void startProgressUpdateThread() {
        if(shutdownWorker){
            logWarn("Tentativa de iniciar o Thread de Atualizar Progresso do Slider com o Worker encerrado!");
            return;
        } else if (progressUpdateThread != null && progressUpdateThread.isAlive()) {
            logWarn("Tentativa de iniciar o Thread de Atualizar Progresso do Slider com um outro Thread rodando!");
        } else if (this.clip == null || !this.clip.isOpen()){
            logWarn("Tentativa de iniciar o Thread de Atualizar Progresso do Slider com um Clip fechado!");
            return;
        }
        progressPublisherRunnable = new ProgressPublisherRunnable(this, this.clip);
        progressUpdateThread = new Thread(progressPublisherRunnable);
        progressUpdateThread.setDaemon(true); // tornar thread daemon (roda em background em baixa prioridade)
        progressUpdateThread.start(); // iniciar o thread :P
        logDebug("Thread de Atualizar Progresso do Slider iniciada com sucesso!");
    }

    private void stopProgressUpdateThread() {
        if (progressPublisherRunnable != null){
            progressPublisherRunnable.stopPublisher();
        }
        if (progressUpdateThread != null && progressUpdateThread.isAlive()){
            progressUpdateThread.interrupt();
        }
        logDebug("Thread de Atualizar Progresso do Slider encerrada com sucesso!");

        // limpa a memória
        this.progressPublisherRunnable = null;
        this.progressUpdateThread = null;
    }

    // GETTER DE isPlaying

    public boolean isPlaying() {
        return isPlaying;
    }

}
