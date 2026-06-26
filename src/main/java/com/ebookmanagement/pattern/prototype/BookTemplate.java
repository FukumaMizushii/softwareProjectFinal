package com.ebookmanagement.pattern.prototype;


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


    public BookTemplate copy() {
        try {

            return (BookTemplate) super.clone();
        } catch (CloneNotSupportedException e) {

            throw new IllegalStateException("Cloning a BookTemplate failed", e);
        }
    }



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
