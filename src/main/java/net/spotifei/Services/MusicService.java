package net.spotifei.Services;

//imports
import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Container.AppContext;
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
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;

public class MusicService {

    private final MusicRepository musicRepository;
    private final ArtistRepository artistRepository;
    private final PlaylistRepository playlistRepository;
    private final GenreRepository genreRepository;
    private final AudioPlayerWorker audioPlayerWorker;
    private final AppContext appContext;

    private boolean isNewMusicSelected = false;
    private int newMusicSelectedId = 0;

    /**
     * Construtor da classe `MusicService`.
     * Injeta as dependências dos repositórios e do AudioPlayerWorker.
     *
     * @param musicRepository Repositório para operações com músicas.
     * @param audioPlayerWorker Componente para controle de áudio.
     * @param artistRepository Repositório para operações com artistas.
     * @param playlistRepository Repositório para operações com playlists.
     * @param genreRepository Repositório para operações com gêneros.
     */
    public MusicService(MusicRepository musicRepository, AudioPlayerWorker audioPlayerWorker, ArtistRepository artistRepository, PlaylistRepository playlistRepository, GenreRepository genreRepository, AppContext appContext){
        this.musicRepository = musicRepository;
        this.audioPlayerWorker = audioPlayerWorker;
        this.artistRepository = artistRepository;
        this.playlistRepository = playlistRepository;
        this.genreRepository = genreRepository;
        this.appContext = appContext;
    }

    /**
     * Obtém a primeira música na fila de reprodução de um usuário.
     *
     * @param userId O ID do usuário.
     * @return Uma `Response` contendo o objeto `Music` a ser tocado.
     */
    public Response<Music> getFirstMusicInUserQueue(int userId){
        try{

            Music music = musicRepository.getNextMusicOnUserQueue(userId);

            if(music == null){
                // uma alternativa inteligente de lidar com quando a próxima música "não existe"
                music = musicRepository.getRandomMusic();
            }

            return ResponseHelper.generateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse("Ocorreu um erro ao tentar" +
                    "tocar uma música!", e);
        }
    }

    /**
     * Obtém a próxima música na fila de reprodução de um usuário.
     *
     * @param userId O ID do usuário.
     * @return Uma `Response` contendo o objeto `Music` a ser tocado.
     */
    public Response<Music> getNextMusicInUserQueue(int userId){
        try{

            Music music = musicRepository.getNextMusicOnUserQueue(userId);

            if(music != null){
                // uma alternativa inteligente de lidar com quando a próxima música "não existe"
                musicRepository.deleteMusicFromQueueById(music.getIdMusicaFila());
            } else{
                music = musicRepository.getRandomMusic();
            }

            return ResponseHelper.generateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse("Ocorreu um erro ao tentar" +
                    "tocar uma música!", e);
        }
    }

