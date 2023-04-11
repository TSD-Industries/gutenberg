package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.apache.commons.lang3.StringUtils;
import org.tsd.gutenberg.prompt.BlogPostOptions;
import org.tsd.gutenberg.prompt.PostCategory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class CompletionService {
    private static final int MAX_TOKENS = 4096;

    private final LambdaLogger log;
    private final BlogPostGenerator promptGenerator = new BlogPostGenerator();

    public CompletionService(LambdaLogger log) {
        this.log = log;
    }

    public Optional<BlogPost> createBlogPost(BlogPostOptions blogPostOptions) throws IOException {
        final var openAiService = new OpenAiService(openAiApiKey(), Duration.ofMinutes(1));

        /*
        https://github.com/TheoKanning/openai-java
         */
//        final var completionRequest = CompletionRequest
//                .builder()
//                .prompt(promptText)
//                .model("text-davinci-003")
//                .echo(false)
//                .maxTokens(maxToken)
//                .build();

        final var settingsMessage = new ChatMessage("system", blogPostOptions.getSettingsMessage());
        final var instructionMessage = new ChatMessage("user", blogPostOptions.getInstructionMessage());

        final var maxToken = MAX_TOKENS
                - blogPostOptions.getSettingsMessage().length()
                - blogPostOptions.getInstructionMessage().length()
                - 1 ;

        final var completionRequest = ChatCompletionRequest
                .builder()
                .messages(List.of(settingsMessage, instructionMessage))
                .model("gpt-3.5-turbo")
                .maxTokens(maxToken)
                .build();

        final var blogPostRef = new AtomicReference<BlogPost>();

        openAiService.createChatCompletion(completionRequest)
                .getChoices().forEach(completionChoice -> {
                    final var logString = String.format("Completion result %s:\n%s",
                            completionChoice.getIndex(),
                            completionChoice.getMessage());
                    log.log(logString);
                    if (blogPostRef.get() == null) {
                        blogPostRef.set(parsePostFromResponse(
                                blogPostOptions.getAuthorId(),
                                blogPostOptions.getPostCategory(),
                                completionChoice.getMessage().getContent()));
                    }
                });

        return Optional.ofNullable(blogPostRef.get());
    }

    private static BlogPost parsePostFromResponse(Long authorId,
                                                  PostCategory postCategory,
                                                  String rawResponse) {
        final var pattern = Pattern.compile(".*?Title:(.*?)Excerpt:(.*?)Body:(.*)", Pattern.DOTALL);
        final var matcher = pattern.matcher(rawResponse);

        if (matcher.find()) {
            final var title = matcher.group(1).trim();
            final var excerpt = matcher.group(2).trim();

            var review = matcher.group(3).trim();
            review = StringUtils.replaceIgnoreCase(review, "conclusion:", "");
            review = review.trim();


            return BlogPost.builder()
                    .title(title)
                    .excerpt(excerpt)
                    .body(review)
                    .author(authorId)
                    .category(postCategory)
                    .build();
        }

        return null;
    }

    private static String openAiApiKey() {
        final var apiKey = System.getenv("OPEN_AI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Could not find OpenAI API KEY in environment.");
        }

        return apiKey;
    }
}
