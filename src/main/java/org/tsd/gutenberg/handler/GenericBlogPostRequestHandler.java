package org.tsd.gutenberg.handler;

import com.amazonaws.services.lambda.runtime.Context;
import org.tsd.gutenberg.generate.GenericBlogPostGenerator;
import org.tsd.gutenberg.prompt.BlogPostGenerationSettings;

public class GenericBlogPostRequestHandler extends GutenbergRequestHandler {

    private GenericBlogPostGenerator genericBlogPostGenerator;

    @Override
    public Object handleRequest(Object o, Context context) {
        log.log("Handling generic blog request (" + o.getClass() + "):\n" + o);
        initialize(context);
        this.genericBlogPostGenerator = new GenericBlogPostGenerator(gptService, wordpressService);
        generateAndPost();
        return null;
    }

    @Override
    BlogPostGenerationSettings buildBlogPostSettings() throws Exception {
        return genericBlogPostGenerator.generate();
    }
}
