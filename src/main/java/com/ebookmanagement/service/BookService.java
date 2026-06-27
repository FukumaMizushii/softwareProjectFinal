package com.ebookmanagement.service;

import com.ebookmanagement.dto.BookDto;
import com.ebookmanagement.entity.Book;

import java.util.List;

public interface BookService {
    Book save(BookDto dto);
    Book update(Long id, BookDto dto);
    List<Book> findAll();
    Book findById(Long id);
    List<Book> search(String keyword);
    List<Book> findByCategory(Long categoryId);
    void deleteById(Long id);
}
