package com.ebookmanagement.repository;

import com.ebookmanagement.entity.Book;
import com.ebookmanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    List<Book> findByCategory(Category category);

    long countByCategory(Category category);
}
