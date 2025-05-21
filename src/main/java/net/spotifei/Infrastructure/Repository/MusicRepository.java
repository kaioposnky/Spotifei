package net.spotifei.Infrastructure.Repository;

//imports
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Models.Genre;
import net.spotifei.Models.Music;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicRepository {

    private final JDBCRepository jdbcRepository;

    /**
     * Construtor da classe.
     * Injeta a dependência do JDBCRepository, que é a ferramenta de acesso ao banco de dados.
     *
     * @param jdbcRepository A instância do JDBCRepository a ser utilizada para as operações de banco de dados.
     */
    public MusicRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    /**
     * Realiza uma busca por músicas com base em um termo de pesquisa.
     *
     * @param searchTerm O termo a ser pesquisado no nome da música.
     * @return Uma lista de objetos Music que correspondem ao termo de pesquisa.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> searchMusics(String searchTerm) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("name", searchTerm);

            String sql = jdbcRepository.getQueryNamed("SearchMusicByName");
            List<Music> musics = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Music.class));

            return  musics;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Realiza uma busca por músicas com base em um termo de pesquisa, inclui detalhes importantes como
     * artistas, likes, dislikes e se o usuário gostou.
     *
     * @param searchTerm O termo a ser pesquisado no nome da música.
     * @param userId O ID do usuário para personalizar os detalhes (ex: se o usuário já curtiu a música).
     * @return Uma lista de objetos Music com informações detalhadas, incluindo dados de artistas e avaliações do usuário.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> searchMusicForUserWithDetails(String searchTerm, int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("name", searchTerm);
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("SearchMusicByNameForUserWithDetails");
            List<Map<String, Object>> musicInfoRaw = jdbcRepository.queryProcedure(sql, params, new MapListHandler());

            List<Music> musics = new ArrayList<>();
            for(Map<String, Object> musicInfo : musicInfoRaw){
                Music music = new Music();
                music.setIdMusica((Integer) musicInfo.get("idmusic"));
                music.setNome(musicInfo.get("name").toString());
                music.setDuracaoMs((Integer) musicInfo.get("durationms"));
                music.setArtistsNames(musicInfo.get("artisticnames").toString());
                music.setLikes((Long) musicInfo.get("likes"));
                music.setDislikes((Long) musicInfo.get("dislikes"));
                music.setGostou((Boolean) musicInfo.get("gostou"));
                musics.add(music);
            }

            return musics;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém a próxima música na fila de reprodução de um usuário.
     *
     * @param userId O ID do usuário.
     * @return O objeto Music correspondente à próxima música na fila, ou null se a fila estiver vazia ou não houver próxima música.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public Music getNextMusicOnUserQueue(int userId) throws Exception {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("GetNextMusicOnUserQueueByUserId");
            Music music = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Music.class));

            return music;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém a música anterior reproduzida por um usuário.
     *
     * @param userId O ID do usuário.
     * @return O objeto Music da música anterior, ou null se não houver registro.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public Music getPreviousMusicFromUser(int userId) throws Exception {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("GetUserPreviousMusic");
            Music music = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Music.class));

            return music;
        } catch (Exception ex){
            throw ex;
        }
    }

    /**
     * Obtém uma música aleatória do banco de dados.
     *
     * @return Um objeto Music aleatório.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public Music getRandomMusic() throws Exception {
        try{
            String sql = jdbcRepository.getQueryNamed("GetRandomMusic");
            Music music = jdbcRepository.queryProcedure(sql, new BeanHandler<>(Music.class));

            return music;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Recupera o arquivo de áudio de uma música como um array de bytes.
     *
     * @param musicId O ID da música cujo áudio será recuperado.
     * @return Um array de bytes contendo os dados do áudio da música.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public byte[] getMusicAsByteArray(int musicId) throws Exception {
        try{
            ScalarHandler<byte[]> handler = new ScalarHandler<>();
            Map<String, Object> params = new HashMap<>();
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("GetMusicAudioByMusicId");

            return jdbcRepository.queryProcedure(sql, params, handler);
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Salva o histórico de pesquisa de um usuário.
     *
     * @param userId O ID do usuário que realizou a pesquisa.
     * @param searchTerm O termo que foi pesquisado.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void saveUserSearch(int userId, String searchTerm) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("searchTerm", searchTerm);

            String sql = jdbcRepository.getQueryNamed("InsertMusicSearchHistory");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém as músicas mais pesquisadas por um usuário específico.
     *
     * @param userId O ID do usuário.
     * @param showAmount O número máximo de músicas a serem retornadas.
     * @return Uma lista das músicas mais pesquisadas pelo usuário.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getUserMostSearchedMusics(int userId, int showAmount) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("limit", showAmount);

            String sql = jdbcRepository.getQueryNamed("GetUserMostSearchedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém uma lista das músicas mais curtidas no sistema.
     *
     * @return Uma lista de objetos Music ordenados pelas mais curtidas.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getMostLikedMusics() throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMostLikedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém uma lista das músicas mais descurtidas no sistema.
     *
     * @return Uma lista de objetos Music ordenados pelas mais descurtidas.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getMostDislikedMusics() throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMostDislikedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém todas as músicas do banco de dados, incluindo seus gêneros e artistas.
     *
     * @return Uma lista de objetos Music com todos os detalhes de gênero e artistas.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getAllMusics() {
        try {
            String sql = jdbcRepository.getQueryNamed("getMusics");
            List<Map<String, Object>> results = jdbcRepository.queryProcedure(sql, new MapListHandler());
            Map<Integer, Music> musicMap = new HashMap<>();

            for (Map<String, Object> row : results) {
                Integer idMusica = (Integer) row.get("id_musica");
                if (idMusica == null) {
                    continue;
                }

                Music music = musicMap.get(idMusica);
                if (music == null) {
                    music = new Music();
                    music.setIdMusica(idMusica);
                    music.setNome((String) row.get("nome"));
                    music.setAutores(new ArrayList<>());
                    Genre genre = new Genre();
                    genre.setName((String) row.get("genero"));
                    music.setGenre(genre);
                    musicMap.put(idMusica, music);
                }

                String artista = (String) row.get("artista");
                if (artista != null && !music.getAutores().contains(artista)) {
                    music.getAutores();
                }
            }
            return new ArrayList<>(musicMap.values());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtém o número total de músicas cadastradas no banco de dados.
     *
     * @return O número total de músicas como um valor long.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public long getTotalMusics() throws Exception{
        try{
            ScalarHandler<Long> handler = new ScalarHandler<>();
            String sql = jdbcRepository.getQueryNamed("GetTotalMusics");
            long totalMusics = jdbcRepository.queryProcedure(sql, handler);

            return totalMusics;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém uma lista das músicas que um usuário curtiu.
     *
     * @param userId O ID do usuário.
     * @param limit O número máximo de músicas a serem retornadas.
     * @return Uma lista de objetos Music que o usuário curtiu.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getUserLikedMusics(int userId, int limit) throws Exception{
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("limit", limit);

            String sql = jdbcRepository.getQueryNamed("GetUserLikedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém uma lista das músicas que um usuário descurtiu.
     *
     * @param userId O ID do usuário.
     * @param limit O número máximo de músicas a serem retornadas.
     * @return Uma lista de objetos Music que o usuário descurtiu.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getUserDislikedMusics(int userId, int limit) throws Exception{
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("limit", limit);

            String sql = jdbcRepository.getQueryNamed("GetUserDislikedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém um objeto Music do banco de dados pelo seu ID.
     *
     * @param musicId O ID da música a ser pesquisada.
     * @return O objeto Music correspondente ao ID, ou null se não for encontrada.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public Music getMusicById(int musicId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("GetMusicById");
            Music music = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Music.class));

            return music;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Insere um registro no histórico de reproduções do usuário.
     *
     * @param userId O ID do usuário que reproduziu a música.
     * @param musicId O ID da música reproduzida.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void insertMusicPlayHistory(int userId, int musicId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("idMusic", musicId);

            String sql = jdbcRepository.getQueryNamed("InsertMusicPlayHistory");
            jdbcRepository.executeProcedure(sql, params);

        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Insere uma nova música no banco de dados, incluindo seus dados de áudio.
     *
     * @param music O objeto Music com os metadados da música (nome, duração, gênero).
     * @param audio O array de bytes contendo os dados de áudio da música.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void insertMusic(Music music, byte[] audio) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("nome", music.getNome());
            params.put("durationMs", music.getDuracaoMs());
            params.put("audio", audio);
            params.put("idGenre", music.getGenre().getIdGenre());

            String sql = jdbcRepository.getQueryNamed("InsertMusic");
            jdbcRepository.executeProcedure(sql, params);

        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Insere uma nova música no banco de dados e retorna o ID gerado para ela.
     *
     * @param music O objeto Music com os metadados da música (nome, duração, gênero).
     * @param audio O array de bytes contendo os dados de áudio da música.
     * @return O ID inteiro da música recém-inserida.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public int insertMusicAndGetReturnId(Music music, byte[] audio) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("name", music.getNome());
            params.put("durationMs", music.getDuracaoMs());
            params.put("audio", audio);
            params.put("idGenre", music.getGenre().getIdGenre());

            ScalarHandler<Integer> handler = new ScalarHandler<>();
            String sql = jdbcRepository.getQueryNamed("InsertMusicAndReturnId");
            int music_id = jdbcRepository.queryProcedure(sql, params, handler);

            return music_id;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Associa um artista a uma música no banco de dados.
     *
     * @param musicId O ID da música.
     * @param artistId O ID do artista a ser associado à música.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void insertArtistIntoMusic(int musicId, int artistId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idMusic", musicId);
            params.put("idArtist", artistId);

            String sql = jdbcRepository.getQueryNamed("InsertArtistIntoMusic");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém a última música reproduzida por um usuário.
     *
     * @param userId O ID do usuário.
     * @return O objeto Music da última música reproduzida, ou null se não houver histórico.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public Music getUserLastPlayedMusic(int userId) throws Exception {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("GetUserLastPlayedMusic");
            Music music = jdbcRepository.queryProcedure(sql, params, new BeanHandler<>(Music.class));

            return music;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Define ou atualiza a avaliação de um usuário para uma música (curtida/descurtida).
     *
     * @param musicId O ID da música avaliada.
     * @param userId O ID do usuário que fez a avaliação.
     * @param liked Um valor booleano: true para curtir, false para descurtir.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void setOrInsertMusicUserRating(int musicId, int userId, Boolean liked) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("idMusic", musicId);
            params.put("liked", liked);

            String sql = jdbcRepository.getQueryNamed("SetOrInsertMusicUserRating");
            jdbcRepository.executeProcedure(sql, params);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Exclui a avaliação de um usuário para uma música.
     *
     * @param musicId O ID da música.
     * @param userId O ID do usuário.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void deleteMusicUserRating(int musicId, int userId) throws Exception {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("idMusic", musicId);

            String sql = jdbcRepository.getQueryNamed("DeleteMusicUserRating");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém a avaliação de um usuário para uma música específica.
     *
     * @param musicId O ID da música.
     * @param userId O ID do usuário.
     * @return `true` se o usuário curtiu, `false` se descurtiu, ou `null` se não houver avaliação registrada.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public Boolean getUserRatingOnMusic(int musicId, int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("idMusic", musicId);

            String sql = jdbcRepository.getQueryNamed("GetUserRatingOnMusic");
            Boolean liked = jdbcRepository.queryProcedure(sql, params, new ScalarHandler<>());

            return liked;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Limpa a fila de reprodução de um usuário.
     *
     * @param userId O ID do usuário cuja fila será limpa.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void deleteUserQueue(int userId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("DeleteUserQueue");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Obtém uma lista de músicas de uma playlist específica para ser usada como fila de reprodução.
     *
     * @param playlistId O ID da playlist.
     * @return Uma lista de objetos Music contendo as músicas da playlist.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getPlaylistMusicForQueue(int playlistId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idPlaylist", playlistId);

            String sql = jdbcRepository.getQueryNamed("GetPlaylistMusicForQueueById");
            List<Music> musics = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Music.class));
            return musics;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Insere uma música na fila de reprodução de um usuário.
     *
     * @param userId O ID do usuário.
     * @param musicId O ID da música a ser inserida na fila.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void insertMusicIntoQueue(int userId, int musicId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("InsertMusicIntoQueue");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Exclui uma música da fila de reprodução de um usuário com base na sua posição na fila.
     *
     * @param userId O ID do usuário.
     * @param position A posição da música na fila (ex: 1 para a primeira, 2 para a segunda, etc.).
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void deleteMusicFromQueueByPosition(int userId, int position) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("posicao", position);

            String sql = jdbcRepository.getQueryNamed("DeleteMusicFromQueueByPosition");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Exclui uma música da fila de reprodução de um usuário com base no ID da música.
     *
     * @param userId O ID do usuário.
     * @param musicId O ID da música a ser removida da fila.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void deleteMusicFromQueueByMusicId(int userId, int musicId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);
            params.put("idMusica", musicId);

            String sql = jdbcRepository.getQueryNamed("DeleteMusicFromQueueByMusicId");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Obtém a fila de reprodução atual de um usuário.
     *
     * @param userId O ID do usuário.
     * @return Uma lista de objetos Music que representam a fila de reprodução do usuário.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public List<Music> getUserQueue(int userId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);

            String sql = jdbcRepository.getQueryNamed("GetUserQueue");
            List<Music> queue = jdbcRepository.queryProcedure(sql, params, new BeanListHandler<>(Music.class));
            return queue;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Exclui uma música permanentemente do banco de dados pelo seu ID.
     *
     * @param idMusic O ID da música a ser excluída.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void deleteMusic(int idMusic) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idMusic", idMusic);

            String sql = jdbcRepository.getQueryNamed("DeleteMusic");
            jdbcRepository.executeProcedure(sql, params);
        } catch(Exception e){
            throw e;
        }
    }

    /**
     * Exclui uma entrada da fila de reprodução de um usuário pelo ID específico da entrada da fila.
     *
     * @param musicQueueId O ID da entrada na fila de reprodução (identificador único da posição da música na fila).
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void deleteMusicFromQueueById(int musicQueueId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("musicQueueId", musicQueueId);

            String sql = jdbcRepository.getQueryNamed("DeleteMusicFromQueueById");
            jdbcRepository.executeProcedure(sql, params);
        } catch (Exception e){
            throw e;
        }
    }

}
