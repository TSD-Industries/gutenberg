package org.tsd.gutenberg.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import org.tsd.gutenberg.generate.GenericBlogPostGenerator;

public class GenericBlogPostRequestHandler extends GutenbergRequestHandler {
    public GenericBlogPostRequestHandler() {
        super(new GenericBlogPostGenerator());
    }

    @Override
    public Object handleRequest(Object o, Context context) {
        initialize(context);
        log.log("Handling request (" + o.getClass() + "):\n" + o);

        final var wpInfo = wpInfo();
        log.log("WP info: " + wpInfo);

        final var jsonNode = objectMapper.convertValue(o, JsonNode.class);

        log.log("jsonNode:\n" + jsonNode.toString());

        if (isScheduledEvent(jsonNode)) {
            try {
                log.log("Handling scheduled event:\n" + jsonNode);
                final var options = generateBlogPost();
                final var blogPostMaybe = gptService.createBlogPost(options);

                if (blogPostMaybe.isPresent()) {
                    log.log("Creating blog post:\n" + blogPostMaybe.get());
                    wordpressService.post(wpInfo, blogPostMaybe.get());
                    log.log("Successfully created blog post.");
                }
            } catch (Exception e) {
                log.log("Error creating blog post: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return null;
    }
}
