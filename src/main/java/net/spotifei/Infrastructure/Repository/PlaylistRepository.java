package net.spotifei.Infrastructure.Repository;

//import
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.Playlist;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistRepository {

    private final JDBCRepository jdbcRepository;

    /**
     * Construtor da classe.
     * Injeta a dependência do JDBCRepository, que é a ferramenta de acesso ao banco de dados.
     *
     * @param jdbcRepository A instância do JDBCRepository a ser utilizada.
     */
    public PlaylistRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    /**
     * Cria uma nova playlist no banco de dados para um usuário.
     *
     * @param playlist O objeto `Playlist` contendo o nome da nova playlist.
     * @param userId O ID do usuário que está criando a playlist.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void createPlaylist(Playlist playlist, int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("nome", playlist.getNome());
            params.put("userId", userId);

            String sql = jdbcRepository.getQueryNamed("CreatePlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    /**
     * Exclui uma playlist do banco de dados pelo seu ID.
     *
     * @param playlistId O ID da playlist a ser excluída.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void deletePlaylist(int playlistId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("DeletePlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    /**
     * Atualiza o nome de uma playlist existente no banco de dados.
     *
     * @param playlist O objeto `Playlist` contendo o ID da playlist a ser atualizada e o novo nome.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void updatePlaylist(Playlist playlist) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("nome", playlist.getNome());
            params.put("idPlaylist", playlist.getIdPlaylist());

            String sql = jdbcRepository.getQueryNamed("UpdatePlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    /**
     * Adiciona uma música a uma playlist.
     *
     * @param music O objeto `Music` contendo o ID da música a ser adicionada.
     * @param playlistId O ID da playlist à qual a música será adicionada.
     * @param position A posição (ordem) em que a música será inserida na playlist.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void addMusicToPlaylist(Music music, int playlistId, int position) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);
            params.put("idMusica", music.getIdMusica());
            params.put("position", position);

            String sql = jdbcRepository.getQueryNamed("AddMusicToPlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    /**
     * Remove uma música de uma playlist.
     *
     * @param musicId O ID da música a ser removida.
     * @param playlistId O ID da playlist da qual a música será removida.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void removeMusicFromPlaylist(int musicId, int playlistId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("RemoveMusicFromPlaylist");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    /**
     * Obtém uma lista de todas as músicas dentro de uma playlist.
     *
     * @param playlistId O ID da playlist cujas músicas serão buscadas.
     * @return Uma lista de objetos `Music` que pertencem à playlist.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getMusicsFromPlaylist(int playlistId) throws Exception {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("GetMusicsFromPlaylist");
            List<Music> musics = jdbcRepository.queryProcedure(sql,
                    params, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    /**
     * Obtém uma playlist do banco de dados pelo seu ID.
     *
     * @param playlistId O ID da playlist a ser pesquisada.
     * @return O objeto `Playlist` correspondente ao ID, ou `null` se não for encontrada.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public Playlist getPlaylistById(int playlistId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("GetPlaylistById");
            Playlist playlist = jdbcRepository.queryProcedure(
                    sql, params, new BeanHandler<>(Playlist.class));

            return playlist;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    /**
     * Obtém a última posição de música em uma playlist.
     *
     * @param playlistId O ID da playlist.
     * @return A última posição ocupada na playlist como um inteiro.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public int getLastPlaylistPosition(int playlistId) throws Exception{
        try {
            ScalarHandler<Integer> handler = new ScalarHandler<>();
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("GetMaxPlaylistPosition");
            int lastPosition = jdbcRepository.queryProcedure(sql, params, handler);

            return lastPosition;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém uma lista de todas as playlists pertencentes a um usuário.
     *
     * @param userId O ID do usuário cujas playlists serão buscadas.
     * @return Uma lista de objetos `Playlist` de propriedade do usuário.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Playlist> getPlaylistUser(int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);

            String sql = jdbcRepository.getQueryNamed("GetPlaylistUser");
            List<Playlist> playlists = jdbcRepository.queryProcedure(sql,
                    params, new BeanListHandler<>(Playlist.class));
            return playlists;

        } catch(Exception ex){
            throw ex;
        }
    }

    /**
     * Define uma playlist existente como a fila de reprodução atual para um usuário.
     *
     * @param playlistId O ID da playlist a ser definida como fila.
     * @param userId O ID do usuário para o qual a fila será definida.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void setPlaylistAsQueueForUser(int playlistId, int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("SetPlaylistAsQueueForUser");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception ex){
            throw ex;
        }
    }

}
