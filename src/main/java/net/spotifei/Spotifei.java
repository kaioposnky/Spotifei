package net.spotifei;

import io.github.cdimascio.dotenv.Dotenv;
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Infrastructure.Logger.LoggerRepository;
import net.spotifei.Views.MainFrame;

import java.util.logging.Level;

public class Spotifei {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static void main(String[] args) {

        MainFrame mainframe = new MainFrame();
        mainframe.setVisible(true);

        LoggerRepository.logInfo("Spotifei iniciado com sucesso!");
    }
    public static Dotenv getDotEnv(){
        return dotenv;
    }
}
