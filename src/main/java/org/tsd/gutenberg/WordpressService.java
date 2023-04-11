package org.tsd.gutenberg;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.model.Guid;
import com.afrozaar.wordpress.wpapi.v2.model.Media;
import com.afrozaar.wordpress.wpapi.v2.model.PostStatus;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ContentBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ExcerptBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.PostBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.TitleBuilder;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.io.UrlResource;

import java.util.List;
import java.util.Optional;

public class WordpressService {
    private static final List<Long> STOCK_MEDIA_ID = List.of(264L);

    private final LambdaLogger log;

    public WordpressService(LambdaLogger log) {
        this.log = log;
    }

    private static long randomStockImage() {
        return STOCK_MEDIA_ID.get(RandomUtils.nextInt(0, STOCK_MEDIA_ID.size()));
    }

    public void post(WpInfo wpInfo, BlogPost blogPost) {
        final var wpClient = ClientFactory
                .fromConfig(ClientConfig.of(
                        wpInfo.getBaseUrl(),
                        wpInfo.getUsername(),
                        wpInfo.getPassword(),
                        true,
                        true));

        final Long mediaId = blogPost.getImageUrl() == null
                ? randomStockImage() : uploadMedia(wpClient, blogPost.getImageUrl()).orElse(randomStockImage());

        final var newPost = PostBuilder.aPost()
                .withTitle(TitleBuilder.aTitle().withRendered(blogPost.getTitle()).build())
                .withExcerpt(ExcerptBuilder.anExcerpt().withRendered(blogPost.getExcerpt()).build())
                .withContent(ContentBuilder.aContent().withRendered(blogPost.getBody()).build())
                .withAuthor(blogPost.getAuthor())
                .withCategories(List.of(blogPost.getCategory().getCategoryId()))
                .withFeaturedMedia(mediaId)
                .build();

        try {
            wpClient.createPost(newPost, PostStatus.publish);
        } catch (Exception e) {
            log.log("ERROR publishing post: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Optional<Long> uploadMedia(Wordpress wpClient, String url) {
        try {
            final var media = new Media();
            media.setGuid(new Guid());
            media.setId(System.currentTimeMillis());
            media.setTitle(TitleBuilder.aTitle().withRendered(Long.toString(System.currentTimeMillis())).build());
            media.setCaption("Light weight baby.");

//            final var resource = new ByteArrayResource(data);
            final var resource = new UrlResource(url);

            wpClient.createMedia(media, resource);
            return Optional.of(media.getId());
        } catch (Exception e) {
            log.log("Error uploading media: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
