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
                    List.of("excited", "amped", "jittery", "aggressive")),

            Persona.of(4, "Tave Date, owner of EliteTFS",
                    List.of("ornery", "disagreeable", "combative", "no-nonsense")),

            Persona.of(5, "someone who just started doing crossfit",
                    List.of("enthusiastic about crossfit", "obsessed with crossfit")),

            Persona.of(6, "a stereotypical gym bro",
                    List.of("vain", "dim-witted", "sleazy")),

            Persona.of(7, "a guy who is really insecure about his weak bench press",
                    List.of("very insecure about his weak bench press",
                            "believes bench press is unimportant",
                            "believes deadlifts are the true test of strength"))
    );
}
