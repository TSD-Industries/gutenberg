package org.tsd.gutenberg.prompt;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

public class BookReviewPrompt extends Prompt {
    private static final String CLASSICS_CSV = "/classics.csv";
    private static final int NUM_ENTRIES = 1006;
    private static final int TITLE_RECORD_IDX = 3;
    private static final int AUTHOR_RECORD_IDX = 11;

    public BookReviewPrompt() {
        super(PostCategory.BOOK_REVIEW);
    }

    private String getRandomBook() throws IOException {
        final var targetIdx = RandomUtils.nextInt(0, NUM_ENTRIES);

        final var inputStreamReader = new InputStreamReader(getClass().getResourceAsStream(CLASSICS_CSV));
        final var csvRecordIterator = CSVFormat.DEFAULT
                .builder()
                .setSkipHeaderRecord(true)
                .build()
                .parse(inputStreamReader)
                .iterator();

        var currentIdx = 0;
        AtomicReference<String[]> titleAuthor = new AtomicReference<>();

        while (csvRecordIterator.hasNext() && titleAuthor.get() == null) {
            CSVRecord record = csvRecordIterator.next();
            if (currentIdx == targetIdx) {
                final var title = record.get(TITLE_RECORD_IDX);
                final var author = record.get(AUTHOR_RECORD_IDX);
                titleAuthor.set(new String[]{title, author});
            } else {
                currentIdx++;
            }
        }

        return String.format("\"%s\" by %s", titleAuthor.get()[0], titleAuthor.get()[1]);
    }

    @Override
    public BlogPostOptions options() throws Exception {
        final var book = getRandomBook();
        final var persona = RandomUtils.nextDouble() > 0.25 ? Persona.random() : null;

        // TODO: generate and upload file.

        return BlogPostOptions.builder()
                .authorId(persona == null ? null : persona.getWordPressUserId())
                .postCategory(PostCategory.BOOK_REVIEW)
                .settingsMessage(generateSettingsText(persona))
                .instructionMessage(generateInstructionText(book))
                .imageGenerationPrompt(ImagePrompts.randomImagePrompt())
                .build();
    }

    private String generateSettingsText(Persona persona) {
        if (persona == null) {
            return "Hello.";
        } else {
            return String.format(
                    "Pretend you are %s. %s.",
                    persona.getName(),
                    StringUtils.join(persona.getEmphases(), ". "));
        }
    }

    private String generateInstructionText(String book) throws IOException {
        return "Write a blog post. The blog post must be heavily influenced by the qualities I described earlier in an exaggerated, over-the-top way. "
                + "The first part of the post should be a review of the book " + book + ". "
                + "The review should begin with a synopsis or plot summary, "
                + "continue with a description of why the book is important or significant, "
                + "and conclude with why someone should read it. Find a way to make the book relevant to people who lift weights. "
                + "Conclude the blog post with an invitation for readers to comment on what they read today and what they did in the gym. "
                + "You must write only one blog post, not multiple. "
                + "The title of the blog post must include the title of the book. "
                + "Your response should be in the following format:\n"
                + "Title:\n"
                + "Excerpt:\n"
                + "Body:\n";
    }
}
