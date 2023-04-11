package org.tsd.gutenberg;

import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.model.PostStatus;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ContentBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ExcerptBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.PostBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.TitleBuilder;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class WordpressService {
    private final LambdaLogger log;

    public WordpressService(LambdaLogger log) {
        this.log = log;
    }

    public void post(WpInfo wpInfo, BlogPost blogPost) {
        final var wpClient = ClientFactory
                .fromConfig(ClientConfig.of(
                        wpInfo.getBaseUrl(),
                        wpInfo.getUsername(),
                        wpInfo.getPassword(),
                        true,
                        true));

        final var newPost = PostBuilder.aPost()
                .withTitle(TitleBuilder.aTitle().withRendered(blogPost.getTitle()).build())
                .withExcerpt(ExcerptBuilder.anExcerpt().withRendered(blogPost.getExcerpt()).build())
                .withContent(ContentBuilder.aContent().withRendered(blogPost.getBody()).build())
                .withAuthor(blogPost.getAuthor())
                .build();

        try {
            wpClient.createPost(newPost, PostStatus.publish);
        } catch (Exception e) {
            log.log("ERROR publishing post: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
