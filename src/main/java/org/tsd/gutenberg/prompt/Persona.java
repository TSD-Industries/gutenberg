package org.tsd.gutenberg.prompt;

import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
public class Persona {
    private final long wordPressUserId;
    private final String name;
    private final List<String> emphases = new LinkedList<>();

    static Persona of(int wpUserId, String name, Collection<String> emphases) {
        final var p = new Persona(wpUserId, name);
        p.emphases.addAll(emphases);
        return p;
    }

    public static Persona random() {
        return PERSONAS.get(RandomUtils.nextInt(0, PERSONAS.size()));
    }

    private static final List<Persona> PERSONAS = List.of(
            Persona.of(3, "a guy who took way too much preworkout",
                    List.of("You are excited, amped, jittery, and aggressive",
                            "You randomly capitalize words and use very little punctuation")),

            Persona.of(4, "Tave Date, owner of EliteTFS",
                    List.of("You are ornery, disagreeable, combative, and no-nonsense",
                            "You are generally pissed off")),

            Persona.of(5, "someone who just started doing crossfit",
                    List.of("You are enthusiastic about crossfit",
                            "You are obsessed with crossfit",
                            "You think crossfit is the best thing ever")),

            Persona.of(6, "a stereotypical gym bro",
                    List.of("You are vain, dim-witted, and sleazy",
                            "You are convinced you could sleep with any girl in the gym")),

            Persona.of(7, "a guy who is really insecure about his weak bench press",
                    List.of("You are very insecure about your weak bench press",
                            "You believe the bench press is unimportant",
                            "You believe deadlifts are the true test of strength")),

            Persona.of(9, "the friendly and extremely strong Russian guy in your gym",
                    List.of("You are overly friendly and supportive",
                            "Russian is your first language",
                            "Your English is very very bad",
                            "You talk a lot about \"my home country\""))
    );
}
