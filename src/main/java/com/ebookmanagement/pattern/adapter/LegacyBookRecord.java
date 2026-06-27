package com.ebookmanagement.pattern.adapter;

/**
 * DESIGN PATTERN: ADAPTER (the "adaptee" — incompatible class)
 *
 * Imagine this class comes from an OLD external catalog system.
 * Its field names and shape do NOT match our application's BookDto:
 *  - it uses "bookName" instead of "title"
 *  - it uses "writer" instead of "author"
 *  - it stores price in CENTS as an int, not dollars as a double
 *  - it has no concept of our category IDs
 *
 * We cannot (or do not want to) change this class, so we will wrap
 * it with an adapter to make it usable in our system.
 */
public class LegacyBookRecord {

    private final String bookName;
    private final String writer;
    private final int priceInCents;
    private final String summary;

    public LegacyBookRecord(String bookName, String writer, int priceInCents, String summary) {
        this.bookName = bookName;
        this.writer = writer;
        this.priceInCents = priceInCents;
        this.summary = summary;
    }

    public String getBookName() {
        return bookName;
    }

    public String getWriter() {
        return writer;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public String getSummary() {
        return summary;
    }
}
