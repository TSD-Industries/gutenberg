package org.tsd.gutenberg.prompt;

public enum PostCategory {
    UNCATEGORIZED(1, "Uncategorized"),
    BOOK_REVIEW(21, "Book Review"),
    SISYPHUS(22, "Sisyphus Sunday");

    private final long categoryId;
    private final String categoryName;

    PostCategory(long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
