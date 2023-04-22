package org.tsd.gutenberg.generate;

import org.tsd.gutenberg.prompt.BlogPostGenerationSettings;

public interface BlogPostGenerator {
    BlogPostGenerationSettings generate() throws Exception;
}
