package net.spotifei.Infrastructure.Container;

import net.spotifei.Controller.AuthController;
import net.spotifei.Controller.MusicController;
import net.spotifei.Controller.UserController;
import net.spotifei.Infrastructure.AudioPlayer.AudioControls;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Cryptograph.CriptographRepository;
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Infrastructure.Repository.ArtistRepository;
import net.spotifei.Infrastructure.Repository.MusicRepository;
import net.spotifei.Infrastructure.Repository.PersonRepository;
import net.spotifei.Infrastructure.Repository.PlaylistRepository;
import net.spotifei.Models.Music;
import net.spotifei.Models.Person;
import net.spotifei.Services.AuthService;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.UserService;
import net.spotifei.Views.MainFrame;
import net.spotifei.Views.Panels.MusicPlayerPanel;

import javax.swing.*;

public class AppContext {


    private final JDBCRepository jdbcRepository;
    private final CriptographRepository criptographRepository;
    private final AudioPlayerWorker audioPlayerWorker;

    private final ArtistRepository artistRepository;
    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicRepository;
    private final PersonRepository personRepository;

    private final MusicService musicService;
    private final AuthService authService;
    private final UserService userService;

    private Person personContext;
    private Music musicContext;

    public AppContext() {
        this.audioPlayerWorker = new AudioPlayerWorker();
        this.jdbcRepository = JDBCRepository.getInstance();
        this.criptographRepository = new CriptographRepository();

        this.personRepository = new PersonRepository(this.jdbcRepository);
        this.musicRepository = new MusicRepository(this.jdbcRepository);
        this.artistRepository = new ArtistRepository(this.jdbcRepository);
        this.playlistRepository = new PlaylistRepository(this.jdbcRepository);

        this.authService = new AuthService(this.personRepository, this.criptographRepository);
        this.userService = new UserService(this.personRepository);
        this.musicService = new MusicService(this.musicRepository, this.audioPlayerWorker);
    }

    /**
     * Registra o panel como um listener para sincronizar as ações do worker
     * no painel de tocador de música
     * @param panel
     */
    public void registerMusicPlayerPanelListener(MusicPlayerPanel panel) {
        this.audioPlayerWorker.addListener(panel);
    }

    public JDBCRepository getJdbcRepository() {
        return jdbcRepository;
    }

    public CriptographRepository getCriptographRepository() {
        return criptographRepository;
    }

    public AudioControls getAudioControls() {
        return audioPlayerWorker;
    }

    public AudioPlayerWorker getAudioPlayerWorker() {
        return audioPlayerWorker;
    }

    public ArtistRepository getArtistRepository() {
        return artistRepository;
    }

    public PlaylistRepository getPlaylistRepository() {
        return playlistRepository;
    }

    public MusicRepository getMusicRepository() {
        return musicRepository;
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    public MusicController getMusicController(JPanel view, MainFrame mainframe) {
        return new MusicController(view, mainframe, this.musicService, this);
    }

    public AuthController getAuthController(JPanel view, MainFrame mainframe) {
        return new AuthController(view, mainframe, this.authService, this.getUserController(view, mainframe));
    }

    public UserController getUserController(JPanel view, MainFrame mainframe) {
        return new UserController(view, mainframe, this.userService, this);
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public UserService getUserService() {
        return userService;
    }

    public Person getPersonContext() {
        return personContext;
    }

    public void setPersonContext(Person personContext) {
        this.personContext = personContext;
    }


    public Music getMusicContext() {
        return musicContext;
    }

    public void setMusicContext(Music musicContext) {
        this.musicContext = musicContext;
    }
}
