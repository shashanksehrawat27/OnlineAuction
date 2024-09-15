package org.bidding.exception;

public class CannotCreateDuplicateEntryException extends RuntimeException {
    public CannotCreateDuplicateEntryException(String message) {
        super(message);
    }
}
