package org.tsd.gutenberg.test;

import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import org.junit.jupiter.api.Test;
import org.tsd.gutenberg.GptService;
import org.tsd.gutenberg.WordpressService;
import org.tsd.gutenberg.WpInfo;
import org.tsd.gutenberg.prompt.SisyphusPrompt;

public class SisyphusTest {
    private GptService gptService;
    private WordpressService wordpressService;

    @Test
    public void test() throws Exception {
        this.gptService = new GptService(LambdaRuntime.getLogger());

        final var wpInfo = WpInfo.builder()
                .baseUrl("https://www.heavyreadingclub.com")
                .username("gutenberg-test")
                .password("Xd0S8tkmJimkd3zd&9QPDkge")
                .build();

        this.wordpressService = new WordpressService(LambdaRuntime.getLogger(), wpInfo);

        final var promptOptions = new SisyphusPrompt().options();

        final var post = gptService.createBlogPost(promptOptions);

        if (post.isPresent()) {
            System.err.println("Title: " + post.get().getTitle());
            System.err.println("Excerpt: " + post.get().getExcerpt());
            System.err.println("Body:\n" + post.get().getBody());
        }
    }
}
