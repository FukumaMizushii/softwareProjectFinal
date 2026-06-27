package com.ebookmanagement.repository;

import com.ebookmanagement.entity.Book;
import com.ebookmanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Data access for Book entities, including search helpers. */
public interface BookRepository extends JpaRepository<Book, Long> {

    // Case-insensitive search by title OR author.
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    List<Book> findByCategory(Category category);

    long countByCategory(Category category);
}
