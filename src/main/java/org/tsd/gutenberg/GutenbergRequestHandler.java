package org.tsd.gutenberg;

import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.model.PostStatus;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ContentBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ExcerptBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.PostBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.TitleBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.Builder;
import lombok.Data;

public class GutenbergRequestHandler implements RequestHandler<Object, Object> {
    @Data
    @Builder
    static class WpInfo {
        private final String username;
        private final String password;
        private final String baseUrl;
    }

    private static final String SOURCE_FIELD = "source";
    private static final String SOURCE_EVENTS = "aws.events";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private LambdaLogger log;

    @Override
    public Object handleRequest(Object o, Context context) {
        setLogger(context);
        log.log("Handling request (" + o.getClass() + "):\n" + o);

        final var wpInfo = wpInfo();
        log.log("WP info: " + wpInfo);

        final var jsonNode = objectMapper.convertValue(o, JsonNode.class);

        log.log("jsonNode:\n" + jsonNode.toString());

        if (isScheduledEvent(jsonNode)) {
            log.log("Handling scheduled event:\n" + jsonNode);

            final var wpClient = ClientFactory
                    .fromConfig(ClientConfig.of(
                            wpInfo.getBaseUrl(),
                            wpInfo.getUsername(),
                            wpInfo.getPassword(),
                            true,
                            true));

            final var newPost = PostBuilder.aPost()
                    .withTitle(TitleBuilder.aTitle().withRendered("YOOOOO " + System.currentTimeMillis()).build())
                    .withExcerpt(ExcerptBuilder.anExcerpt().withRendered("DAAAAAMN").build())
                    .withContent(ContentBuilder.aContent().withRendered("Some content").build())
                    .build();

            try {
                wpClient.createPost(newPost, PostStatus.publish);
            } catch (Exception e) {
                log.log("ERROR publishing post: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return null;
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

    private void setLogger(Context context) {
        log = context.getLogger();
    }
}
