package net.spotifei.Infrastructure.AudioPlayer;

import javax.sound.sampled.Clip;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class ProgressPublisherRunnable implements Runnable {

    private final AudioPlayerWorker audioPlayerWorker;
    private boolean stopThread = false;
    private final Clip clip;

    public ProgressPublisherRunnable(AudioPlayerWorker audioPlayerWorker, Clip clip) {
        this.audioPlayerWorker = audioPlayerWorker;
        this.clip = clip;
    }

    @Override
    public void run() {
        try{
            while(audioPlayerWorker.isPlaying() && !stopThread && !Thread.currentThread().isInterrupted()){
                if (stopThread){
                    break;
                }
                if (clip.isRunning() && clip.isActive() && clip.isOpen()){
                    audioPlayerWorker.publishProgress(clip.getMicrosecondPosition());
                } else{
                    continue; // só skipa essa iteração porque a música provavelmente pausou ou foi finalizada
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e){
            logDebug("Thread de progresso encerrado.");
        } catch (Exception e){
            logError("Erro ao executar thread de atualização de progresso do Slider de música!", e);
        }
    }

    public void stopPublisher(){
        this.stopThread = true;
    }
}
