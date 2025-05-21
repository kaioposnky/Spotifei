package net.spotifei.Services;

//imports
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

    /**
     * Construtor da classe `PlaylistService`.
     * Injeta as dependências do `PlaylistRepository` e `MusicRepository`.
     *
     * @param playlistRepository A instância de `PlaylistRepository` a ser utilizada.
     * @param musicRepository A instância de `MusicRepository` a ser utilizada.
     */
    public PlaylistService(PlaylistRepository playlistRepository, MusicRepository musicRepository) {
        this.playlistRepository = playlistRepository;
        this.musicRepository = musicRepository;
    }

    /**
     * Cria uma nova playlist no sistema.
     *
     * @param playlist O objeto `Playlist` a ser criado.
     * @param userId O ID do usuário que está criando a playlist.
     * @return Uma `Response<Void>` indicando o sucesso ou falha da operação.
     */
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

    /**
     * Deleta uma playlist existente pelo seu ID.
     *
     * @param playlistId O ID da playlist a ser deletada.
     * @return Uma `Response<Void>` indicando o sucesso ou falha da operação.
     */
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

    /**
     * Atualiza os dados de uma playlist.
     *
     * @param playlist O objeto `Playlist` com os dados atualizados (deve conter o ID da playlist).
     * @return Uma `Response<Void>` indicando o sucesso ou falha da operação.
     */
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

    /**
     * Adiciona uma música na última posição de uma playlist.
     *
     * @param musicId O ID da música a ser adicionada.
     * @param playlistId O ID da playlist.
     * @return Uma `Response<Void>` indicando o sucesso ou falha da operação.
     */
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

            // ultima posicao +1 para ser um depois da última posição
            playlistRepository.addMusicToPlaylist(music, playlistId, position + 1);

            return ResponseHelper.generateSuccessResponse("Música adicionada à playlist com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse("Erro ao adicionar música à playlist!", ex);
        }
    }

    /**
     * Remove uma música de uma playlist.
     *
     * @param musicId O ID da música a ser removida.
     * @param playlistId O ID da playlist da qual a música será removida.
     * @return Uma `Response<Void>` indicando o sucesso ou falha da operação.
     */
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

    /**
     * Obtém uma playlist pelo seu ID.
     *
     * @param playlistId O ID da playlist a ser obtida.
     * @return Uma `Response<Playlist>` contendo o objeto `Playlist` ou uma mensagem de erro.
     */
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

    /**
     * Obtém todas as playlists de um usuário.
     *
     * @param userId O ID do usuário.
     * @return Uma `Response<List<Playlist>>` contendo a lista de playlists do usuário.
     */
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

    /**
     * Define uma playlist inteira como a fila de reprodução para um usuário.
     *
     * @param playlistId O ID da playlist a ser usada como fila.
     * @param userId O ID do usuário.
     * @return Uma `Response<Void>` indicando o sucesso ou falha da operação.
     */
    public Response<Void> setPlaylistAsQueueForUser(int playlistId, int userId){
        try{

            List<Music> musics = playlistRepository.getMusicsFromPlaylist(playlistId);

            if (musics == null || musics.isEmpty()){
                return ResponseHelper.generateBadResponse("A playlist não tem músicas para tocar!");
            }
            musicRepository.deleteUserQueue(userId);

            playlistRepository.setPlaylistAsQueueForUser(playlistId, userId);

            return ResponseHelper.generateSuccessResponse("Playlist colocada na Fila do usuário com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }
}