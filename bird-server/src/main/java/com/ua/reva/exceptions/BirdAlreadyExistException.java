package com.ua.reva.exceptions;

/**
 * Bird already exist exception
 */
public class BirdAlreadyExistException extends BirdException  {
    public BirdAlreadyExistException(String message) {
        super(message);
    }
}
