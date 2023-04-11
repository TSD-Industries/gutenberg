package org.tsd.gutenberg.prompt;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class PromptOptions {
    private final Persona persona;

    public Optional<Long> getAuthorId() {
        return persona == null ? Optional.empty() : Optional.of(persona.getWordPressUserId());
    }
}
