package com.ebookmanagement.service;

import com.ebookmanagement.entity.Book;
import com.ebookmanagement.entity.CollectionItem;

import java.util.List;

public interface CollectionService {
    void addToCollection(String userEmail, Long bookId);
    void removeFromCollection(String userEmail, Long bookId);
    List<Book> getUserCollection(String userEmail);
    boolean isInCollection(String userEmail, Long bookId);
}
