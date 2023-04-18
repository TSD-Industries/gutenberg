package org.tsd.gutenberg.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.tsd.gutenberg.GptService;
import org.tsd.gutenberg.WordpressService;
import org.tsd.gutenberg.WpInfo;
import org.tsd.gutenberg.generate.BlogPostGenerator;
import org.tsd.gutenberg.prompt.BlogPostOptions;

public abstract class GutenbergRequestHandler implements RequestHandler<Object, Object> {

    protected static final String SOURCE_FIELD = "source";
    protected static final String SOURCE_EVENTS = "aws.events";

    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final BlogPostGenerator blogPostGenerator;
    protected GptService gptService;
    protected WordpressService wordpressService;

    protected LambdaLogger log;

    public GutenbergRequestHandler(BlogPostGenerator blogPostGenerator) {
        this.blogPostGenerator = blogPostGenerator;
    }

    protected BlogPostOptions generateBlogPost() throws Exception {
        return blogPostGenerator.generate();
    }

    protected static WpInfo wpInfo() {
        return WpInfo.builder()
                .username(System.getenv("WP_REST_USER"))
                .password(System.getenv("WP_REST_PASSWORD"))
                .baseUrl(System.getenv("WP_URL"))
                .build();
    }

    protected boolean isScheduledEvent(JsonNode jsonNode) {
        log.log("Checking if scheduled event: " + jsonNode.toString());
        return jsonNode.has(SOURCE_FIELD)
                && jsonNode.get(SOURCE_FIELD).getNodeType().equals(JsonNodeType.STRING)
                && SOURCE_EVENTS.equalsIgnoreCase(jsonNode.get(SOURCE_FIELD).asText());
    }

    protected void initialize(Context context) {
        this.log = context.getLogger();
        this.gptService = new GptService(log);
        this.wordpressService = new WordpressService(log);
    }
}
