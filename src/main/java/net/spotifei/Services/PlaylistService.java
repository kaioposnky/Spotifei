package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.Repository.MusicRepository;
import net.spotifei.Infrastructure.Repository.PlaylistRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import net.spotifei.Models.Responses.Response;

import java.util.List;

public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicRepository;

    public PlaylistService(PlaylistRepository playlistRepository, MusicRepository musicRepository) {
        this.playlistRepository = playlistRepository;
        this.musicRepository = musicRepository;
    }

    public Response<Void> createPlaylist(Playlist playlist, int userId){
        try{
            if (playlist == null){
                return ResponseHelper.GenerateBadResponse("A playlist fornecida foi nula!");
            }

            playlistRepository.createPlaylist(playlist, userId);

            return ResponseHelper.GenerateSuccessResponse("Playlist criada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse("Erro ao criar playlist!", ex);
        }
    }

    public Response<Void> deletePlaylist(int playlistId){
        try{
            if (playlistId <= 0){
                return ResponseHelper.GenerateBadResponse("O id da playlist deve ser >= 0!");
            }

            playlistRepository.deletePlaylist(playlistId);

            return ResponseHelper.GenerateSuccessResponse("Playlist deletada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse("Erro ao deletar playlist!", ex);
        }
    }

    public Response<Void> updatePlaylist(Playlist playlist){
        try{
            if (playlist == null || playlist.getIdPlaylist() <= 0){
                return ResponseHelper.GenerateBadResponse("Os campos playlist(incluidno o id da playlist)" +
                        "não podem ser nulos ou zero!");
            }

            playlistRepository.updatePlaylist(playlist);

            return ResponseHelper.GenerateSuccessResponse("Playlist atualizada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse("Erro ao atualizar playlist!", ex);
        }
    }

    public Response<Void> addMusicToLastPositionInPlaylist(int musicId, int playlistId){
        try{
            if (musicId == 0 || playlistId <= 0){
                return ResponseHelper.GenerateBadResponse(
                        "Os campos musicId e " +
                                "playlistId não podem ser nulos ou zero!");
            }

            Music music = musicRepository.getMusicById(musicId);

            if(music == null){
                return ResponseHelper.GenerateBadResponse("Música não encontrada!");
            }

            int position = playlistRepository.getLastPlaylistPosition(playlistId);

            if(position == 0){
                position++;
            }

            playlistRepository.addMusicToPlaylist(music, playlistId, position);

            return ResponseHelper.GenerateSuccessResponse("Música adicionada à playlist com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse("Erro ao adicionar música à playlist!", ex);
        }
    }

    public Response<Void> removeMusicFromPlaylist(int musicId, int playlistId){
        try{
            if (musicId == 0 || playlistId <= 0){
                return ResponseHelper.GenerateBadResponse(
                        "Os campos musicId e playlistId não podem ser nulos ou zero!");
            }

            playlistRepository.removeMusicFromPlaylist(musicId, playlistId);

            return ResponseHelper.GenerateSuccessResponse("Música removida da playlist com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse("Erro ao remover música da playlist!", ex);
        }
    }

    public Response<List<Music>> getMusicsFromPlaylist(int playlistId) {
        try {
            if (playlistId <= 0) {
                return ResponseHelper.GenerateBadResponse("O id da playlist deve ser >= 0!");
            }

            List<Music> musics = playlistRepository.getMusicsFromPlaylist(playlistId);

            return ResponseHelper.GenerateSuccessResponse("Músicas da playlist obtidas com sucesso!", musics);
        } catch (Exception ex) {
            return ResponseHelper.GenerateErrorResponse("Erro ao obter músicas da playlist!", ex);
        }
    }

    public Response<Playlist> getPlaylistById(int playlistId) {
        try {
            if (playlistId <= 0) {
                return ResponseHelper.GenerateBadResponse("O id da playlist deve ser >= 0!");
            }

            Playlist playlist = playlistRepository.getPlaylistById(playlistId);

            if (playlist == null) {
                return ResponseHelper.GenerateBadResponse("Playlist não encontrada!");
            }

            return ResponseHelper.GenerateSuccessResponse("Playlist obtida com sucesso!", playlist);
        } catch (Exception ex) {
            return ResponseHelper.GenerateErrorResponse("Erro ao obter playlist!", ex);
        }
    }

    public Response<List<Playlist>> getPlaylistUser(int userId){
        try{
            if(userId <= 0){
                return ResponseHelper.GenerateBadResponse("O Id do usuário deve ser >= 0!");
            }
            List<Playlist> playlists = playlistRepository.getPlaylistUser(userId);

            if (playlists == null){
                return ResponseHelper.GenerateBadResponse("Playlist não encontrada!");
            }

            return ResponseHelper.GenerateSuccessResponse("Playlists Obtidas com sucesso!", playlists);
        }catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse("Erro ao obter as playlists", ex);
        }
    }
}