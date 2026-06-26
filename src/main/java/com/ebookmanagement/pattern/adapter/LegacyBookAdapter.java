package com.ebookmanagement.pattern.adapter;

import com.ebookmanagement.dto.BookDto;


public class LegacyBookAdapter {


    private static final int CENTS_PER_DOLLAR = 100;

    private final LegacyBookRecord legacyRecord;

    public LegacyBookAdapter(LegacyBookRecord legacyRecord) {
        this.legacyRecord = legacyRecord;
    }


    public BookDto toBookDto(Long categoryId) {
        BookDto dto = new BookDto();
        dto.setTitle(legacyRecord.getBookName());
        dto.setAuthor(legacyRecord.getWriter());
        dto.setDescription(legacyRecord.getSummary());
        dto.setPrice(legacyRecord.getPriceInCents() / (double) CENTS_PER_DOLLAR);
        dto.setCategoryId(categoryId);
        return dto;
    }
}
