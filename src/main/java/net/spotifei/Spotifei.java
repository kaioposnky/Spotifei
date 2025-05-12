package net.spotifei;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
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
            try {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
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
