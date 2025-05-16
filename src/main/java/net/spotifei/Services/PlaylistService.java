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
                return ResponseHelper.generateBadResponse("A playlist fornecida foi nula!");
            }

            playlistRepository.createPlaylist(playlist, userId);

            return ResponseHelper.generateSuccessResponse("Playlist criada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse("Erro ao criar playlist!", ex);
        }
    }

    public Response<Void> deletePlaylist(int playlistId){
        try{
            if (playlistId <= 0){
                return ResponseHelper.generateBadResponse("O id da playlist deve ser >= 0!");
            }

            playlistRepository.deletePlaylist(playlistId);

            return ResponseHelper.generateSuccessResponse("Playlist deletada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse("Erro ao deletar playlist!", ex);
        }
    }

    public Response<Void> updatePlaylist(Playlist playlist){
        try{
            if (playlist == null || playlist.getIdPlaylist() <= 0){
                return ResponseHelper.generateBadResponse("Os campos playlist(incluidno o id da playlist)" +
                        "não podem ser nulos ou zero!");
            }

            playlistRepository.updatePlaylist(playlist);

            return ResponseHelper.generateSuccessResponse("Playlist atualizada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse("Erro ao atualizar playlist!", ex);
        }
    }

    public Response<Void> addMusicToLastPositionInPlaylist(int musicId, int playlistId){
        try{
            if (musicId == 0 || playlistId <= 0){
                return ResponseHelper.generateBadResponse(
                        "Os campos musicId e " +
                                "playlistId não podem ser nulos ou zero!");
            }

            Music music = musicRepository.getMusicById(musicId);

            if(music == null){
                return ResponseHelper.generateBadResponse("Música não encontrada!");
            }

            int position = playlistRepository.getLastPlaylistPosition(playlistId);

            if(position == 0){
                position++;
            }

            playlistRepository.addMusicToPlaylist(music, playlistId, position);

            return ResponseHelper.generateSuccessResponse("Música adicionada à playlist com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse("Erro ao adicionar música à playlist!", ex);
        }
    }

    public Response<Void> removeMusicFromPlaylist(int musicId, int playlistId){
        try{
            if (musicId == 0 || playlistId <= 0){
                return ResponseHelper.generateBadResponse(
                        "Os campos musicId e playlistId não podem ser nulos ou zero!");
            }

            playlistRepository.removeMusicFromPlaylist(musicId, playlistId);

            return ResponseHelper.generateSuccessResponse("Música removida da playlist com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse("Erro ao remover música da playlist!", ex);
        }
    }

    public Response<List<Music>> getMusicsFromPlaylist(int playlistId) {
        try {
            if (playlistId <= 0) {
                return ResponseHelper.generateBadResponse("O id da playlist deve ser >= 0!");
            }

            List<Music> musics = playlistRepository.getMusicsFromPlaylist(playlistId);

            return ResponseHelper.generateSuccessResponse("Músicas da playlist obtidas com sucesso!", musics);
        } catch (Exception ex) {
            return ResponseHelper.generateErrorResponse("Erro ao obter músicas da playlist!", ex);
        }
    }

    public Response<Playlist> getPlaylistById(int playlistId) {
        try {
            if (playlistId <= 0) {
                return ResponseHelper.generateBadResponse("O id da playlist deve ser >= 0!");
            }

            Playlist playlist = playlistRepository.getPlaylistById(playlistId);

            if (playlist == null) {
                return ResponseHelper.generateBadResponse("Playlist não encontrada!");
            }

            return ResponseHelper.generateSuccessResponse("Playlist obtida com sucesso!", playlist);
        } catch (Exception ex) {
            return ResponseHelper.generateErrorResponse("Erro ao obter playlist!", ex);
        }
    }

    public Response<List<Playlist>> getPlaylistUser(int userId){
        try{
            if(userId <= 0){
                return ResponseHelper.generateBadResponse("O Id do usuário deve ser >= 0!");
            }
            List<Playlist> playlists = playlistRepository.getPlaylistUser(userId);

            if (playlists == null){
                return ResponseHelper.generateBadResponse("Playlist não encontrada!");
            }

            return ResponseHelper.generateSuccessResponse("Playlists Obtidas com sucesso!", playlists);
        }catch (Exception ex){
            return ResponseHelper.generateErrorResponse("Erro ao obter as playlists", ex);
        }
    }
}