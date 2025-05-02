package net.spotifei.Infrastructure.AudioPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Worker assíncrono que gerencia as músicas sendo tocadas em background
 * @implNote Não crie instâncias de MusicPlayerWorker se você não
 * quiser erros catastróficos
 */
public class AudioPlayerWorker extends SwingWorker<String, Void> {

    private final LinkedBlockingQueue<AudioCommand> commandQueue = new LinkedBlockingQueue<>();
    private long musicMicrosecond = 0;
    private boolean isPlaying = false;
    private boolean keepRunning = true;
    private Clip clip;

    @FunctionalInterface
    private interface AudioCommand{
        void execute() throws Exception;
    }

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
        while (keepRunning) { // Infinite loop planejado p
            try {
                AudioCommand command = commandQueue.take(); // Trava a Thread até que consiga obter um elemento da Queue
                command.execute();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IllegalArgumentException e) {
                // loggar isso, algum valor colocado errado que vai ser ignorado
                System.err.println("Um comando executado no worker de aúdio falhou!" + e.getMessage());
            }
        }

        if (clip != null && clip.isOpen()) clip.close();

        return "Worker de aúdio encerrado.";
    }

    /**
     * Toca uma música com o audioinputstream fornecido
     * @param audioInputStream "InputStream" da música
     * @throws Exception Retorna qualquer excessão gerada
     * ao tentar tocar a música
     */
    public void playMusic(AudioInputStream audioInputStream) throws InterruptedException {
        commandQueue.put(() -> handlePlayMusic(audioInputStream));
    }

    /**
     * Força a pausa ou despausa de uma música conforme
     * o parâmetro forceStatus inserido, deixe sem argumento
     * para pausar / despausar automaticamente
     * @param forceStatus Booleano para definir se deve pausar ou
     *              despausar as músicas:
     *              true = pausa
     *              false = despausa
     */
    public void pause(boolean forceStatus) throws InterruptedException {
        commandQueue.put(() -> handlePauseMusic(forceStatus));
    }

    /**
     * Pausa ou despausa uma música, se está tocando
     * será pausada, se estiver pausada será tocada
     */
    public void pause() throws InterruptedException {
        commandQueue.put(() -> handlePauseMusic(null));
    }

    /**
     * Altera o tempo da música sendo tocada
     * @param microseconds Tempo da música em microsegundos a ser
     * setado (vai tocar a música a partir desse tempo)
     */
    public void seek(long microseconds) throws InterruptedException {
        commandQueue.put(() -> handleSeek(microseconds));
    }

    /**
     * Altera o volume da música sendo tocada
     * @param volume Número float entre 0.1f e 1.0f
     */
    public void setVolume(float volume) throws InterruptedException {
        if (volume <= 0f || volume > 1f){
            throw new IllegalArgumentException("O volume deve ser um valor entre (0 e 1]!");
        }
        commandQueue.put(() -> handleSetVolume(volume));
    }

    public void shutdown() throws InterruptedException {
        keepRunning = false;
        commandQueue.put(() -> {});
    }


    private void handleSetVolume(float volume){
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        gainControl.setValue(20f * (float) Math.log10(volume));
    }


    private void handleSeek(long microseconds){
        if (microseconds > clip.getMicrosecondLength()){
            microseconds = clip.getMicrosecondLength() - 1;
        }
        clip.setMicrosecondPosition(microseconds);
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
            musicMicrosecond = clip.getMicrosecondPosition();
            clip.stop();
            isPlaying = false;
        }
        else {
            clip.setMicrosecondPosition(musicMicrosecond);
            clip.start();
            isPlaying = true;
        }
    }

    private void handlePlayMusic(AudioInputStream audioInputStream) throws Exception{
        clip.open(audioInputStream);
        clip.setMicrosecondPosition(0);
        clip.start();
    }
}
