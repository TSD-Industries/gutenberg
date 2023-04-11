package org.tsd.gutenberg.prompt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromptOptions {
    private final Persona persona;
}
