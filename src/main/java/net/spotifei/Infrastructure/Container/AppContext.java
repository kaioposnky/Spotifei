package net.spotifei.Infrastructure.Container;

import net.spotifei.Controller.*;
import net.spotifei.Infrastructure.AudioPlayer.AudioControls;
import net.spotifei.Infrastructure.AudioPlayer.AudioPlayerWorker;
import net.spotifei.Infrastructure.Cryptograph.CriptographRepository;
import net.spotifei.Infrastructure.Factories.MusicInfoComponent.MusicInfoPanelBuilder;
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import net.spotifei.Infrastructure.Repository.*;
import net.spotifei.Models.Music;
import net.spotifei.Models.Person;
import net.spotifei.Services.AuthService;
import net.spotifei.Services.MusicService;
import net.spotifei.Services.PlaylistService;
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
    private final AdministratorRepository administratorRepository;
    private final GenreRepository genreRepository;

    private final MusicService musicService;
    private final AuthService authService;
    private final UserService userService;
    private final PlaylistService playlistService;

    private Person personContext;
    private Music musicContext;

    public AppContext() {
        this.audioPlayerWorker = new AudioPlayerWorker();
        this.jdbcRepository = new JDBCRepository();
        this.criptographRepository = new CriptographRepository();

        this.personRepository = new PersonRepository(this.jdbcRepository);
        this.musicRepository = new MusicRepository(this.jdbcRepository);
        this.artistRepository = new ArtistRepository(this.jdbcRepository);
        this.playlistRepository = new PlaylistRepository(this.jdbcRepository);
        this.administratorRepository = new AdministratorRepository(this.jdbcRepository);
        this.genreRepository = new GenreRepository(this.jdbcRepository);

        this.authService = new AuthService(this.personRepository, this.criptographRepository);
        this.playlistService = new PlaylistService(this.playlistRepository, this.musicRepository);
        this.userService = new UserService(this.personRepository, this.administratorRepository, this.artistRepository, this.authService);
        this.musicService = new MusicService(this.musicRepository, this.audioPlayerWorker, this.artistRepository, this.playlistRepository, this.genreRepository);

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
        return new UserController(view, mainframe, this);
    }

    public AdminController getAdminController(JPanel view){
        return new AdminController(view, this.musicService, this.userService, this);
    }

    public PlaylistController getPlayListController(JPanel view, MainFrame mainframe){
        return new PlaylistController(view, this.playlistService, this, this.musicService, mainframe);
    }

    public PlaylistController getPlayListController(JDialog viewDialog, MainFrame mainframe){
        return new PlaylistController(viewDialog, this.playlistService, this, this.musicService, mainframe);
    }

    public HistoryController getHistoryController(JPanel view){
        return new HistoryController(this, this.musicService, view);
    }

    public GenreController getGenreController(JPanel view, MainFrame mainframe){
        return new GenreController(view, mainframe, this.musicService);
    }

    public MusicInfoPanelBuilder getMusicInfoPanelBuilder(MainFrame mainFrame) {
        return new MusicInfoPanelBuilder(this, mainFrame);
    }

    public ArtistController getArtistController(JPanel view, MainFrame mainframe){
        return new ArtistController(view, this.musicService, this.userService, this);
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
