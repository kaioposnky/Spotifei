package net.spotifei;

import io.github.cdimascio.dotenv.Dotenv;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Logger.LoggerRepository;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logInfo;

public class Spotifei {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static void main(String[] args) throws Exception {
        LoggerRepository.setDebugEnabled(true);
        AppContext appContext = new AppContext();
        appContext.getAudioPlayerWorker().execute();

        SwingUtilities.invokeLater(() -> {
            MainFrame mainframe = new MainFrame(appContext);
            mainframe.setVisible(true);
        });

        logInfo("Spotifei iniciado com sucesso!");
        LoggerRepository.logDebug("DEBUG habilitado!");
    }
    public static Dotenv getDotEnv(){
        return dotenv;
    }
}
