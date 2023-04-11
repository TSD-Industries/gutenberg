package org.tsd.gutenberg.prompt;

import java.io.IOException;

public interface Prompt {
    String generate(PromptOptions promptOptions) throws IOException;
}
