package net.spotifei.Helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class MessagesHelper {
    private static String language = "pt-br"; // lingua padrão

    public String getLanguageMessage(String key) throws FileNotFoundException {
        File langFile = AssetsLoader.getLangFile();
        Reader reader = new FileReader(langFile);
        JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
        return jsonObject.get(key).getAsString();
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        MessagesHelper.language = language;
    }
}
