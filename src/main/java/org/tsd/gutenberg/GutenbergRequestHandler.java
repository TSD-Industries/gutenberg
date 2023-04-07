package org.tsd.gutenberg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class GutenbergRequestHandler implements RequestHandler<Object, Object> {

    private LambdaLogger log;

    @Override
    public Object handleRequest(Object o, Context context) {
        log = context.getLogger();
        log.log("Handling request (" + o.getClass() + "):\n" + o);

        if (o instanceof APIGatewayProxyRequestEvent) {
            log.log("API event:\n" + o);
            return "SUCCESS";
        } else {
            throw new RuntimeException("Unsupported event (" + o.getClass() + "): " + o);
        }
    }
}
