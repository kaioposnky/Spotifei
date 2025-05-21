package net.spotifei.Helpers;

// imports
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logWarn;

public class AssetsLoader {

    private final static String[] QUERIES_FILES = {
            "Administrator.xml",
            "Artist.xml",
            "Genre.xml",
            "Music.xml",
            "MusicQueue.xml",
            "Person.xml",
            "Playlist.xml"
    };

    /**
     * Obtém os arquivos de query XML da pasta de assets.
     *
     * @return Um array de objetos File representando os arquivos de query.
     * @throws FileNotFoundException Se a pasta de queries não for encontrada.
     * @throws RuntimeException Se ocorrer um erro de I/O inesperado durante a cópia dos arquivos.
     */
    public static File[] getQueriesFiles() throws FileNotFoundException, RuntimeException {
        String queriesPath = "assets/queries";

        File[] queryFiles = null;
        try {
            Path tempPath = Files.createTempDirectory("temp");

            for (String queryFilename : QUERIES_FILES){
                try (InputStream inputStream = AssetsLoader.class.getClassLoader().
                        getResourceAsStream(queriesPath + "/" + queryFilename)) {
                    // obtem o inputstream do arquivo (bytes)
                    // cria um arquivo em um diretório temporário para cada query
                    Files.copy(inputStream, tempPath.resolve(queryFilename));
                }
            }

            queryFiles = tempPath.toFile().listFiles();
            // só funciona se rodado pela IDE, não roda em .jar
//            File[] queriesDir = new File(queriesPath).listFiles();
//            return queriesDir;
            return queryFiles;
        } catch (NullPointerException e) {
            throw new FileNotFoundException("Pasta com arquivos de query não encontrada!\n"
                    + "Caminho: " + queriesPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Limpa os arquivos de query temporários.
     * Esta função assume que os arquivos foram criados em um diretório específico
     * ou que podem ser acessados diretamente.
     *
     * @throws IOException Se ocorrer um erro durante a exclusão dos arquivos.
     */
    public static void clearQueriesFiles() throws IOException {
        String queriesPath = "assets/queries";
        File[] queryFiles = new File(queriesPath).listFiles();
        if (queryFiles == null) {
            logWarn("Os arquivos de query temporários não foram encontrados para serem removidos!");
            return;
        }
        for (File queryFile : queryFiles){
            queryFile.delete();
        }
    }

    /**
     * Carrega um ícone de imagem a partir dos assets e o redimensiona para as dimensões especificadas.
     *
     * @param iconFilename O nome do arquivo do ícone (ex: "play.png").
     * @param width A largura desejada para o ícone.
     * @param height A altura desejada para o ícone.
     * @return Um objeto ImageIcon redimensionado.
     */
    public static ImageIcon loadImageIcon(String iconFilename, int width, int height){
        String iconPath = "assets/icons/" + iconFilename;
        URL url = AssetsLoader.class.getClassLoader().getResource(iconPath);
        ImageIcon icon = new ImageIcon(url);
        Image image = icon.getImage();
        image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);
        return icon;
    }

    /**
     * Carrega um ícone de imagem a partir dos assets sem redimensionamento.
     *
     * @param iconFilename O nome do arquivo do ícone (ex: "home.png").
     * @return Um objeto ImageIcon original.
     */
    public static ImageIcon loadImageIcon(String iconFilename){
        String iconPath = "assets/icons/" + iconFilename;

        URL url = AssetsLoader.class.getClassLoader().getResource(iconPath);
        return new ImageIcon(url);
    }
}
