package org.tsd.gutenberg.prompt;

public abstract class Prompt {
    private final PostCategory postCategory;

    public Prompt(PostCategory postCategory) {
        this.postCategory = postCategory;
    }

    public abstract BlogPostGenerationSettings options() throws Exception;
}
