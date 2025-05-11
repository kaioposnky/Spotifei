package net.spotifei.Helpers;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

public class AssetsLoader {
    private static final String assetsPath =
            String.valueOf(AssetsLoader.class.getClassLoader().getResource("assets"))
                    .replace("file:/", "");

    public static File[] getQueriesFiles() throws FileNotFoundException, RuntimeException {
        String queriesPath = assetsPath + "/" + "queries";
        try {
            File[] queriesDir = new File(queriesPath).listFiles();
            return queriesDir;
        } catch (NullPointerException e) {
            throw new FileNotFoundException("Pasta com arquivos de query não encontrada!\n"
                    + "Caminho: " + queriesPath);
        }
    }

    public static ImageIcon loadImageIcon(String iconFilename){
        String iconPath = assetsPath + "/" + "icons" + "/" + iconFilename;
        return new ImageIcon(iconPath);
    }
}
