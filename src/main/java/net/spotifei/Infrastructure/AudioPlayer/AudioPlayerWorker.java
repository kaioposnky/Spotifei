package net.spotifei.Infrastructure.AudioPlayer;

//imports
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

    /**
     * Interface funcional para definir comandos de áudio que serão processados sequencialmente.
     */
    @FunctionalInterface
    private interface AudioCommand{
        void execute() throws Exception;
    }

    // FUNÇÕES RELACIONADAS AOS LISTENERS (conexões para outros lugares do código)


    /**
     * Adiciona um listener para receber atualizações de áudio.
     *
     * @param listener O AudioUpdateListener a ser adicionado.
     */
    public void addListener(AudioUpdateListener listener){
        if (listener != null && !listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    /**
     * Remove um listener.
     *
     * @param listener O AudioUpdateListener a ser removido.
     */
    public void removeListener(AudioUpdateListener listener){
        if (listener != null){
            listeners.remove(listener);
        }
    }

    /**
     * Notifica todos os listeners que a música atual chegou ao fim.
     */
    public void notifyEndOfMusic(){
        for(AudioUpdateListener listener : listeners){
            listener.onEndOfMusic();
        }
    }

    /**
     * Notifica todos os listeners sobre a atualização do progresso da música.
     *
     * @param musicTime O tempo atual da música em microssegundos.
     * @param musicTotalTime O tempo total da música em microssegundos.
     */
    public void notifyOnMusicUpdate(long musicTime, long musicTotalTime){
        for(AudioUpdateListener listener : listeners){
            listener.onMusicProgressUpdate(musicTime, musicTotalTime);
        }
    }

    /**
     * Notifica todos os listeners sobre a mudança no status de reprodução (tocando/pausado).
     *
     * @param isPlaying true se a música estiver tocando, false caso contrário.
     */
    public void notifyOnPlayingStatusUpdate(boolean isPlaying){
        for (AudioUpdateListener listener : listeners){
            listener.onMusicPlayingStatusUpdate(isPlaying);
        }
    }

    /**
     * Notifica todos os listeners que uma nova música foi selecionada.
     *
     * @param music O objeto Music que foi selecionado.
     */
    public void notifyOnMusicSelected(Music music){
        for (AudioUpdateListener listener : listeners){
            listener.onSelectMusic(music);
        }
    }

    // FUNÇÕES DO WORKER


    /**
     * Método chamado na Event Dispatch Thread (EDT) após a conclusão da tarefa em segundo plano.
     * Este método lida com quaisquer exceções que possam ter ocorrido durante doInBackground().
     */
    @Override
    protected void done() {
        try {
            get(); // da throw em qualquer excessão lançada durante o worker
        } catch (Exception e) {
            if(clip != null){
                clip.close();
            }
            e.printStackTrace(); // Da throw novamente na Exception
        }
    }

    /**
     * Este método é executado em uma thread separada e contém a lógica principal do worker.
     * Ele entra em um loop para processar comandos de áudio da fila.
     *
     * @return Uma mensagem indicando que o worker foi encerrado.
     * @throws Exception Se ocorrer um erro inesperado durante a execução de um comando.
     */
    @Override
    protected String doInBackground() throws Exception {
        try {
            clip = AudioSystem.getClip();
        } catch (IllegalArgumentException e){
            JOptionPane.showMessageDialog(null,
                    "Seu dispositivo está com o aúdio desabilitado! Não será" +
                            "possível tocar músicas. Habilite o aúdio do dispositivo e" +
                            "abra o aplicativo novamente.");
            throw new IllegalStateException("O aúdio do sistema está desabilitado!");
        }
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

    /**
     * Processa os chunks de dados publicados.
     * Este método é executado na Event Dispatch Thread (EDT) e é usado para atualizar a interface do usuário
     * com o progresso da música.
     *
     * @param chunks Uma lista de Longs representando os tempos de música em microssegundos.
     */
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


    /**
     * Notifica os listeners que uma música foi selecionada.
     *
     * @param music O objeto Music a ser selecionado.
     */
    @Override
    public void selectMusic(Music music) {
        notifyOnMusicSelected(music);
    }

    /**
     * Toca uma música com o audioinputstream fornecido
     *
     * @param musicAudioByteArray byte[] contendo o aúdio da música
     * @throws InterruptedException Retorna qualquer excessão gerada ao tentar gerar o inputStream da música
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
     *
     * @param musicTimePercentage Tempo da música em porcentagem
     * setado (vai tocar a música a partir desse tempo)
     */
    public void seek(float musicTimePercentage) throws InterruptedException {
        commandQueue.put(() -> handleSeek(musicTimePercentage));
    }

    /**
     * Altera o volume da música sendo tocada
     *
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

    /**
     * Sinaliza ao worker para encerrar seu loop principal e liberar recursos.
     *
     * @throws InterruptedException Se a thread for interrompida ao adicionar o comando à fila.
     */
    public void shutdown() throws InterruptedException {
        shutdownWorker = false;
        commandQueue.put(() -> {});
    }

    // FUNCOES DA LÓGICA DOS CONTROLES DE AÚDIO

    /**
     * Manipula o evento de fim de música do Clip.
     * Notifica os listeners quando a música atinge o fim.
     *
     * @param event O LineEvent recebido do Clip.
     */
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

    /**
     * Define o volume do Clip de áudio.
     *
     * @param volume O volume desejado (0.0f a 1.0f).
     */
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

    /**
     * Força a definição do volume do Clip.
     *
     * @param volume O volume em decibéis a ser aplicado.
     */
    private void forceSetVolume(float volume){
        if (clip == null || !clip.isOpen()) {
            logWarn("Tentativa de forçar volume enquanto clip não foi inicializado!");
            return;
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        logDebug("Volume setado de forma forçada para: " + volume + "");
        gainControl.setValue(volume);
    }

    /**
     * Altera a posição de reprodução da música.
     *
     * @param musicTimePercentage A porcentagem da música para a qual pular.
     */
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

    /**
     * Lida com a pausa ou despausa da música.
     *
     * @param forceStatus Se não nulo, força o status para true (pausar) ou false (despausar).
     * Se nulo, alterna o status atual.
     */
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

    /**
     * Publica o progresso da música para ser consumido pelo método process() do SwingWorker.
     *
     * @param microseconds A posição atual da música em microssegundos.
     */
    public void publishProgress(long microseconds) {
        if (shutdownWorker) return;
        publish(microseconds);
    }

    /**
     * Manipula a lógica de iniciar a reprodução de uma música.
     * Fecha o clip anterior (se houver), converte os bytes de áudio e abre um novo clip para reprodução.
     *
     * @param audioByteArray O array de bytes contendo os dados de áudio da música (formato Opus).
     * @throws Exception Se ocorrer um erro durante a conversão ou reprodução do áudio.
     */
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

    /**
     * Inicia a thread que publica o progresso da música para atualização da UI.
     */
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

    /**
     * Para a thread que publica o progresso da música.
     * Interrompe o runnable e a thread, e limpa as referências.
     */
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

    /**
     * Retorna o status de reprodução atual.
     *
     * @return true se a música estiver tocando, false caso contrário.
     */
    public boolean isPlaying() {
        return isPlaying;
    }

}
