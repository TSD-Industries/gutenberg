package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.StringJoiner;

public class GutenbergRequestHandler implements RequestHandler<Object, Object> {
    static class RestAuthInfo {
        private final String username;
        private final String password;

        public RestAuthInfo(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", RestAuthInfo.class.getSimpleName() + "[", "]")
                    .add("username='" + username + "'")
                    .add("password='" + password + "'")
                    .toString();
        }
    }

    private static final String SOURCE_FIELD = "source";
    private static final String SOURCE_EVENTS = "aws.events";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private LambdaLogger log;

    @Override
    public Object handleRequest(Object o, Context context) {
        setObjectMapper();
        setLogger(context);

        log.log("Handling request (" + o.getClass() + "):\n" + o);

        final var authInfo = restAuthInfo();

        log.log("Auth info: " + authInfo);

        final var jsonNode = objectMapper.convertValue(o, JsonNode.class);

        log.log("jsonNode:\n" + jsonNode.toString());

        if (isScheduledEvent(jsonNode)) {
            log.log("Handling scheduled event:\n" + jsonNode);
        }

        return null;
    }

    private static RestAuthInfo restAuthInfo() {
        return new RestAuthInfo(
                System.getenv("WP_REST_USER"),
                System.getenv("WP_REST_PASSWORD"));
    }

    private static boolean isScheduledEvent(JsonNode jsonNode) {
        return jsonNode.has(SOURCE_FIELD)
                && jsonNode.get(SOURCE_FIELD).getNodeType().equals(JsonNodeType.STRING)
                && SOURCE_EVENTS.equalsIgnoreCase(jsonNode.get(SOURCE_FIELD).asText());
    }

    private void setObjectMapper() {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    private void setLogger(Context context) {
        log = context.getLogger();
    }
}
