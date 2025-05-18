package net.spotifei.Helpers;

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

    public static ImageIcon loadImageIcon(String iconFilename, int width, int height){
        String iconPath = "assets/icons/" + iconFilename;
        URL url = AssetsLoader.class.getClassLoader().getResource(iconPath);
        ImageIcon icon = new ImageIcon(url);
        Image image = icon.getImage();
        image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);
        return icon;
    }

    public static ImageIcon loadImageIcon(String iconFilename){
        String iconPath = "assets/icons/" + iconFilename;

        URL url = AssetsLoader.class.getClassLoader().getResource(iconPath);
        return new ImageIcon(url);
    }
}
