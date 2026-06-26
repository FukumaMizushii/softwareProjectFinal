package com.ebookmanagement.pattern.singleton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class AuditLogger {


    private static AuditLogger instance;


    private final List<String> entries = new ArrayList<>();


    private AuditLogger() {
    }


    public static synchronized AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }


    public void log(String action) {
        String line = LocalDateTime.now() + "  ->  " + action;
        entries.add(line);

        System.out.println("[AUDIT] " + line);
    }

    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
