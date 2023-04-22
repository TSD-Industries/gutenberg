package org.tsd.gutenberg.test;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tsd.gutenberg.GptService;
import org.tsd.gutenberg.WordpressService;
import org.tsd.gutenberg.prompt.BookReviewPrompt;
import org.tsd.gutenberg.prompt.Persona;

public class CompletionServiceTest {
    private GptService gptService;
    private Wordpress wordpress;
    private WordpressService wordpressService;

    @Test
    @Disabled
    public void test() throws Exception {
        this.gptService = new GptService(LambdaRuntime.getLogger());

        this.wordpress = ClientFactory
                .fromConfig(ClientConfig.of(
                        "https://www.heavyreadingclub.com",
                        "gutenberg-test",
                        "Xd0S8tkmJimkd3zd&9QPDkge",
                        true,
                        true));

        this.wordpressService = new WordpressService(LambdaRuntime.getLogger(), null);

        final var persona = Persona.random();

        final var promptOptions = new BookReviewPrompt(gptService, wordpressService).options();

        final var post = gptService.createBlogPost(promptOptions);
        int i=0;
    }
}
