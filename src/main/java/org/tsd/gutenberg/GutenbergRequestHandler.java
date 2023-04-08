package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GutenbergRequestHandler implements RequestHandler<Object, Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private LambdaLogger log;

    @Override
    public Object handleRequest(Object o, Context context) {
        setObjectMapper();
        setLogger(context);

        log.log("Handling request (" + o.getClass() + "):\n" + o);

        try {
            final var jsonNode = objectMapper.readTree(o.toString());

            log.log("jsonNode:\n" + jsonNode.toString());
            if (o instanceof APIGatewayProxyRequestEvent) {
                log.log("API event:\n" + o);
                return "SUCCESS";
            } else {
                throw new RuntimeException("Unsupported event (" + o.getClass() + "): " + o);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void setObjectMapper() {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    private void setLogger(Context context) {
        log = context.getLogger();
    }
}
