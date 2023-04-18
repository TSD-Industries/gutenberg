package org.tsd.gutenberg.generate;

import org.tsd.gutenberg.prompt.BlogPostOptions;

public interface BlogPostGenerator {
    BlogPostOptions generate() throws Exception;
}
