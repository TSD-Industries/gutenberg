package org.tsd.gutenberg.prompt;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class SisyphusPrompt extends Prompt {
    private static final long AUTHOR_ID = 10;
    private static final long IMG_ID = 385;

    public SisyphusPrompt() {
        super(PostCategory.SISYPHUS);
    }

    @Override
    public BlogPostGenerationSettings options() throws Exception {
        return BlogPostGenerationSettings.builder()
                .authorId(AUTHOR_ID)
                .postCategory(PostCategory.SISYPHUS)
                .settingsMessage(settingsText())
                .instructionMessage(instructionText())
                .mediaId(IMG_ID)
                .build();
    }

    private static String settingsText() {
        return "Pretend you are Sisyphus. You are a blogger who tries to give "
                + "good advice to readers, but you are melancholy because you know "
                + "your advice will go unheeded by others and they will never "
                + "make progress. You are cursed to forever give advice to people "
                + "who will not take it.";
    }

    private static String instructionText() {
        final var topic = randomAdviceTopic();

        return "Write a blog post on the topic of " + topic + ". "
                + "The blog post must be heavily influenced by the qualities I described earlier in an exaggerated, over-the-top way. "
                + "You must write only one blog post, not multiple. "
                + "Your response should be in the following format:\n"
                + "Title:\n"
                + "Excerpt:\n"
                + "Body:\n";
    }

    private static String randomAdviceTopic() {
        return ADVICE_TOPICS.get(RandomUtils.nextInt(0, ADVICE_TOPICS.size()));
    }

    private static final List<String> ADVICE_TOPICS = List.of(
            "losing weight",
            "gaining muscle",
            "squatting with correct form",
            "deadlifting with correct form",
            "benching with correct form",
            "the benefits of bodyweight exercises",
            "the benefits of strength training",
            "the benefits of exercise",
            "the benefits of eating well",
            "proper gym etiquette",
            "the importance of warming up before a workout"
    );
}
