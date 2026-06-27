package com.ebookmanagement.pattern.prototype;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ============================================================
 * DESIGN PATTERN: PROTOTYPE (registry / manager)
 * ============================================================
 * Holds the named "master" BookTemplates. When the admin wants a
 * new book that is similar to a common kind, the registry hands
 * back a CLONE of the matching prototype (never the original),
 * so the caller can safely modify the copy.
 * ============================================================
 */
@Component
public class BookTemplateRegistry {

    private final Map<String, BookTemplate> prototypes = new LinkedHashMap<>();

    public BookTemplateRegistry() {
        // A few ready-made master copies (category ids match the seeded
        // categories created by DataInitializer: 1=Fiction ... 4=Technology).
        prototypes.put("free-fiction",
                new BookTemplate("Untitled Fiction", "Unknown Author",
                        "A free fiction e-book.", 0.0, 1L));
        prototypes.put("programming-book",
                new BookTemplate("Untitled Programming Book", "Unknown Author",
                        "A programming/technology e-book.", 9.99, 4L));
        prototypes.put("history-book",
                new BookTemplate("Untitled History Book", "Unknown Author",
                        "A history e-book.", 4.99, 3L));
    }

    /** Returns a CLONE of the named prototype, or null if it does not exist. */
    public BookTemplate getClone(String key) {
        BookTemplate prototype = prototypes.get(key);
        if (prototype == null) {
            return null;
        }
        return prototype.copy(); // clone, so the master stays untouched
    }

    /** Keys available, for showing options in the UI. */
    public Map<String, BookTemplate> getAll() {
        return prototypes;
    }
}
