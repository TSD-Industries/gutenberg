package org.tsd.gutenberg;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.model.Media;
import com.afrozaar.wordpress.wpapi.v2.model.PostStatus;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ContentBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ExcerptBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.PostBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.TitleBuilder;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class WordpressService {
    private static final List<Long> STOCK_MEDIA_ID = List.of(264L);

    private final LambdaLogger log;
    private final Wordpress wpClient;

    public WordpressService(LambdaLogger log, WpInfo wpInfo) {
        this.log = log;
        this.wpClient = ClientFactory
                .fromConfig(ClientConfig.of(
                        wpInfo.getBaseUrl(),
                        wpInfo.getUsername(),
                        wpInfo.getPassword(),
                        true,
                        true));
    }

    private static long randomStockImage() {
        return STOCK_MEDIA_ID.get(RandomUtils.nextInt(0, STOCK_MEDIA_ID.size()));
    }

    public void post(BlogPost blogPost) {
        final var mediaId = blogPost.getImageMediaId() == null ? randomStockImage() : blogPost.getImageMediaId();

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

    public Optional<Long> uploadMedia(File imageFile) {
        try {
            final var media = new Media();
            media.setTitle(TitleBuilder.aTitle().withRendered(Long.toString(System.currentTimeMillis())).build());
            media.setMediaType("image");
            media.setMimeType("image/png");
            media.setCaption("Light weight baby.");
            final var uploadedMedia = wpClient.createMedia(media, new FileSystemResource(imageFile));
            return Optional.of(uploadedMedia.getId());
        } catch (Exception e) {
            log.log("Error uploading media: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