    /**
     * Obtém a música anterior na fila de reprodução do usuário.
     *
     * @param userId O ID do usuário.
     * @return Uma `Response` contendo o objeto `Music` anterior.
     */
    public Response<Music> getUserPreviousMusic(int userId){
        try{
            Music music = musicRepository.getPreviousMusicFromUser(userId);

            putAuthorsIntoMusic(music);
            putGenreIntoMusic(music);

            return ResponseHelper.generateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Obtém os detalhes de uma música pelo seu ID.
     *
     * @param musicId O ID da música.
     * @return Uma `Response` contendo o objeto `Music` completo.
     */
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

    /**
     * Inicia a reprodução de uma música para um usuário.
     *
     * @param musicId O ID da música a ser tocada.
     * @param userId O ID do usuário que está solicitando a reprodução.
     * @return Uma `Response` indicando o sucesso da operação.
     */
    public Response<Void> playMusic(int musicId, int userId){
        try{
            if(userId == 0 || musicId == 0){
                return ResponseHelper.generateBadResponse("Os parâmetros de id não podem ser nulos ou zero");
            }

            Music music = musicRepository.getMusicById(musicId);
            List<Artist> artists = artistRepository.getArtistsByMusicId(musicId);
            Boolean userMusicRating = musicRepository.getUserRatingOnMusic(musicId, userId);

            byte[] musicAudio;
            if(appContext.getMusicCache().cacheContains(musicId)){
                musicAudio = appContext.getMusicCache().getMusicBytesFromCache(musicId);
            } else{
                musicAudio = musicRepository.getMusicAsByteArray(musicId);
                appContext.getMusicCache().addMusicBytesToCache(musicId, musicAudio);
            }

            if(musicAudio == null){
                return ResponseHelper.generateBadResponse("O aúdio retornado foi nulo! Operação cancelada!");
            }

            music.setAutores(artists);
            music.setGostou(userMusicRating);

            isNewMusicSelected = true;
            newMusicSelectedId = musicId;

            audioPlayerWorker.playMusic(musicAudio);
            audioPlayerWorker.selectMusic(music); // atualiza o UI

            musicAudio = null;

            return ResponseHelper.generateSuccessResponse("Música iniciada com sucesso!");
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse("Ocorreu um erro ao tentar" +
                    "tocar uma música!", e);
        }
    }

    /**
     * Obtém a avaliação de um usuário para uma música.
     *
     * @param musicId O ID da música.
     * @param userId O ID do usuário.
     * @return Uma `Response` contendo um `Boolean` (true para curtiu, false para descurtiu, null para não avaliou).
     */
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

    /**
     * Define o volume de reprodução do áudio.
     *
     * @param volume O volume desejado (0 a 100).
     * @return Uma `Response` indicando o sucesso da operação.
     */
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

    /**
     * Define o tempo de reprodução atual da música.
     *
     * @param musicTime O tempo desejado em percentual (0 a 100).
     * @return Uma `Response` indicando o sucesso da operação.
     */
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

    /**
     * Pausa ou retoma a reprodução da música.
     *
     * @return Uma `Response` indicando o sucesso da operação.
     */
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

    /**
     * Adiciona um registro da música ao histórico de reprodução de um usuário.
     *
     * @param userId O ID do usuário.
     * @param musicId O ID da música.
     * @return Uma `Response` indicando o sucesso da operação.
     */
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

    /**
     * Registra uma nova música no sistema.
     *
     * @param musicFilePath O caminho completo para o arquivo de áudio da música (MP3).
     * @param musicName O nome da música.
     * @param musicArtist O nome artístico do autor da música.
     * @param musicGenre O nome do gênero da música.
     * @return Uma `Response` indicando o sucesso da operação.
     */
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
            fileBytes = null;
            return ResponseHelper.generateSuccessResponse("Música cadastrada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Sobrecarga do método `registerMusic`, que aceita o ID do artista em vez do nome.
     *
     * @param musicFilePath O caminho completo para o arquivo de áudio da música (MP3).
     * @param musicName O nome da música.
     * @param artistId O ID do artista autor da música.
     * @param musicGenre O nome do gênero da música.
     * @return Uma `Response` indicando o sucesso da operação.
     */
    public Response<Void> registerMusic(String musicFilePath, String musicName, int artistId, String musicGenre){
        try{
            String[] infos = {musicFilePath, musicName, musicGenre};
            if (Arrays.stream(infos).anyMatch(info -> info == null || info.isEmpty())){
                return ResponseHelper.generateBadResponse("Nenhum dos argumentos pode ser nulo ou vazio!");
            }

            File musicFile = new File(musicFilePath);
            if (!musicFile.exists()){
                throw new FileNotFoundException("O arquivo de música não existe!");
            }

            byte[] fileBytes = Files.readAllBytes(Paths.get(musicFilePath));
            if (fileBytes == null){
                return ResponseHelper.generateBadResponse("Não foi possível obter os bytes do arquivo!");
            }
            Genre genre = genreRepository.getGenreByName(musicGenre);
            if (genre == null){
                return ResponseHelper.generateBadResponse("Gênero não encontrado! (" + musicGenre + ") ");
            }

            long musicDuration = getMP3DurationInMicrosseconds(musicFile);

            Music music = new Music();
            music.setNome(musicName);
            music.setDuracaoMs(musicDuration);
            music.setGenre(genre);

            // cria a música e já retorna o id para usarmos para juntar com o artista
            int musicId = musicRepository.insertMusicAndGetReturnId(
                    music, fileBytes);

            musicRepository.insertArtistIntoMusic(musicId, artistId);
            fileBytes = null;
            return ResponseHelper.generateSuccessResponse("Música cadastrada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Obtém a última música tocada por um usuário.
     *
     * @param userId O ID do usuário.
     * @return Uma `Response` contendo a última música tocada.
     */
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

    /**
     * Define ou atualiza a avaliação de um usuário para uma música.
     *
     * @param musicId O ID da música.
     * @param userId O ID do usuário.
     * @param liked `Boolean.TRUE` para curtir, `Boolean.FALSE` para descurtir, `null` para remover avaliação.
     * @return Uma `Response` indicando o sucesso da operação.
     */
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

    /**
     * Realiza uma busca por músicas, retornando detalhes específicos para um usuário.
     *
     * @param searchTerm O termo de pesquisa.
     * @param userId O ID do usuário (para personalização ou detalhes de avaliação).
     * @return Uma `Response` contendo uma lista de objetos `Music`.
     */
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

    /**
     * Realiza uma busca genérica por músicas, sem detalhes específicos de usuário.
     *
     * @param searchTerm O termo de pesquisa.
     * @return Uma `Response` contendo uma lista de objetos `Music`.
     */
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

    /**
     * Obtém as músicas mais pesquisadas por um usuário específico.
     *
     * @param userId O ID do usuário.
     * @param showAmount O número máximo de músicas a serem retornadas.
     * @return Uma `Response` contendo uma lista das músicas mais pesquisadas.
     */
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

    /**
     * Retorna se uma nova música foi selecionada no player.
     *
     * @return `true` se uma nova música foi selecionada, `false` caso contrário.
     */
    public boolean isNewMusicSelected() {
        return isNewMusicSelected;
    }

    /**
     * Define o estado de seleção de nova música.
     *
     * @param newMusicSelected O `boolean` a ser definido.
     */
    public void setNewMusicSelected(boolean newMusicSelected) {
        isNewMusicSelected = newMusicSelected;
    }

    /**
     * Retorna o ID da última música que foi selecionada para tocar.
     *
     * @return O `int` do ID da música.
     */
    public int getNewMusicSelectedId() {
        return newMusicSelectedId;
    }

    /**
     * Define o ID da última música selecionada para tocar.
     *
     * @param newMusicSelectedId O `int` a ser definido como o ID da música.
     */
    public void setNewMusicSelectedId(int newMusicSelectedId) {
        this.newMusicSelectedId = newMusicSelectedId;
    }

    /**
     * Deleta uma música do sistema pelo seu ID.
     *
     * @param idMusic O ID da música a ser deletada.
     * @return Uma `Response` indicando o sucesso da operação.
     */
    public Response<Void> deleteMusic(int idMusic){
        try{
            if(idMusic == 0){
                return ResponseHelper.generateBadResponse("O parâmetro de id não pode ser nulo ou zero");
            }

            musicRepository.deleteMusic(idMusic);

            return ResponseHelper.generateSuccessResponse("Música Deletada com sucesso!");
        } catch(Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Obtém uma lista de músicas curtidas por um usuário.
     *
     * @param userId O ID do usuário.
     * @param limit O número máximo de músicas a serem retornadas.
     * @return Uma `Response` contendo a lista de músicas curtidas.
     */
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

    /**
     * Obtém uma lista de músicas descurtidas por um usuário.
     *
     * @param userId O ID do usuário.
     * @param limit O número máximo de músicas a serem retornadas.
     * @return Uma `Response` contendo a lista de músicas descurtidas.
     */
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

    /**
     * Obtém o número total de músicas registradas no sistema.
     *
     * @return Uma `Response` contendo o total de músicas.
     */
    public Response<Long> getTotalMusics(){
        try{
            long total = musicRepository.getTotalMusics();
            return ResponseHelper.generateSuccessResponse("Total de músicas obtido com sucesso.", total);
        } catch (Exception e){
            return ResponseHelper.generateErrorResponse(e.getMessage(), e);
        }
    }

    /**
     * Obtém uma lista de todas as músicas cadastradas no sistema.
     *
     * @return Uma `Response` contendo a lista de todas as músicas.
     */
    public Response<List<Music>> getAllMusics() {
        try {

            List<Music> musics = musicRepository.getAllMusics();

            return ResponseHelper.generateSuccessResponse("Músicas obtidas com sucesso!", musics);
        } catch (Exception e) {
            return ResponseHelper.generateErrorResponse(e.getMessage(), e);
        }
    }

    /**
     * Obtém uma lista das músicas mais curtidas no sistema.
     *
     * @return Uma `Response` contendo a lista das músicas mais curtidas.
     */
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

    /**
     * Obtém a fila de reprodução atual de um usuário.
     *
     * @param userId O ID do usuário.
     * @return Uma `Response` contendo a lista de músicas na fila do usuário.
     */
    public Response<List<Music>> getUserQueue(int userId){
        try {
            if(userId == 0){
                return ResponseHelper.generateBadResponse("Ignorado por userId ser 0!");
            }
            List<Music> musics = musicRepository.getUserQueue(userId);

            for (Music music : musics){
                putAuthorsIntoMusic(music);
                putGenreIntoMusic(music);
            }
            return ResponseHelper.generateSuccessResponse("Musicas obtidas com sucesso", musics);
        } catch(Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Obtém uma lista das músicas mais descurtidas no sistema.
     *
     * @return Uma `Response` contendo a lista das músicas mais descurtidas.
     */
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

    /**
     * Obtém todas as músicas de uma playlist.
     *
     * @param playlistId O ID da playlist.
     * @return Uma `Response` contendo a lista de músicas da playlist.
     */
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

    /**
     * Cria um novo gênero musical no sistema.
     *
     * @param name O nome do gênero a ser criado.
     * @return Uma `Response` indicando o sucesso da operação.
     */
    public Response<Void> createGenre(String name){
        try{

            Genre genreToCheck = genreRepository.getGenreByName(name);
            if (genreToCheck != null){
                return ResponseHelper.generateBadResponse("Já existe um gênero com esse nome!");
            }

            genreRepository.createGenre(name);


            return ResponseHelper.generateSuccessResponse("Gênero criado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Deleta a primeira música na fila de músicas do usuário.
     * @param userId O id do usuário.
     * @return A resposta da tentativa de deletar a música.
     */
    public Response<Void> deleteFirstMusicFromUserQueue(int userId){
        try {
            musicRepository.deleteFirstMusicFromUserQueue(userId);

            return ResponseHelper.generateSuccessResponse("Música removida da primeira posição da Queue com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.generateErrorResponse(ex.getMessage(), ex);
        }
    }

    /**
     * Método auxiliar privado para popular a lista de autores em um objeto `Music`.
     * Busca os artistas associados à música no `ArtistRepository`.
     *
     * @param music O objeto `Music` a ser preenchido.
     * @throws Exception Se ocorrer um erro ao obter os artistas ou se não houver artistas.
     */
    private void putAuthorsIntoMusic(Music music) throws Exception{
        List<Artist> artists = artistRepository.getArtistsByMusicId(music.getIdMusica());
        if (artists == null || artists.isEmpty()){
            throw new IllegalStateException("Não há artistas para a música ou os artistas são nulos!");
        }
        music.setAutores(artists);
    }

    /**
     * Método auxiliar privado para popular o gênero em um objeto `Music`.
     * Busca o gênero associado à música no `GenreRepository`.
     *
     * @param music O objeto `Music` a ser preenchido.
     * @throws Exception Se ocorrer um erro ao obter o gênero ou se não houver gênero.
     */
    private void putGenreIntoMusic(Music music) throws Exception{
        Genre genre = genreRepository.getGenreByMusicId(music.getIdMusica());
        if (genre == null){
            throw new IllegalStateException("Não há gênero musical para essa música!");
        }
        music.setGenre(genre);
    }
}
