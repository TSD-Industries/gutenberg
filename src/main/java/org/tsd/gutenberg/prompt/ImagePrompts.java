package org.tsd.gutenberg.prompt;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class ImagePrompts {
    private static final List<String> PROMPTS = List.of(
            "very muscular guy writing at his desk with weights in the background",
            "very muscular woman writing at her desk with weights in the background",
            "a stack of books in a gym",
            "a muscular guy reading a book next to a barbell",
            "a muscular woman reading a book next to a barbell"
    );

    public static String randomImagePrompt() {
        return PROMPTS.get(RandomUtils.nextInt(0, PROMPTS.size()));
    }
}
