package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Repository.MusicRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logInfo;

public class MusicService {

    private final MusicRepository musicRepository = new MusicRepository();
    private final AudioPlayerWorker audioPlayerWorker;

    public MusicService(AudioPlayerWorker audioPlayerWorker){
        this.audioPlayerWorker = audioPlayerWorker;
    }

    public Response<Music> getNextMusicInUserQueue(int userId){
        try{
            Music music = musicRepository.getNextMusicOnUserQueue(userId);

            if(music == null){
                return ResponseHelper.GenerateBadResponse("A música retornada foi nula!");
            }

            return ResponseHelper.GenerateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception e){
            return ResponseHelper.GenerateErrorResponse("Ocorreu um erro ao tentar" +
                    "tocar uma música!", e);
        }
    }

    public Response<Void> playMusic(int musicId){
        try{
            byte[] musicAudio = musicRepository.getMusicAsByteArray(musicId);

            if(musicAudio == null){
                return ResponseHelper.GenerateBadResponse("O aúdio retornado foi nulo! Cancelando operação.");
            }

            audioPlayerWorker.playMusic(musicAudio);

            return ResponseHelper.GenerateSuccessResponse("Música iniciada com sucesso!");
        } catch (Exception e){
            return ResponseHelper.GenerateErrorResponse("Ocorreu um erro ao tentar" +
                    "tocar uma música!", e);
        }
    }

    public Response<Void> setAudioVolume(float volume){
        try {
            if(volume < 0.0f || volume > 100.0f){
                return ResponseHelper.GenerateBadResponse("O volume deve estar entre 0 e 100!");
            }

            // audioplayerworker só aceita volume entre 0f e 1f
            volume = volume / 100;

            audioPlayerWorker.setVolume(volume);

            return ResponseHelper.GenerateSuccessResponse("Volume alterado com sucesso!");

        } catch (Exception e){
            return ResponseHelper.GenerateErrorResponse("Erro ao tentar alterar o volume!", e);
        }
    }

    public Response<Void> setMusicTime(float musicTime){
        try {
            if(musicTime < 0 || musicTime > 100){
                return ResponseHelper.GenerateBadResponse("O volume deve estar entre 0 e 100!");
            }

            // audioplayerworker só aceita volume entre 0f e 1f
            musicTime = musicTime / 100;

            audioPlayerWorker.seek(musicTime);

            return ResponseHelper.GenerateSuccessResponse("Volume alterado com sucesso!");

        } catch (Exception e){
            return ResponseHelper.GenerateErrorResponse("Erro ao tentar alterar o volume!", e);
        }
    }
}
