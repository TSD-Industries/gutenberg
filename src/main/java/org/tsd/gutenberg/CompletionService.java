package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.tsd.gutenberg.prompt.BlogPostOptions;
import org.tsd.gutenberg.prompt.PostCategory;

import java.io.IOException;
import java.time.Duration;
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
        final var promptText = blogPostOptions.getPromptText();

        final var openAiService = new OpenAiService(openAiApiKey(), Duration.ofMinutes(1));

        final var maxToken = MAX_TOKENS - promptText.length() - 1;

        /*
        https://github.com/TheoKanning/openai-java
         */
        final var completionRequest = CompletionRequest
                .builder()
                .prompt(promptText)
                .model("text-davinci-003")
                .echo(false)
                .maxTokens(maxToken)
                .build();

        final var blogPostRef = new AtomicReference<BlogPost>();

        openAiService.createCompletion(completionRequest)
                .getChoices().forEach(completionChoice -> {
                    final var logString = String.format("Completion result %s (%s):\n%s",
                            completionChoice.getIndex(),
                            completionChoice.getFinish_reason(),
                            completionChoice.getText());
                    log.log(logString);
                    if (blogPostRef.get() == null) {
                        blogPostRef.set(parsePostFromResponse(
                                blogPostOptions.getAuthorId(),
                                blogPostOptions.getPostCategory(),
                                completionChoice.getText()));
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
            final var review = matcher.group(3).trim();
            return BlogPost.builder()
                    .title(title)
                    .excerpt(excerpt)
                    .body(review)
                    .author(authorId)
                    .category(postCategory.getCategoryName())
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
