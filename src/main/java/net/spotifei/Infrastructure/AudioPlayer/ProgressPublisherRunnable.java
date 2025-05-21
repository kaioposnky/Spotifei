package net.spotifei.Infrastructure.AudioPlayer;

//imports
import javax.sound.sampled.Clip;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class ProgressPublisherRunnable implements Runnable {

    private final AudioPlayerWorker audioPlayerWorker;
    private boolean stopThread = false;
    private final Clip clip;

    /**
     * Construtor para o ProgressPublisherRunnable.
     *
     * @param audioPlayerWorker A instância do AudioPlayerWorker que publicará as atualizações de progresso.
     * @param clip O Clip de áudio que está sendo monitorado.
     */
    public ProgressPublisherRunnable(AudioPlayerWorker audioPlayerWorker, Clip clip) {
        this.audioPlayerWorker = audioPlayerWorker;
        this.clip = clip;
    }

    /**
     * Contém a lógica principal da thread.
     * Ele monitora a posição atual do Clip e a publica para o AudioPlayerWorker.
     */
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

    /**
     * Sinaliza para a thread parar sua execução no próximo ciclo.
     */
    public void stopPublisher(){
        this.stopThread = true;
    }
}
