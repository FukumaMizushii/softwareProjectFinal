package com.ebookmanagement.pattern.command;

/**
 * ============================================================
 * DESIGN PATTERN: COMMAND (the command interface)
 * ============================================================
 * Wraps an action as an object so it can be executed, undone,
 * stored, or queued. Every concrete command knows how to do
 * its action (execute) and how to reverse it (undo).
 * ============================================================
 */
public interface Command {

    /** Carry out the action. */
    void execute();

    /** Reverse the action (so we can support undo). */
    void undo();

    /** A short human-readable description, useful for logging/history. */
    String describe();
}
