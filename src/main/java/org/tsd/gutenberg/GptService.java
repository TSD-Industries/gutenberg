package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.tsd.gutenberg.prompt.BlogPostOptions;
import org.tsd.gutenberg.prompt.PostCategory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class GptService extends OpenAIService {
    private static final int MAX_TOKENS = 4096;

    public GptService(LambdaLogger log) {
        super(log);
    }

    public Optional<BlogPost> createBlogPost(BlogPostOptions blogPostOptions) throws IOException {
        log.log("Creating blog post with options: " + blogPostOptions);

        /*
        https://github.com/TheoKanning/openai-java
         */
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

        service.createChatCompletion(completionRequest)
                .getChoices().forEach(completionChoice -> {
                    final var logString = String.format("Completion result %s:\n%s",
                            completionChoice.getIndex(),
                            completionChoice.getMessage());
                    log.log(logString);

                    if (blogPostRef.get() == null) {
                        final var postMediaMaybe
                                = generateImage(blogPostOptions.getImageGenerationPrompt());

                        log.log("Generated image URL: " + postMediaMaybe.orElse(null));

                        blogPostRef.set(parsePostFromResponse(
                                blogPostOptions.getAuthorId(),
                                blogPostOptions.getPostCategory(),
                                postMediaMaybe.orElse(null),
                                completionChoice.getMessage().getContent()));
                    }
                });

        log.log("Built blog post info: " + blogPostRef.get());
        return Optional.ofNullable(blogPostRef.get());
    }

    private static BlogPost parsePostFromResponse(Long authorId,
                                                  PostCategory postCategory,
                                                  File imageFile,
                                                  String rawResponse) {
        final var pattern = Pattern.compile(".*?Title:(.*?)Excerpt:(.*?)Body:(.*)", Pattern.DOTALL);
        final var matcher = pattern.matcher(rawResponse);

        if (matcher.find()) {
            final var title = matcher.group(1).trim();
            final var excerpt = matcher.group(2).trim();

            var review = matcher.group(3).trim();
            review = excerpt + "\n\n" + review;
            review = StringUtils.replaceIgnoreCase(review, "conclusion:", "");
            review = StringUtils.replaceIgnoreCase(review, "outro:", "");
            review = review.trim();

            return BlogPost.builder()
                    .title(title)
                    .excerpt(excerpt)
                    .body(review)
                    .author(authorId)
                    .category(postCategory)
                    .imageFile(imageFile)
                    .build();
        }

        return null;
    }

    public Optional<File> generateImage(String prompt) {
        log.log("Generating image with prompt: " + prompt);

        final var request = CreateImageRequest.builder()
                .prompt(prompt)
                .size("1024x1024")
                .n(1)
                .build();

        final var imageResult = service.createImage(request);

        if (imageResult.getData() != null && !imageResult.getData().isEmpty()) {
            log.log("Image generation successful, downloading...");
            try {
                final var file = Files.createTempFile("tempImg", ".png").toFile();
                FileUtils.copyInputStreamToFile(
                        URI.create(imageResult.getData().get(0).getUrl()).toURL().openConnection().getInputStream(),
                        file);
                return Optional.of(file);
            } catch (Exception e) {
                log.log("Error generating image: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }
}
