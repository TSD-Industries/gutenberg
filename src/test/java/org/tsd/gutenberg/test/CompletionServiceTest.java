package org.tsd.gutenberg.test;

import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import org.junit.jupiter.api.Test;
import org.tsd.gutenberg.BlogPostGenerator;
import org.tsd.gutenberg.GptService;
import org.tsd.gutenberg.prompt.BookReviewPrompt;
import org.tsd.gutenberg.prompt.Persona;

public class CompletionServiceTest {
    private GptService completionService;
    private BlogPostGenerator promptGenerator = new BlogPostGenerator();

    @Test
    public void test() throws Exception {
        this.completionService = new GptService(LambdaRuntime.getLogger());

        final var persona = Persona.random();

        final var promptOptions = new BookReviewPrompt().options();

        final var post = completionService.createBlogPost(promptOptions);
        int i=0;
    }
}
