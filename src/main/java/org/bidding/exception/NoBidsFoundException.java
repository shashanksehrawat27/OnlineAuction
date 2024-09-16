package org.bidding.exception;

public class NoBidsFoundException extends RuntimeException {
    public NoBidsFoundException(String message) {
        super(message);
    }
}