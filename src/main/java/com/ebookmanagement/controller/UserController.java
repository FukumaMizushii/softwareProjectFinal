package com.ebookmanagement.controller;

import com.ebookmanagement.pattern.command.AddToCollectionCommand;
import com.ebookmanagement.pattern.command.CollectionCommandInvoker;
import com.ebookmanagement.pattern.command.RemoveFromCollectionCommand;
import com.ebookmanagement.service.CollectionService;
import com.ebookmanagement.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Logged-in reader features: dashboard and personal collection management.
 *
 * Collection changes go through the COMMAND pattern: each add/remove is
 * wrapped in a Command and run by the invoker, which records history and
 * supports "undo last change".
 */
@Controller
@RequestMapping
public class UserController {

    private final UserService userService;
    private final CollectionService collectionService;
    private final CollectionCommandInvoker commandInvoker;

    public UserController(UserService userService,
                          CollectionService collectionService,
                          CollectionCommandInvoker commandInvoker) {
        this.userService = userService;
        this.collectionService = collectionService;
        this.commandInvoker = commandInvoker;
    }

    @GetMapping("/user/dashboard")
    public String dashboard(Authentication auth, Model model) {
        model.addAttribute("currentUser", userService.findByEmail(auth.getName()));
        model.addAttribute("collectionCount",
                collectionService.getUserCollection(auth.getName()).size());
        model.addAttribute("canUndo", commandInvoker.hasHistory(auth.getName()));
        return "user/dashboard";
    }

    @GetMapping("/collection")
    public String myCollection(Authentication auth, Model model) {
        model.addAttribute("books", collectionService.getUserCollection(auth.getName()));
        model.addAttribute("canUndo", commandInvoker.hasHistory(auth.getName()));
        return "user/collection";
    }

    @PostMapping("/collection/add/{bookId}")
    public String addToCollection(@PathVariable Long bookId, Authentication auth) {
        // COMMAND pattern: wrap the action, then let the invoker run it.
        commandInvoker.run(auth.getName(),
                new AddToCollectionCommand(collectionService, auth.getName(), bookId));
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/collection/remove/{bookId}")
    public String removeFromCollection(@PathVariable Long bookId, Authentication auth) {
        commandInvoker.run(auth.getName(),
                new RemoveFromCollectionCommand(collectionService, auth.getName(), bookId));
        return "redirect:/collection";
    }

    /** Undo the most recent collection change (COMMAND pattern undo). */
    @PostMapping("/collection/undo")
    public String undoLast(Authentication auth) {
        commandInvoker.undoLast(auth.getName());
        return "redirect:/collection";
    }
}
