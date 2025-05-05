package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Repository.MusicRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;

public class MusicServices {

    private final MusicRepository musicRepository = new MusicRepository();

    public Response<Void> playMusic(int userId){
        try{
            Music music = musicRepository.getNextMusicOnUserQueue(userId);

            return ResponseHelper.GenerateSuccessResponse("Música colocada para tocar com sucesso!");
        } catch (Exception e){
            return ResponseHelper.GenerateErrorResponse("Ocorreu um erro ao tentar" +
                    "tocar uma música!", e);
        }
    }
}
