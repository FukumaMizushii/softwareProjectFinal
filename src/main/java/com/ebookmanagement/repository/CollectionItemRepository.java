package com.ebookmanagement.repository;

import com.ebookmanagement.entity.Book;
import com.ebookmanagement.entity.CollectionItem;
import com.ebookmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Data access for personal collection entries. */
public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long> {

    List<CollectionItem> findByUser(User user);

    Optional<CollectionItem> findByUserAndBook(User user, Book book);

    boolean existsByUserAndBook(User user, Book book);

    void deleteByUserAndBook(User user, Book book);
}
