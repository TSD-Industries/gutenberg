package org.tsd.gutenberg.handler;

import com.amazonaws.services.lambda.runtime.Context;
import org.tsd.gutenberg.generate.SisyphusBlogPostGenerator;
import org.tsd.gutenberg.prompt.BlogPostGenerationSettings;

public class SisyphusBlogPostRequestHandler extends GutenbergRequestHandler {

    private SisyphusBlogPostGenerator sisyphusBlogPostGenerator;

    @Override
    BlogPostGenerationSettings buildBlogPostSettings() throws Exception {
        return sisyphusBlogPostGenerator.generate();
    }

    @Override
    public Object handleRequest(Object o, Context context) {
        log.log("Handling Sisyphus blog request (" + o.getClass() + "):\n" + o);
        initialize(context);
        this.sisyphusBlogPostGenerator = new SisyphusBlogPostGenerator();
        generateAndPost();
        return null;
    }
}
