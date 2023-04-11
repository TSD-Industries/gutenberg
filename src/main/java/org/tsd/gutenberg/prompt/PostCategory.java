package org.tsd.gutenberg.prompt;

public enum PostCategory {
    UNCATEGORIZED("Uncategorized"),
    BOOK_REVIEW("Book Review");

    private final String categoryName;

    PostCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
