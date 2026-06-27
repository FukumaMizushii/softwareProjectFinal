package com.ebookmanagement.pattern.command;

import com.ebookmanagement.pattern.singleton.AuditLogger;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================
 * DESIGN PATTERN: COMMAND (the invoker)
 * ============================================================
 * The invoker runs commands without knowing their details, and
 * keeps a history stack so the last action can be undone.
 *
 * It also uses the SINGLETON AuditLogger to record each action,
 * showing two patterns cooperating.
 *
 * Note: history is kept per-invoker. Because this is a Spring
 * singleton bean, it provides a simple app-level "undo last
 * collection change" for demonstration purposes.
 * ============================================================
 */
@Component
public class CollectionCommandInvoker {

    // Stack of executed commands; the top is the most recent.
    private final Deque<Command> history = new ArrayDeque<>();

    /** Runs a command and remembers it so it can be undone later. */
    public void run(Command command) {
        command.execute();
        history.push(command);
        AuditLogger.getInstance().log(command.describe());
    }

    /** Undoes the most recent command, if any. Returns true if something was undone. */
    public boolean undoLast() {
        if (history.isEmpty()) {
            return false;
        }
        Command last = history.pop();
        last.undo();
        AuditLogger.getInstance().log("UNDO: " + last.describe());
        return true;
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}
