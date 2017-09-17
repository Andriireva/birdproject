package com.ua.reva.cli.exceptions;

/**
 * Generic exception for CLI application
 */
public class BirdCliException extends RuntimeException {
    public BirdCliException(String message) {
        super(message);
    }

    BirdCliException(String message, Throwable cause) {
        super(message, cause);
    }
}
