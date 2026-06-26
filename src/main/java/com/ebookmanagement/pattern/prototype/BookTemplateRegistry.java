package com.ebookmanagement.pattern.prototype;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class BookTemplateRegistry {

    private final Map<String, BookTemplate> prototypes = new LinkedHashMap<>();

    public BookTemplateRegistry() {

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


    public BookTemplate getClone(String key) {
        BookTemplate prototype = prototypes.get(key);
        if (prototype == null) {
            return null;
        }
        return prototype.copy();
    }


    public Map<String, BookTemplate> getAll() {
        return prototypes;
    }
}
