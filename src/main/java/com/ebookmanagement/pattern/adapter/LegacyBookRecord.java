package com.ebookmanagement.pattern.adapter;


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
