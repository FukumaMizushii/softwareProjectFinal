package com.ebookmanagement.service.impl;

import com.ebookmanagement.dto.BookDto;
import com.ebookmanagement.entity.Book;
import com.ebookmanagement.entity.Category;
import com.ebookmanagement.exception.ResourceNotFoundException;
import com.ebookmanagement.repository.BookRepository;
import com.ebookmanagement.service.BookService;
import com.ebookmanagement.service.CategoryService;
import com.ebookmanagement.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    public BookServiceImpl(BookRepository bookRepository,
                           CategoryService categoryService,
                           FileStorageService fileStorageService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public Book save(BookDto dto) {
        Book book = new Book();
        applyDtoToBook(dto, book);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Long id, BookDto dto) {
        Book book = findById(id);
        applyDtoToBook(dto, book);
        return bookRepository.save(book);
    }

    /** Shared logic for both create and update. Handles optional file uploads. */
    private void applyDtoToBook(BookDto dto, Book book) {
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());

        Category category = categoryService.findById(dto.getCategoryId());
        book.setCategory(category);

        // Only replace the book file if a new one was uploaded.
        MultipartFile bookFile = dto.getBookFile();
        if (bookFile != null && !bookFile.isEmpty()) {
            book.setFilePath(fileStorageService.storeFile(bookFile, "books"));
        } else if (dto.getExistingFilePath() != null) {
            book.setFilePath(dto.getExistingFilePath());
        }

        // Only replace the cover image if a new one was uploaded.
        MultipartFile cover = dto.getCoverImage();
        if (cover != null && !cover.isEmpty()) {
            book.setCoverImagePath(fileStorageService.storeFile(cover, "covers"));
        } else if (dto.getExistingCoverImagePath() != null) {
            book.setCoverImagePath(dto.getExistingCoverImagePath());
        }
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    @Override
    public List<Book> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public List<Book> findByCategory(Long categoryId) {
        Category category = categoryService.findById(categoryId);
        return bookRepository.findByCategory(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
    }
}
