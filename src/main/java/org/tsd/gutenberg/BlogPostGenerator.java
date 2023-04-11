package org.tsd.gutenberg;

import org.apache.commons.lang3.RandomUtils;
import org.tsd.gutenberg.prompt.BlogPostOptions;
import org.tsd.gutenberg.prompt.BookReviewPrompt;
import org.tsd.gutenberg.prompt.Prompt;

import java.util.LinkedList;
import java.util.List;

public class BlogPostGenerator {
    private static final List<Prompt> prompts = new LinkedList<>();

    static {
        prompts.add(new BookReviewPrompt());
    }

    public BlogPostOptions generateRandomBlogPost() throws Exception {
        return prompts.get(RandomUtils.nextInt(0, prompts.size())).options();
    }
}
