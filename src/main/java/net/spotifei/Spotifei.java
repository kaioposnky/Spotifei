package net.spotifei;

import io.github.cdimascio.dotenv.Dotenv;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Logger.LoggerRepository;
import net.spotifei.Views.MainFrame;

import static net.spotifei.Infrastructure.Cryptograph.CriptographRepository.generateHash;

public class Spotifei {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static void main(String[] args) {
        AppContext appContext = new AppContext();
        AudioPlayerWorker audioPlayerWorker = new AudioPlayerWorker();

        MainFrame mainframe = new MainFrame(appContext, audioPlayerWorker);
        mainframe.setVisible(true);

        LoggerRepository.logInfo("Spotifei iniciado com sucesso!");
    }
    public static Dotenv getDotEnv(){
        return dotenv;
    }
}
