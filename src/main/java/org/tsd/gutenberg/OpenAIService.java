package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;

public abstract class OpenAIService {
    protected final LambdaLogger log;
    protected final OpenAiService service;

    public OpenAIService(LambdaLogger log) {
        this.log = log;
        this.service = new OpenAiService(openAiApiKey(), Duration.ofMinutes(1));
    }

    private static String openAiApiKey() {
        final var apiKey = System.getenv("OPEN_AI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Could not find OpenAI API KEY in environment.");
        }

        return apiKey;
    }
}
