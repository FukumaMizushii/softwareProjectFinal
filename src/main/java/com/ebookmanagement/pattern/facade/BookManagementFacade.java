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


@Component
public class BookManagementFacade {

    private final BookService bookService;
    private final CategoryService categoryService;

    public BookManagementFacade(BookService bookService,
                                CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }



    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    public Book getBook(Long id) {
        return bookService.findById(id);
    }


    public Book addBook(BookDto dto) {
        Book saved = bookService.save(dto);
        AuditLogger.getInstance().log("Admin added book: " + saved.getTitle());
        return saved;
    }


    public Book updateBook(Long id, BookDto dto) {
        Book updated = bookService.update(id, dto);
        AuditLogger.getInstance().log("Admin updated book #" + id);
        return updated;
    }


    public void deleteBook(Long id) {
        bookService.deleteById(id);
        AuditLogger.getInstance().log("Admin deleted book #" + id);
    }


    public Book importLegacyBook(LegacyBookRecord legacyRecord, Long categoryId) {
        BookDto dto = new LegacyBookAdapter(legacyRecord).toBookDto(categoryId);
        Book saved = bookService.save(dto);
        AuditLogger.getInstance().log("Imported legacy book: " + saved.getTitle());
        return saved;
    }
}
