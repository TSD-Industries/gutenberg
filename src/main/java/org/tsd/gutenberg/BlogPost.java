package org.tsd.gutenberg;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.tsd.gutenberg.prompt.PostCategory;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class BlogPost {
    @ToString.Include
    private final Long author;

    @ToString.Include
    private final String title;

    @ToString.Include
    private final String excerpt;

    @ToString.Include
    private final String body;

    @ToString.Include
    private final PostCategory category;

    @ToString.Exclude
    private final byte[] mediaBytes;
}
