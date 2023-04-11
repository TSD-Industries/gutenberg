package org.tsd.gutenberg;

import org.tsd.gutenberg.prompt.BookReviewPrompt;
import org.tsd.gutenberg.prompt.Prompt;
import org.tsd.gutenberg.prompt.PromptOptions;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PromptGenerator {
    private static final List<Prompt> prompts = new LinkedList<>();

    static {
        prompts.add(new BookReviewPrompt());
    }

    public String generate(PromptOptions promptOptions) throws IOException {
        return prompts.get(0).generate(promptOptions);
    }
}
