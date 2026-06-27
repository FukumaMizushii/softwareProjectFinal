package com.ebookmanagement.pattern.command;

import com.ebookmanagement.service.CollectionService;

/**
 * DESIGN PATTERN: COMMAND (concrete command)
 *
 * Packages the "add this book to my collection" request as an object.
 * - Receiver: CollectionService (does the real work)
 * - execute(): adds the book
 * - undo():    removes the book (reverses execute)
 */
public class AddToCollectionCommand implements Command {

    private final CollectionService collectionService; // the Receiver
    private final String userEmail;
    private final Long bookId;

    public AddToCollectionCommand(CollectionService collectionService,
                                  String userEmail, Long bookId) {
        this.collectionService = collectionService;
        this.userEmail = userEmail;
        this.bookId = bookId;
    }

    @Override
    public void execute() {
        collectionService.addToCollection(userEmail, bookId);
    }

    @Override
    public void undo() {
        collectionService.removeFromCollection(userEmail, bookId);
    }

    @Override
    public String describe() {
        return "Add book #" + bookId + " to collection of " + userEmail;
    }
}
