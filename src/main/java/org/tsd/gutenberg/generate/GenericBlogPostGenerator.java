package org.tsd.gutenberg.generate;

import org.apache.commons.lang3.RandomUtils;
import org.tsd.gutenberg.GptService;
import org.tsd.gutenberg.WordpressService;
import org.tsd.gutenberg.prompt.BlogPostGenerationSettings;
import org.tsd.gutenberg.prompt.BookReviewPrompt;
import org.tsd.gutenberg.prompt.Prompt;

import java.util.LinkedList;
import java.util.List;

public class GenericBlogPostGenerator implements BlogPostGenerator {
    private final List<Prompt> prompts = new LinkedList<>();

    public GenericBlogPostGenerator(GptService gptService, WordpressService wordpressService) {
        prompts.add(new BookReviewPrompt(gptService, wordpressService));
    }

    @Override
    public BlogPostGenerationSettings generate() throws Exception {
        return prompts.get(RandomUtils.nextInt(0, prompts.size())).options();
    }
}
