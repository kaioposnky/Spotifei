package net.spotifei.Helpers;

import java.io.File;
import java.io.FileNotFoundException;

public class AssetsLoader {
    private static final String assetsPath =
            String.valueOf(AssetsLoader.class.getClassLoader().getResource("assets"))
                    .replace("file:/", "");

    public static File getLangFile() throws FileNotFoundException {
        String langFilePath = assetsPath + File.separator + "lang" + File.separator + MessagesHelper.getLanguage() + ".json";
        try{
            return new File(langFilePath);
        } catch (NullPointerException e){
            throw new FileNotFoundException("Arquivo de linguagens para a lingua não encontrado ou inválido!"
                + "Caminho: " + langFilePath);
        }
    }

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
}
