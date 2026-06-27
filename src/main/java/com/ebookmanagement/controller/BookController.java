package com.ebookmanagement.controller;

import com.ebookmanagement.entity.Book;
import com.ebookmanagement.service.BookService;
import com.ebookmanagement.service.CategoryService;
import com.ebookmanagement.service.CollectionService;
import com.ebookmanagement.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Public + user facing book browsing, plus secured read/download endpoints.
 */
@Controller
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final CollectionService collectionService;
    private final FileStorageService fileStorageService;

    public BookController(BookService bookService,
                          CategoryService categoryService,
                          CollectionService collectionService,
                          FileStorageService fileStorageService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.collectionService = collectionService;
        this.fileStorageService = fileStorageService;
    }

    /** Home page: show all books and categories. */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "index";
    }

    /** Full book list, optionally filtered by category. */
    @GetMapping("/books")
    public String listBooks(@RequestParam(value = "categoryId", required = false) Long categoryId,
                            Model model) {
        List<Book> books = (categoryId != null)
                ? bookService.findByCategory(categoryId)
                : bookService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("categories", categoryService.findAll());
        return "books/list";
    }

    /** Search by title or author. */
    @GetMapping("/books/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("books", bookService.search(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categoryService.findAll());
        return "books/list";
    }

    /** Book details page. */
    @GetMapping("/books/{id}")
    public String bookDetails(@PathVariable Long id, Model model, Authentication auth) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        // If logged in, tell the view whether the book is already collected.
        boolean inCollection = false;
        if (auth != null && auth.isAuthenticated()
                && !auth.getName().equals("anonymousUser")) {
            inCollection = collectionService.isInCollection(auth.getName(), id);
        }
        model.addAttribute("inCollection", inCollection);
        return "books/details";
    }

    /** Read the PDF online (inline in the browser). Requires login. */
    @GetMapping("/books/read/{id}")
    public ResponseEntity<Resource> readBook(@PathVariable Long id) {
        Book book = bookService.findById(id);
        Resource resource = fileStorageService.loadAsResource(book.getFilePath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + book.getTitle() + ".pdf\"")
                .body(resource);
    }

    /** Download the book file as an attachment. Requires login. */
    @GetMapping("/books/download/{id}")
    public ResponseEntity<Resource> downloadBook(@PathVariable Long id) {
        Book book = bookService.findById(id);
        Resource resource = fileStorageService.loadAsResource(book.getFilePath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + book.getTitle() + ".pdf\"")
                .body(resource);
    }
}
