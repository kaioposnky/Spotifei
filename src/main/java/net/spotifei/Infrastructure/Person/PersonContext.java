package net.spotifei.Infrastructure.Person;

import net.spotifei.Models.Person;

public class PersonContext {

    private static Person personContext;

    public static Person getPersonContext() {
        return personContext;
    }

    public static void setPersonContext(Person personContext) {
        PersonContext.personContext = personContext;
    }
}
