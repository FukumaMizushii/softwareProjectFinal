package com.ebookmanagement.pattern.command;

import com.ebookmanagement.pattern.singleton.AuditLogger;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;


@Component
public class CollectionCommandInvoker {


    private final Deque<Command> history = new ArrayDeque<>();


    public void run(Command command) {
        command.execute();
        history.push(command);
        AuditLogger.getInstance().log(command.describe());
    }


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
