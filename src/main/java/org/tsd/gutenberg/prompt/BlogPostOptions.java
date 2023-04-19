package org.tsd.gutenberg.prompt;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class BlogPostOptions {
    private final Long authorId;
    private final PostCategory postCategory;
    private final String settingsMessage;
    private final String instructionMessage;
    private final File media;
}
