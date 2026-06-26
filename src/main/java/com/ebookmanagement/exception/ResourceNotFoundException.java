package com.ebookmanagement.exception;

/** Thrown when a requested entity (book, user, category) does not exist. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
