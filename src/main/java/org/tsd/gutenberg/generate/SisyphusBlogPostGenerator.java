package org.tsd.gutenberg.generate;

import org.tsd.gutenberg.prompt.BlogPostGenerationSettings;
import org.tsd.gutenberg.prompt.SisyphusPrompt;

public class SisyphusBlogPostGenerator implements BlogPostGenerator {
    @Override
    public BlogPostGenerationSettings generate() throws Exception {
        final var prompt = new SisyphusPrompt();
        return prompt.options();
    }
}
