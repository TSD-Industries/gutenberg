package org.tsd.gutenberg.prompt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogPostOptions {
    private final Long authorId;
    private final PostCategory postCategory;
    private final String settingsMessage;
    private final String instructionMessage;
}
