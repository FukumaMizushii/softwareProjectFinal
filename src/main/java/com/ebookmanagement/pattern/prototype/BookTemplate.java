package com.ebookmanagement.pattern.prototype;

/**
 * ============================================================
 * DESIGN PATTERN: PROTOTYPE
 * ============================================================
 * A BookTemplate is a "master copy" of common book settings.
 * Instead of typing the same description/price/category every
 * time an admin adds a similar book, we keep ready-made
 * templates and CLONE them, then tweak the small differences
 * (like the title).
 *
 * This implements Prototype because:
 *  - It declares a clone() operation (via Cloneable + copy()).
 *  - Concrete templates are copied rather than rebuilt from scratch.
 *  - The client copies a prototype and changes only what differs.
 * ============================================================
 */
public class BookTemplate implements Cloneable {

    private String title;
    private String author;
    private String description;
    private Double price;
    private Long categoryId;

    public BookTemplate() {
    }

    public BookTemplate(String title, String author, String description,
                        Double price, Long categoryId) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
    }

    /**
     * Returns a copy of this template. This is the heart of the
     * Prototype pattern: we duplicate an existing object instead
     * of constructing a brand new one field-by-field.
     */
    public BookTemplate copy() {
        try {
            // BookTemplate only holds immutable fields (String, Double, Long),
            // so a shallow clone is a correct, complete copy here.
            return (BookTemplate) super.clone();
        } catch (CloneNotSupportedException e) {
            // Cannot happen because we implement Cloneable, but handle defensively.
            throw new IllegalStateException("Cloning a BookTemplate failed", e);
        }
    }

    // ---- getters / setters ----

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
