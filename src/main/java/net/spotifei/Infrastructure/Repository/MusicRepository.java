package net.spotifei.Infrastructure.Repository;

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

    public MusicRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

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

    public Music getRandomMusic() throws Exception {
        try{
            String sql = jdbcRepository.getQueryNamed("GetRandomMusic");
            Music music = jdbcRepository.queryProcedure(sql, new BeanHandler<>(Music.class));

            return music;
        } catch (Exception e){
            throw e;
        }
    }

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

    public List<Music> getMostLikedMusics() throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMostLikedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            throw e;
        }
    }

    public List<Music> getMostDislikedMusics() throws Exception{
        try{
            String sql = jdbcRepository.getQueryNamed("GetMostDislikedMusics");
            List<Music> musics = jdbcRepository.queryProcedure(sql, new BeanListHandler<>(Music.class));

            return musics;
        } catch (Exception e){
            throw e;
        }
    }

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
