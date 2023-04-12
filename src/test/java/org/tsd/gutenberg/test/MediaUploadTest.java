package org.tsd.gutenberg.test;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.model.Guid;
import com.afrozaar.wordpress.wpapi.v2.model.Media;
import com.afrozaar.wordpress.wpapi.v2.model.builder.TitleBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Files;

public class MediaUploadTest {
    private Wordpress wordpress;

    @Test
    public void test() throws Exception {
        this.wordpress = ClientFactory
                .fromConfig(ClientConfig.of(
                        "https://www.heavyreadingclub.com",
                        "gutenberg-test",
                        "Xd0S8tkmJimkd3zd&9QPDkge",
                        true,
                        true));

//        final var imgBytes = IOUtils.toByteArray();

        final var media = new Media();
        media.setGuid(new Guid());
        media.setId(System.currentTimeMillis());
        media.setTitle(TitleBuilder.aTitle().withRendered(Long.toString(System.currentTimeMillis())).build());
        media.setMediaType("image");
        media.setMimeType("image/png");
        media.setCaption("Light weight baby.");

        final var file = Files.createTempFile("img", ".png").toFile();
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/bear.webp"), file);
        final var resource = new FileSystemResource(file);
//        final var resource = new ByteArrayResource(imgBytes);
//        final var resource = new InputStreamResource(new ByteArrayInputStream(imgBytes));
//            final var resource = new UrlResource(url);

        final var mediaUploaded = wordpress.createMedia(media, resource);

        int i=0;
    }
}
