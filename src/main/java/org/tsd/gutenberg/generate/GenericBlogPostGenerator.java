package org.tsd.gutenberg.generate;

import org.apache.commons.lang3.RandomUtils;
import org.tsd.gutenberg.prompt.BlogPostOptions;
import org.tsd.gutenberg.prompt.BookReviewPrompt;
import org.tsd.gutenberg.prompt.Prompt;

import java.util.LinkedList;
import java.util.List;

public class GenericBlogPostGenerator implements BlogPostGenerator {
    private static final List<Prompt> PROMPTS = new LinkedList<>();

    static {
        PROMPTS.add(new BookReviewPrompt());
    }

    @Override
    public BlogPostOptions generate() throws Exception {
        return PROMPTS.get(RandomUtils.nextInt(0, PROMPTS.size())).options();
    }
}
