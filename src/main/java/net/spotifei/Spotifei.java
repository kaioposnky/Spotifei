package net.spotifei;

import io.github.cdimascio.dotenv.Dotenv;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Infrastructure.Logger.LoggerRepository;
import net.spotifei.Infrastructure.Repository.MusicRepository;
import net.spotifei.Views.MainFrame;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Map;

import static net.spotifei.Infrastructure.Cryptograph.CriptographRepository.generateHash;

public class Spotifei {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static void main(String[] args) throws Exception {
        LoggerRepository.setDebugEnabled(false);
        AppContext appContext = new AppContext();
        AudioPlayerWorker audioPlayerWorker = new AudioPlayerWorker();
        audioPlayerWorker.execute();

        SwingUtilities.invokeLater(() -> {
            MainFrame mainframe = new MainFrame(appContext, audioPlayerWorker);
            mainframe.setVisible(true);
        });

        JDBCRepository jdbcRepository = JDBCRepository.getInstance();

        MusicRepository musicRepository = new MusicRepository();
        byte[] musicAudio = musicRepository.getMusicAsByteArray(1);

        File kingslayerAudio = new File("musics/Kingslayer.wav");
//
        byte[] musicaudio2 = Files.readAllBytes(kingslayerAudio.toPath());
//
        if (musicAudio == musicaudio2) {
            System.out.println("au");
        }
////        System.out.println("Tamanho do arquivo: " + musicAudio.length + " bytes.");

        audioPlayerWorker.playMusic(musicaudio2);

        musicAudio = null;


        LoggerRepository.logInfo("Spotifei iniciado com sucesso!");
        LoggerRepository.logDebug("DEBUG habilitado!");
    }
    public static Dotenv getDotEnv(){
        return dotenv;
    }
}
