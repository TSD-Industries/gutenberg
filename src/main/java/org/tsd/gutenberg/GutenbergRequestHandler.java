package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.tsd.gutenberg.prompt.Persona;
import org.tsd.gutenberg.prompt.PromptOptions;

import java.util.Random;

public class GutenbergRequestHandler implements RequestHandler<Object, Object> {

    private static final String SOURCE_FIELD = "source";
    private static final String SOURCE_EVENTS = "aws.events";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private CompletionService completionService;
    private WordpressService wordpressService;
    private PromptGenerator promptGenerator = new PromptGenerator();
    private LambdaLogger log;

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
                final var prompt = promptGenerator.generate(buildPromptOptions());
                final var blogPostMaybe = completionService.createBlogPost(prompt);
                if (blogPostMaybe.isPresent()) {
                    log.log("Creating blog post:\n" + blogPostMaybe.get());
                    wordpressService.post(wpInfo, blogPostMaybe.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private PromptOptions buildPromptOptions() {
        final var promptBuilder = PromptOptions.builder();
        final var random = new Random();

        if (random.nextDouble() > 0.25) {
            promptBuilder.persona(Persona.random());
        }

        return promptBuilder.build();
    }

    private static WpInfo wpInfo() {
        return WpInfo.builder()
                .username(System.getenv("WP_REST_USER"))
                .password(System.getenv("WP_REST_PASSWORD"))
                .baseUrl(System.getenv("WP_URL"))
                .build();
    }

    private static boolean isScheduledEvent(JsonNode jsonNode) {
        return jsonNode.has(SOURCE_FIELD)
                && jsonNode.get(SOURCE_FIELD).getNodeType().equals(JsonNodeType.STRING)
                && SOURCE_EVENTS.equalsIgnoreCase(jsonNode.get(SOURCE_FIELD).asText());
    }

    private void initialize(Context context) {
        this.log = context.getLogger();
        this.completionService = new CompletionService(log);
        this.wordpressService = new WordpressService(log);
    }
}
