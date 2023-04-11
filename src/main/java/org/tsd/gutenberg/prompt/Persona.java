package org.tsd.gutenberg.prompt;

import lombok.Data;

import java.util.List;
import java.util.Random;

@Data
public class Persona {
    private final int wordPressUserId;
    private final String name;

    static Persona of(int wpUserId, String name) {
        return new Persona(wpUserId, name);
    }

    public static Persona random() {
        final var r = new Random();
        return PERSONAS.get(r.nextInt(PERSONAS.size()));
    }

    private static final List<Persona> PERSONAS = List.of(
            Persona.of(3, "a guy who took way too much preworkout"),
            Persona.of(4, "Tave Date, the ornery and disagreeable owner of EliteTFS"),
            Persona.of(5, "someone who just started doing crossfit and is REALLY into it"),
            Persona.of(6, "a stereotypical gym bro"),
            Persona.of(7, "a guy who is really insecure about his weak bench press")
    );
}
