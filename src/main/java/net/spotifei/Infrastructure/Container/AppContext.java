package net.spotifei.Infrastructure.Container;

import net.spotifei.Models.Music;
import net.spotifei.Models.Person;

public class AppContext {

    private Person personContext;
    private Music musicContext;

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
