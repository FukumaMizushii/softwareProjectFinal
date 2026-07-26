package com.ebookmanagement.pattern.adapter;

import com.ebookmanagement.dto.BookDto;

/**
 * ============================================================
 * DESIGN PATTERN: ADAPTER
 * ============================================================
 * Converts an incompatible LegacyBookRecord into the BookDto that
 * the rest of our application understands. The adapter "translates"
 * between the two structures:
 *   bookName     -> title
 *   writer       -> author
 *   priceInCents -> price (dollars)
 *   summary      -> description
 *
 * Thanks to this adapter, we can import books from the legacy catalog
 * WITHOUT changing either the legacy class or our BookDto.
 * ============================================================
 */
public class LegacyBookAdapter {

    // 100 cents = 1 dollar. Named constant instead of a magic number.
    private static final int CENTS_PER_DOLLAR = 100;

    private final LegacyBookRecord legacyRecord;

    public LegacyBookAdapter(LegacyBookRecord legacyRecord) {
        this.legacyRecord = legacyRecord;
    }

    /**
     * Produces a BookDto for our services.
     * A category id must be supplied because the legacy record
     * has no equivalent field.
     */
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