package net.spotifei;

//imports
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import io.github.cdimascio.dotenv.Dotenv;
import net.spotifei.Helpers.AssetsLoader;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Infrastructure.Logger.LoggerRepository;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.io.IOException;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logInfo;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logWarn;

public class Spotifei {
    private static final Dotenv dotenv = Dotenv.configure().load();

    /**
     * O método principal de execução da aplicação Spotifei.
     *
     * @param args Argumentos da linha de comando (não utilizados nesta aplicação).
     */
    public static void main(String[] args) {
        LoggerRepository.setDebugEnabled(false);
        AppContext appContext = new AppContext();
        appContext.getAudioPlayerWorker().execute();

        Runtime.getRuntime().addShutdownHook(new Thread(Spotifei::onApplicationClose));

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

    /**
     * Fornece acesso à instância de Dotenv para carregar variáveis de ambiente.
     *
     * @return A instância de Dotenv configurada.
     */
    public static Dotenv getDotEnv(){
        return dotenv;
    }

    /**
     * Método estático que é executado quando a aplicação está prestes a ser encerrada.
     */
    private static void onApplicationClose(){
        try{
            AssetsLoader.clearQueriesFiles();
            logInfo("Arquivos de query temporários limpos com sucesso!");
        } catch (IOException e){
            logWarn("Erro ao limpar arquivos de query temporários: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
