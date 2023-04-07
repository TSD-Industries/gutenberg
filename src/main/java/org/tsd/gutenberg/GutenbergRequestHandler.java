package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class GutenbergRequestHandler implements RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object o, Context context) {
        System.err.println("Handling request: " + o);

        if (o instanceof APIGatewayProxyRequestEvent) {
            System.err.println("API event:\n" + o);
            return "SUCCESS";
        } else {
            throw new RuntimeException("Unsupported event: " + o);
        }
    }
}
