package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Repository.ArtistRepository;
import net.spotifei.Infrastructure.Repository.GenreRepository;
import net.spotifei.Infrastructure.Repository.MusicRepository;
import net.spotifei.Infrastructure.Repository.PlaylistRepository;
import net.spotifei.Models.Artist;
import net.spotifei.Models.Genre;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static net.spotifei.Infrastructure.AudioPlayer.OpusConverter.getMP3DurationInMicrosseconds;

public class MusicService {

    private final MusicRepository musicRepository;
    private final ArtistRepository artistRepository;
    private final PlaylistRepository playlistRepository;
    private final GenreRepository genreRepository;
    private final AudioPlayerWorker audioPlayerWorker;

    private boolean isNewMusicSelected = false;
    private int newMusicSelectedId = 0;

    public MusicService(MusicRepository musicRepository, AudioPlayerWorker audioPlayerWorker, ArtistRepository artistRepository, PlaylistRepository playlistRepository, GenreRepository genreRepository){
        this.musicRepository = musicRepository;
        this.audioPlayerWorker = audioPlayerWorker;
        this.artistRepository = artistRepository;
        this.playlistRepository = playlistRepository;
        this.genreRepository = genreRepository;
    }

    public Response<Music> getNextMusicInUserQueue(int userId){
        try{

            Music music = musicRepository.getNextMusicOnUserQueue(userId);

            if(music == null){
                // uma alternativa inteligente de lidar com quando a próxima música "não existe"
                music = musicRepository.getRandomMusic();
            } else{
                musicRepository.deleteMusicFromQueueById(music.getIdMusicaFila());
            }

            return ResponseHelper.generateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse("Ocorreu um erro ao tentar" +
                    "tocar uma música!", e);
        }
    }

    public Response<Music> getMusicById(int musicId){
        try{
            Music music = musicRepository.getMusicById(musicId);

            if (music == null){
                return ResponseHelper.generateBadResponse("A música retornada foi nula!");
            }

            putAuthorsIntoMusic(music);
            putGenreIntoMusic(music);

            return ResponseHelper.generateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse("Erro ao obter música!", e);
        }
    }

    public Response<Void> playMusic(int musicId, int userId){
        try{
            if(userId == 0 || musicId == 0){
                return ResponseHelper.generateBadResponse("Os parâmetros de id não podem ser nulos ou zero");
            }

            Music music = musicRepository.getMusicById(musicId);
            List<Artist> artists = artistRepository.getArtistsByMusicId(musicId);
            Boolean userMusicRating = musicRepository.getUserRatingOnMusic(musicId, userId);
            byte[] musicAudio = musicRepository.getMusicAsByteArray(musicId);

            if(musicAudio == null){
                return ResponseHelper.generateBadResponse("O aúdio retornado foi nulo! Operação cancelada!");
            }

            music.setAutores(artists);
            music.setGostou(userMusicRating);

            isNewMusicSelected = true;
            newMusicSelectedId = musicId;

            audioPlayerWorker.playMusic(musicAudio);
            audioPlayerWorker.selectMusic(music); // atualiza o UI

            return ResponseHelper.generateSuccessResponse("Música iniciada com sucesso!");
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse("Ocorreu um erro ao tentar" +
                    "tocar uma música!", e);
        }
    }

    public Response<Boolean> getUserMusicRating(int musicId, int userId){
        try{

            if(userId == 0 || musicId == 0){
                return ResponseHelper.generateBadResponse("Os parâmetros de id não podem ser nulos ou zero");
            }

            Boolean userMusicRating = musicRepository.getUserRatingOnMusic(musicId, userId);

            return ResponseHelper.generateSuccessResponse(
                    "Avaliação do usuário para a música retornada com sucesso!", userMusicRating);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse(e.getMessage(), e);
        }
    }

