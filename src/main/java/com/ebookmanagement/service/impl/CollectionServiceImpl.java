package com.ebookmanagement.service.impl;

import com.ebookmanagement.entity.Book;
import com.ebookmanagement.entity.CollectionItem;
import com.ebookmanagement.entity.User;
import com.ebookmanagement.repository.CollectionItemRepository;
import com.ebookmanagement.service.BookService;
import com.ebookmanagement.service.CollectionService;
import com.ebookmanagement.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionItemRepository collectionItemRepository;
    private final UserService userService;
    private final BookService bookService;

    public CollectionServiceImpl(CollectionItemRepository collectionItemRepository,
                                 UserService userService,
                                 BookService bookService) {
        this.collectionItemRepository = collectionItemRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    @Transactional
    public void addToCollection(String userEmail, Long bookId) {
        User user = userService.findByEmail(userEmail);
        Book book = bookService.findById(bookId);
        // Avoid duplicates: only add if not already present.
        if (!collectionItemRepository.existsByUserAndBook(user, book)) {
            CollectionItem item = new CollectionItem();
            item.setUser(user);
            item.setBook(book);
            collectionItemRepository.save(item);
        }
    }

    @Override
    @Transactional
    public void removeFromCollection(String userEmail, Long bookId) {
        User user = userService.findByEmail(userEmail);
        Book book = bookService.findById(bookId);
        collectionItemRepository.deleteByUserAndBook(user, book);
    }

    @Override
    public List<Book> getUserCollection(String userEmail) {
        User user = userService.findByEmail(userEmail);
        return collectionItemRepository.findByUser(user)
                .stream()
                .map(CollectionItem::getBook)
                .toList();
    }

    @Override
    public boolean isInCollection(String userEmail, Long bookId) {
        User user = userService.findByEmail(userEmail);
        Book book = bookService.findById(bookId);
        return collectionItemRepository.existsByUserAndBook(user, book);
    }
}
