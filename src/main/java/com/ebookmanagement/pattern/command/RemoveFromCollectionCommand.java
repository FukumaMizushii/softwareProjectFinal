package com.ebookmanagement.pattern.command;

import com.ebookmanagement.service.CollectionService;


public class RemoveFromCollectionCommand implements Command {

    private final CollectionService collectionService; // the Receiver
    private final String userEmail;
    private final Long bookId;

    public RemoveFromCollectionCommand(CollectionService collectionService,
                                       String userEmail, Long bookId) {
        this.collectionService = collectionService;
        this.userEmail = userEmail;
        this.bookId = bookId;
    }

    @Override
    public void execute() {
        collectionService.removeFromCollection(userEmail, bookId);
    }

    @Override
    public void undo() {
        collectionService.addToCollection(userEmail, bookId);
    }

    @Override
    public String describe() {
        return "Remove book #" + bookId + " from collection of " + userEmail;
    }
}
