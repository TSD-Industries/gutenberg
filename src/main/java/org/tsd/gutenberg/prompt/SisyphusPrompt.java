package org.tsd.gutenberg.prompt;

public class SisyphusPrompt extends Prompt {
    @Override
    public BlogPostOptions options() throws Exception {
        return BlogPostOptions.builder()
                ..build()
    }
}
