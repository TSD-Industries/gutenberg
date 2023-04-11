package org.tsd.gutenberg;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogPost {
    private final Long author;
    private final String title;
    private final String excerpt;
    private final String body;
    private final String category;
}
