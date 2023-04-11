package org.tsd.gutenberg.test;

import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import org.junit.jupiter.api.Test;
import org.tsd.gutenberg.CompletionService;
import org.tsd.gutenberg.PromptGenerator;
import org.tsd.gutenberg.prompt.Persona;
import org.tsd.gutenberg.prompt.PromptOptions;

public class CompletionServiceTest {
    private CompletionService completionService;
    private PromptGenerator promptGenerator = new PromptGenerator();

    @Test
    public void test() throws Exception {
        this.completionService = new CompletionService(LambdaRuntime.getLogger());

        final var promptOptions = PromptOptions.builder()
                .persona(Persona.random())
                .build();

        final var post = completionService.createBlogPost(promptGenerator.generate(promptOptions));
        int i=0;
    }
}