    public Response<Void> setAudioVolume(float volume){
        try {
            if(volume < 0.0f || volume > 100.0f){
                return ResponseHelper.generateBadResponse("O volume deve estar entre 0 e 100!");
            }

            // audioplayerworker só aceita volume entre 0f e 1f
            volume = volume / 100;

            audioPlayerWorker.setVolume(volume);

            return ResponseHelper.generateSuccessResponse("Volume alterado com sucesso!");

        } catch (Exception e){
            return ResponseHelper.generateErrorResponse("Erro ao tentar alterar o volume!", e);
        }
    }

    public Response<Void> setMusicTime(float musicTime){
        try {
            if(musicTime < 0 || musicTime > 100){
                return ResponseHelper.generateBadResponse("O volume deve estar entre 0 e 100!");
            }

            // audioplayerworker só aceita volume entre 0f e 1f
            musicTime = musicTime / 100;

            audioPlayerWorker.seek(musicTime);

            return ResponseHelper.generateSuccessResponse("Volume alterado com sucesso!");

        } catch (Exception e){
            return ResponseHelper.generateErrorResponse("Erro ao tentar alterar o volume!", e);
        }
    }

    public Response<Void> pauseMusic(){
        try{
            if (audioPlayerWorker.isPlaying()){
                audioPlayerWorker.pause();
            } else{
                audioPlayerWorker.resume();
            }

            return ResponseHelper.generateSuccessResponse("Música pausada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse("Erro ao tentar pausar a música!", ex);
        }
    }

    public Response<Void> addMusicToUserHistory(int userId, int musicId){
        try{
            if(userId == 0 || musicId == 0){
                return ResponseHelper.generateBadResponse("Os parâmetros de id não podem ser nulos ou zero");
            }

            musicRepository.insertMusicPlayHistory(userId, musicId);

            return ResponseHelper.generateSuccessResponse("Música adicionada ao histórico com sucesso");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }

    }

    public Response<Void> registerMusic(String musicFilePath, String musicName, String musicArtist, String musicGenre){
        try{
            String[] infos = {musicFilePath, musicName, musicArtist, musicGenre};
            if (Arrays.stream(infos).anyMatch(info -> info == null || info.isEmpty())){
                return ResponseHelper.generateBadResponse("Nenhum dos argumentos pode ser nulo ou vazio!");
            }

            File musicFile = new File(musicFilePath);
            if (!musicFile.exists()){
                throw new FileNotFoundException("O arquivo de música não existe!");
            }
//            byte[] fileBytes = convertMP3FileToOpusBytes(musicFile);
            byte[] fileBytes = Files.readAllBytes(Paths.get(musicFilePath));
            if (fileBytes == null){
                return ResponseHelper.generateBadResponse("Não foi possível obter os bytes do arquivo!");
            }
            Genre genre = genreRepository.getGenreByName(musicGenre);
            if (genre == null){
                return ResponseHelper.generateBadResponse("Gênero não encontrado! (" + musicGenre + ") ");
            }

            Artist artist = artistRepository.getArtistByName(musicArtist);
            if (artist == null){
                return ResponseHelper.generateBadResponse("Artista não encontrado, use o nome artístico! (" + musicArtist + ") ");
            }

            long musicDuration = getMP3DurationInMicrosseconds(musicFile);

            Music music = new Music();
            music.setNome(musicName);
            music.setDuracaoMs(musicDuration);
            music.setGenre(genre);

            // cria a música e já retorna o id para usarmos para juntar com o artista
            int musicId = musicRepository.insertMusicAndGetReturnId(
                    music, fileBytes);

            musicRepository.insertArtistIntoMusic(musicId, artist.getIdArtista());

            return ResponseHelper.generateSuccessResponse("Música cadastrada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Music> getUserLastPlayedMusic(int userId){
        try{
            if(userId == 0){
                return ResponseHelper.generateBadResponse("O parâmetro de id não pode ser nulo ou zero");
            }

            Music music = musicRepository.getUserLastPlayedMusic(userId);

            // se retornar nulo é pq nn tem nenhuma música registrada
            if(music == null){
                music = musicRepository.getRandomMusic();
            }

            putAuthorsIntoMusic(music);
            putGenreIntoMusic(music);

            return ResponseHelper.generateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Void> setOrInsertMusicUserRating(int musicId, int userId, Boolean liked){
        try{
            if (userId <= 0 || musicId <= 0){
                return ResponseHelper.generateBadResponse("Os ids não podem ser nulos ou zero!");
            }

            // se for nulo o usuario tirou a avaliacao
            if(liked == null){
                musicRepository.deleteMusicUserRating(musicId, userId);
            } else{
                musicRepository.setOrInsertMusicUserRating(musicId, userId, liked);
            }

            return ResponseHelper.generateSuccessResponse("Feedback da música atualizado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<List<Music>> searchMusicsWithUserDetails(String searchTerm, int userId){
        try{
            if(searchTerm.isBlank()){
                return ResponseHelper.generateBadResponse("O termo de pesquisa não pode estar vazio!");
            }
            if(userId == 0){
                return ResponseHelper.generateBadResponse("O parâmetro de id não pode ser nulo ou zero");
            }

            List<Music> musics = musicRepository.searchMusicForUserWithDetails(searchTerm, userId);
            if(musics == null || musics.isEmpty()){
                return ResponseHelper.generateBadResponse("Nenhuma música foi encontrada com o termo de pesquisa: " + searchTerm);
            }

            for(Music music : musics){
                putGenreIntoMusic(music);
            }

            return ResponseHelper.generateSuccessResponse("Músicas encontradas com sucesso!", musics);

        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<List<Music>> searchMusics(String searchTerm){
        try{
            if(searchTerm.isBlank()){
                return ResponseHelper.generateBadResponse("O termo de pesquisa não pode estar vazio!");
            }

            List<Music> musics = musicRepository.searchMusics(searchTerm);
            if(musics == null || musics.isEmpty()){
                return ResponseHelper.generateBadResponse("Nenhuma música foi encontrada com o termo de pesquisa: " + searchTerm);
            }

            for(Music music : musics){
                putAuthorsIntoMusic(music);
                putGenreIntoMusic(music);
            }

            return ResponseHelper.generateSuccessResponse("Músicas encontradas com sucesso!", musics);

        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<List<Music>> getUserMostSearchedMusics(int userId, int showAmount){
        try{
            if (userId <= 0 || showAmount <= 0){
                return ResponseHelper.generateBadResponse("Os valores userId e showAmount devem ser >= 0 !");
            }

            List<Music> musics = musicRepository.getUserMostSearchedMusics(userId, showAmount);

            for( Music music : musics){
                List<Artist> artist = artistRepository.getArtistsByMusicId(music.getIdMusica());
                music.setAutores(artist);
            }

            return ResponseHelper.generateSuccessResponse("Músicas encontradas com sucesso!", musics);
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    public boolean isNewMusicSelected() {
        return isNewMusicSelected;
    }

    public void setNewMusicSelected(boolean newMusicSelected) {
        isNewMusicSelected = newMusicSelected;
    }

    public int getNewMusicSelectedId() {
        return newMusicSelectedId;
    }

    public void setNewMusicSelectedId(int newMusicSelectedId) {
        this.newMusicSelectedId = newMusicSelectedId;
    }

    public Response<List<Music>> deleteMusic(int idMusic){
        try{
            if(idMusic == 0){
                return ResponseHelper.generateBadResponse("O parâmetro de id não pode ser nulo ou zero");
            }
            List<Music> music = musicRepository.getMusicDeleted(idMusic);
            if(music == null || music.isEmpty()){
                return ResponseHelper.generateBadResponse("Nenhuma música foi encontrada com o ID digitado!");
            }
            return ResponseHelper.generateSuccessResponse("Música Deletada com sucesso!", music);
        } catch(Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<List<Music>> getUserLikedMusics(int userId, int limit){
        try{
            if(userId <= 0 || limit <= 0){
                return ResponseHelper.generateBadResponse("O id do usuário e o limite devem ser >= 0!");
            }

            List<Music> musics = musicRepository.getUserLikedMusics(userId, limit);
            for(Music music : musics){
                putAuthorsIntoMusic(music);
            }
            return ResponseHelper.generateSuccessResponse("Músicas obtidas com sucesso!", musics);

        } catch (Exception e){
            return ResponseHelper.generateErrorResponse(e.getMessage(), e);
        }
    }

    public Response<List<Music>> getUserDislikedMusics(int userId, int limit){
        try{
            if(userId <= 0 || limit <= 0){
                return ResponseHelper.generateBadResponse("O id do usuário e o limite devem ser >= 0!");
            }

            List<Music> musics = musicRepository.getUserDislikedMusics(userId, limit);
            for(Music music : musics){
                putAuthorsIntoMusic(music);
            }

            return ResponseHelper.generateSuccessResponse("Músicas obtidas com sucesso!", musics);

        } catch (Exception e){
            return ResponseHelper.generateErrorResponse(e.getMessage(), e);
        }
    }

    public Response<Long> getTotalMusics(){
        try{
            long total = musicRepository.getTotalMusics();
            return ResponseHelper.generateSuccessResponse("Total de músicas obtido com sucesso.", total);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse(e.getMessage(), e);
        }
    }

    public Response<List<Music>> getMostLikedMusics(){
        try{
            List<Music> musics = musicRepository.getMostLikedMusics();
            for (Music music : musics){
                putAuthorsIntoMusic(music);
                putGenreIntoMusic(music);
            }
            return ResponseHelper.generateSuccessResponse("Músicas obtidas com sucesso!", musics);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse(e.getMessage(), e);
        }
    }

    public Response<List<Music>> getMostDislikedMusics(){
        try{
            List<Music> musics = musicRepository.getMostDislikedMusics();
            for (Music music : musics){
                putAuthorsIntoMusic(music);
                putGenreIntoMusic(music);
            }
            return ResponseHelper.generateSuccessResponse("Músicas obtidas com sucesso!", musics);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse(e.getMessage(), e);
        }
    }

    public Response<List<Music>> getMusicsFromPlaylist(int playlistId) {
        try {
            if (playlistId <= 0) {
                return ResponseHelper.generateBadResponse("O id da playlist deve ser >= 0!");
            }

            List<Music> musics = playlistRepository.getMusicsFromPlaylist(playlistId);
            for(Music music : musics){
                putAuthorsIntoMusic(music);
                putGenreIntoMusic(music);
            }

            return ResponseHelper.generateSuccessResponse("Músicas da playlist obtidas com sucesso!", musics);
        } catch (Exception ex) {
            return ResponseHelper.generateErrorResponse("Erro ao obter músicas da playlist!", ex);
        }
    }

    // sim criei isso no music service porque estou com preguiça de criar um outro service para colocar 1 funçao...
    public Response<Void> createGenre(String name){
        try{
            genreRepository.createGenre(name);

            return ResponseHelper.generateSuccessResponse("Gênero criado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    private void putAuthorsIntoMusic(Music music) throws Exception{
        List<Artist> artists = artistRepository.getArtistsByMusicId(music.getIdMusica());
        if (artists == null || artists.isEmpty()){
            throw new IllegalStateException("Não há artistas para a música ou os artistas são nulos!");
        }
        music.setAutores(artists);
    }

    private void putGenreIntoMusic(Music music) throws Exception{
        Genre genre = genreRepository.getGenreByMusicId(music.getIdMusica());
        if (genre == null){
            throw new IllegalStateException("Não há gênero musical para essa música!");
        }
        music.setGenre(genre);
    }
}
