package com.ebookmanagement.pattern.singleton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 * DESIGN PATTERN: SINGLETON
 * ============================================================
 * Ensures that only ONE AuditLogger object ever exists in the
 * whole application, giving us a single shared place to record
 * important admin actions (add/delete book, etc.).
 *
 * Why Singleton here:
 *  - There should be exactly one audit log for the running app.
 *  - All parts of the system write to the same log instance.
 *
 * How the Singleton is enforced:
 *  - private constructor (nobody else can call "new AuditLogger()")
 *  - a single private static instance
 *  - a public static getInstance() that always returns that one instance
 *  - getInstance() is synchronized to stay safe with multiple users/threads
 * ============================================================
 */
public final class AuditLogger {

    // The single, shared instance (the "one and only" object).
    private static AuditLogger instance;

    // In-memory list of log entries (newest actions appended at the end).
    private final List<String> entries = new ArrayList<>();

    // Private constructor: prevents creating instances from outside.
    private AuditLogger() {
    }

    /**
     * The global access point. Always returns the same instance.
     * synchronized makes lazy initialization thread-safe.
     */
    public static synchronized AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }

    /** Records a single action with a timestamp. */
    public void log(String action) {
        String line = LocalDateTime.now() + "  ->  " + action;
        entries.add(line);
        // Also echo to the console so it is visible while running.
        System.out.println("[AUDIT] " + line);
    }

    /** Returns a read-only view of all recorded actions. */
    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
