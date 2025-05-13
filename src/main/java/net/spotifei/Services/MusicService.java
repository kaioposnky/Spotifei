package net.spotifei.Services;

import net.spotifei.Helpers.ResponseHelper;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Repository.ArtistRepository;
import net.spotifei.Infrastructure.Repository.GenreRepository;
import net.spotifei.Infrastructure.Repository.MusicRepository;
import net.spotifei.Models.Artist;
import net.spotifei.Models.Genre;
import net.spotifei.Models.Music;
import net.spotifei.Models.Responses.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static net.spotifei.Infrastructure.AudioPlayer.OpusConverter.convertMP3FileToOpusBytes;
import static net.spotifei.Infrastructure.AudioPlayer.OpusConverter.getMP3DurationInMicrosseconds;

public class MusicService {

    private final MusicRepository musicRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final AudioPlayerWorker audioPlayerWorker;

    public MusicService(MusicRepository musicRepository, AudioPlayerWorker audioPlayerWorker, ArtistRepository artistRepository, GenreRepository genreRepository){
        this.musicRepository = musicRepository;
        this.audioPlayerWorker = audioPlayerWorker;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
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

    public Response<Music> getMusicById(int musicId){
        try{
            Music music = musicRepository.getMusicById(musicId);

            if (music == null){
                return ResponseHelper.GenerateBadResponse("A música retornada foi nula!");
            }

            List<Artist> artists = artistRepository.getArtistsByMusicId(musicId);
            if (artists == null || artists.isEmpty()){
                return ResponseHelper.GenerateBadResponse("Os artistas da música retornados foram nulos!");
            }
            music.setAutores(artists);

            return ResponseHelper.GenerateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception e){
            return ResponseHelper.GenerateErrorResponse("Erro ao obter música!", e);
        }
    }

    public Response<Void> playMusic(int musicId){
        try{

            Music music = musicRepository.getMusicById(musicId);
            List<Artist> artists = artistRepository.getArtistsByMusicId(musicId);
            byte[] musicAudio = musicRepository.getMusicAsByteArray(musicId);

            if(musicAudio == null){
                return ResponseHelper.GenerateBadResponse("O aúdio retornado foi nulo! Operação cancelada!");
            }

            music.setAutores(artists);

            audioPlayerWorker.playMusic(musicAudio);

            audioPlayerWorker.pause();

            audioPlayerWorker.selectMusic(music); // atualiza o UI

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

    public Response<Void> pauseMusic(){
        try{
            audioPlayerWorker.pause();

            return ResponseHelper.GenerateSuccessResponse("Música pausada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse("Erro ao tentar pausar a música!", ex);
        }
    }

    public Response<Void> addMusicToUserHistory(int userId, int musicId){
        try{
            if(userId == 0 || musicId == 0){
                return ResponseHelper.GenerateBadResponse("Os parâmetros de id não podem ser nulos ou zero");
            }

            musicRepository.insertMusicPlayHistory(userId, musicId);

            return ResponseHelper.GenerateSuccessResponse("Música adicionada ao histórico com sucesso");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }

    }

    public Response<Void> registerMusic(String musicFilePath, String musicName, String musicArtist, String musicGenre){
        try{
            String[] infos = {musicFilePath, musicName, musicArtist, musicGenre};
            if (Arrays.stream(infos).anyMatch(info -> info == null || info.isEmpty())){
                return ResponseHelper.GenerateBadResponse("Nenhum dos argumentos pode ser nulo ou vazio!");
            }

            File musicFile = new File(musicFilePath);
            if (!musicFile.exists()){
                throw new FileNotFoundException("O arquivo de música não existe!");
            }
            byte[] fileBytes = convertMP3FileToOpusBytes(musicFile);
            if (fileBytes == null){
                return ResponseHelper.GenerateBadResponse("Não foi possível obter os bytes do arquivo!");
            }
            Genre genre = genreRepository.getGenreByName(musicGenre);
            if (genre == null){
                return ResponseHelper.GenerateBadResponse("Gênero não encontrado! (" + musicGenre + ") ");
            }

            Artist artist = artistRepository.getArtistByName(musicArtist);
            if (artist == null){
                return ResponseHelper.GenerateBadResponse("Artista não encontrado, use o nome artístico! (" + musicArtist + ") ");
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

            return ResponseHelper.GenerateSuccessResponse("Música cadastrada com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Music> getUserLastPlayedMusic(int userId){
        try{
            if(userId == 0){
                return ResponseHelper.GenerateBadResponse("O parâmetro de id não pode ser nulo ou zero");
            }

            Music music = musicRepository.getUserLastPlayedMusic(userId);

            // se retornar nulo é pq nn tem nenhuma música registrada
            if(music == null){
                music = new Music();
                music.setDuracaoMs(100000000);
                music.setNome("Selecione uma música para tocar!");
                Artist artist = new Artist();
                artist.setNome("Selecione uma música para tocar!");
                music.setAutores(List.of(artist));
            }

            return ResponseHelper.GenerateSuccessResponse("Música obtida com sucesso!", music);
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<Void> setOrInsertMusicUserRating(int musicId, int userId, Boolean liked){
        try{
            if (userId <= 0 || musicId <= 0){
                return ResponseHelper.GenerateBadResponse("Os ids não podem ser nulos ou zero!");
            }

            // se for nulo o usuario tirou a avaliacao
            if(liked == null){
                musicRepository.deleteMusicUserRating(musicId, userId);
            } else{
                musicRepository.setOrInsertMusicUserRating(musicId, userId, liked);
            }

            return ResponseHelper.GenerateSuccessResponse("Feedback da música atualizado com sucesso!");
        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }

    public Response<List<Music>> searchMusics(String searchTerm, int userId){
        try{
            if(searchTerm.isBlank()){
                return ResponseHelper.GenerateBadResponse("O termo de pesquisa não pode estar vazio!");
            }
            if(userId == 0){
                return ResponseHelper.GenerateBadResponse("O parâmetro de id não pode ser nulo ou zero");
            }

            List<Music> musics = musicRepository.searchMusicForUserWithDetails(searchTerm, userId);
            if(musics == null || musics.isEmpty()){
                return ResponseHelper.GenerateBadResponse("Nenhuma música foi encontrada com o termo de pesquisa: " + searchTerm);
            }

            return ResponseHelper.GenerateSuccessResponse("Músicas encontradas com sucesso!", musics);

        } catch (Exception ex){
            return ResponseHelper.GenerateErrorResponse(ex.getMessage(), ex);
        }
    }
}
