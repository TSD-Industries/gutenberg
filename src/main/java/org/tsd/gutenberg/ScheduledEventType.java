package org.tsd.gutenberg;

public enum ScheduledEventType {
    BOOK_REVIEW("book_review"),
    SISYPHUS("sisyphus");

    private final String code;

    ScheduledEventType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
