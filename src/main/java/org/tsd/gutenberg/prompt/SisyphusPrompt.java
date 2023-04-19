package org.tsd.gutenberg.prompt;

public class SisyphusPrompt extends Prompt {
    public SisyphusPrompt() {
        super(PostCategory.SISYPHUS);
    }

    @Override
    public BlogPostOptions options() throws Exception {
        return null;
    }
}
