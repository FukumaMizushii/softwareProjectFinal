package com.ebookmanagement.controller;

import com.ebookmanagement.dto.BookDto;
import com.ebookmanagement.entity.Book;
import com.ebookmanagement.pattern.adapter.LegacyBookRecord;
import com.ebookmanagement.pattern.facade.BookManagementFacade;
import com.ebookmanagement.pattern.prototype.BookTemplate;
import com.ebookmanagement.pattern.prototype.BookTemplateRegistry;
import com.ebookmanagement.service.CategoryService;
import com.ebookmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Admin dashboard + full CRUD for books. All routes under /admin
 * require the ADMIN role (enforced in SecurityConfig).
 *
 * This controller is intentionally THIN: it delegates the real work to
 * the FACADE (BookManagementFacade), which hides the book/category/audit
 * services behind one simple interface. It also demonstrates the
 * PROTOTYPE pattern (clone a template) and the ADAPTER pattern
 * (import a legacy-format book).
 */
@Controller
@RequestMapping("/admin")
public class AdminBookController {

    private final BookManagementFacade bookFacade;     // FACADE
    private final CategoryService categoryService;
    private final UserService userService;
    private final BookTemplateRegistry templateRegistry; // PROTOTYPE registry

    public AdminBookController(BookManagementFacade bookFacade,
                              CategoryService categoryService,
                              UserService userService,
                              BookTemplateRegistry templateRegistry) {
        this.bookFacade = bookFacade;
        this.categoryService = categoryService;
        this.userService = userService;
        this.templateRegistry = templateRegistry;
    }

    /** Admin dashboard with quick counts. */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("bookCount", bookFacade.getAllBooks().size());
        model.addAttribute("categoryCount", categoryService.findAll().size());
        model.addAttribute("userCount", userService.findAll().size());
        return "admin/dashboard";
    }

    /** List all books. */
    @GetMapping("/books")
    public String manageBooks(Model model) {
        model.addAttribute("books", bookFacade.getAllBooks());
        model.addAttribute("templates", templateRegistry.getAll());
        return "admin/books";
    }

    /** Show "add book" form. */
    @GetMapping("/books/add")
    public String addBookForm(Model model) {
        model.addAttribute("bookDto", new BookDto());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/book-form";
    }

    /** Handle "add book" submission. */
    @PostMapping("/books/add")
    public String addBook(@Valid @ModelAttribute("bookDto") BookDto bookDto,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/book-form";
        }
        bookFacade.addBook(bookDto); // via FACADE (also writes audit log)
        return "redirect:/admin/books";
    }

    /**
     * PROTOTYPE pattern in action: start a new book form pre-filled by
     * CLONING a master template, instead of typing the same fields again.
     */
    @GetMapping("/books/add-from-template/{key}")
    public String addFromTemplate(@PathVariable String key, Model model) {
        BookTemplate clone = templateRegistry.getClone(key);
        BookDto dto = new BookDto();
        if (clone != null) {
            dto.setTitle(clone.getTitle());
            dto.setAuthor(clone.getAuthor());
            dto.setDescription(clone.getDescription());
            dto.setPrice(clone.getPrice());
            dto.setCategoryId(clone.getCategoryId());
        }
        model.addAttribute("bookDto", dto);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/book-form";
    }

    /**
     * ADAPTER pattern in action: import a hard-coded sample book that is
     * in the OLD legacy format. The facade uses the adapter internally to
     * convert it into our BookDto, then saves it.
     */
    @PostMapping("/books/import-legacy")
    public String importLegacySample() {
        // Pretend this record came from an old external catalog (price in cents).
        LegacyBookRecord legacy = new LegacyBookRecord(
                "The Old Classic", "A. Legacy Writer", 1299,
                "A sample book imported from the legacy catalog format.");
        // categoryId 1 = Fiction (from the seeded categories)
        bookFacade.importLegacyBook(legacy, 1L);
        return "redirect:/admin/books";
    }

    /** Show "edit book" form pre-filled with existing values. */
    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookFacade.getBook(id);
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setCategoryId(book.getCategory() != null ? book.getCategory().getId() : null);
        dto.setExistingFilePath(book.getFilePath());
        dto.setExistingCoverImagePath(book.getCoverImagePath());

        model.addAttribute("bookDto", dto);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("editMode", true);
        return "admin/book-form";
    }

    /** Handle "edit book" submission. */
    @PostMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id,
                           @Valid @ModelAttribute("bookDto") BookDto bookDto,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("editMode", true);
            return "admin/book-form";
        }
        bookFacade.updateBook(id, bookDto); // via FACADE
        return "redirect:/admin/books";
    }

    /** Delete a book. */
    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookFacade.deleteBook(id); // via FACADE
        return "redirect:/admin/books";
    }
}
