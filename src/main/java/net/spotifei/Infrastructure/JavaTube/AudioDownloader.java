package net.spotifei.Infrastructure.JavaTube;

import com.github.felipeucelli.javatube.Youtube;
import net.spotifei.Exceptions.VideoDownloadException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AudioDownloader {

    private final String tmpPath = System.getProperty("java.io.tmpdir") + "/spotifei/";

    /**
     * Baixa um vídeo do Youtube como aúdio e armazena num lugar na temp com nome aleatório
     * @param youtubeVideoLink Vídeo do Youtube a ser baixado no formato: <a href="https://www.youtube.com/watch?v=S2PVsv2K1Bg">...</a>
     * @return Path do arquivo baixado com .mp4 no final, exemplo: musicalegal.mp4
     * @throws IOException Se ocorrer um erro ao criar o diretório temporário
     * @throws VideoDownloadException Se ocorrer um erro ao baixar o vídeo
     */
    public String downloadYoutubeAudioWithLink(String youtubeVideoLink) throws IOException, VideoDownloadException {
        createTmpDir(); // Cria o diretório na temp do sistema para guardar os arquivos

        String randNum = String.valueOf((int) (Math.random() * 10000000));

        return downloadVideoAudio(youtubeVideoLink, tmpPath, randNum);
    }

    /**
     * Baixa o aúdio de um vídeo com .mp4 no final
     * @param videoLink Link do vídeo no Youtube no formato <a href="https://www.youtube.com/watch?v=S2PVsv2K1Bg">...</a>
     * @param fileDir Diretório em que o arquivo vai ficar, exemplo: /home/tmp/
     * @param fileName Nome do arquivo resultante, exemplo: arquivolegal (Será baixado como arquivolegal.mp4)
     * @throws VideoDownloadException Caso o vídeo tenha problemas para ser baixado
     */
    private String downloadVideoAudio(String videoLink, String fileDir, String fileName) throws VideoDownloadException{
        try{
            Youtube yt = new Youtube(videoLink);
            yt.streams().getOnlyAudio().download(fileDir, fileName); // Baixa o arquivo e renomeia com .mp4 no final
        } catch (Exception e) {
            throw new VideoDownloadException("Could not download the video from Youtube! Did you put it in the correct " +
                    "format? Example: https://www.youtube.com/watch?v=S2PVsv2K1Bg");
        }

        return fileDir + fileName + ".mp4";
    }

    private void createTmpDir() throws IOException {
        Path tmpDir = Paths.get(tmpPath);

        if(!Files.exists(tmpDir)){
            if (!new File(tmpPath).mkdirs()){
                throw new IOException("Could not create temporary folder for music files!");
            }
        }
    }
}
