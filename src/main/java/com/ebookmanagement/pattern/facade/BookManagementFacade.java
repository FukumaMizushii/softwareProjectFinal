package com.ebookmanagement.pattern.facade;

import com.ebookmanagement.dto.BookDto;
import com.ebookmanagement.entity.Book;
import com.ebookmanagement.entity.Category;
import com.ebookmanagement.pattern.adapter.LegacyBookAdapter;
import com.ebookmanagement.pattern.adapter.LegacyBookRecord;
import com.ebookmanagement.pattern.singleton.AuditLogger;
import com.ebookmanagement.service.BookService;
import com.ebookmanagement.service.CategoryService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ============================================================
 * DESIGN PATTERN: FACADE
 * ============================================================
 * Provides ONE simplified entry point for the common "manage
 * books" operations that otherwise touch several services
 * (books, categories) and helpers (audit logging, importing
 * legacy data).
 *
 * The admin controller talks only to this facade instead of
 * juggling multiple services, which keeps the controller thin
 * (and reduces coupling — supporting the Dependency Inversion
 * and Single Responsibility ideas).
 * ============================================================
 */
@Component
public class BookManagementFacade {

    private final BookService bookService;
    private final CategoryService categoryService;

    public BookManagementFacade(BookService bookService,
                                CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    // ---- simple pass-through reads ----

    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    public Book getBook(Long id) {
        return bookService.findById(id);
    }

    // ---- operations that combine steps behind one call ----

    /** Save a new book and record the action in the audit log. */
    public Book addBook(BookDto dto) {
        Book saved = bookService.save(dto);
        AuditLogger.getInstance().log("Admin added book: " + saved.getTitle());
        return saved;
    }

    /** Update an existing book and record it. */
    public Book updateBook(Long id, BookDto dto) {
        Book updated = bookService.update(id, dto);
        AuditLogger.getInstance().log("Admin updated book #" + id);
        return updated;
    }

    /** Delete a book and record it. */
    public void deleteBook(Long id) {
        bookService.deleteById(id);
        AuditLogger.getInstance().log("Admin deleted book #" + id);
    }

    /**
     * Imports a book that arrived in the OLD legacy format.
     * Demonstrates the Facade USING the Adapter pattern internally:
     * the caller just hands over a legacy record and a category.
     */
    public Book importLegacyBook(LegacyBookRecord legacyRecord, Long categoryId) {
        BookDto dto = new LegacyBookAdapter(legacyRecord).toBookDto(categoryId);
        Book saved = bookService.save(dto);
        AuditLogger.getInstance().log("Imported legacy book: " + saved.getTitle());
        return saved;
    }
}
