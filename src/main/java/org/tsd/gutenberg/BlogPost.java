package org.tsd.gutenberg;

import lombok.Builder;
import lombok.Data;
import org.tsd.gutenberg.prompt.PostCategory;

import java.io.File;

@Data
@Builder
public class BlogPost {
    private final Long author;
    private final String title;
    private final String excerpt;
    private final String body;
    private final PostCategory category;
    private final File imageFile;
}
