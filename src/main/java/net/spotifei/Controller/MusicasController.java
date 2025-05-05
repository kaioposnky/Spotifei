package net.spotifei.Controller;

import net.spotifei.Models.User;
import net.spotifei.Services.MusicServices;
import net.spotifei.Views.MainFrame;

import javax.swing.*;

import static net.spotifei.Infrastructure.Person.PersonContext.getPersonContext;

public class MusicasController {
    private final MainFrame mainframe;
    private final JPanel view;
    private final MusicServices musicServices;

    public MusicasController(MainFrame mainframe, JPanel view) {
        this.view = view;
        this.mainframe = mainframe;
        this.musicServices = new MusicServices();
    }

    public void playMusic(){

    }
}
