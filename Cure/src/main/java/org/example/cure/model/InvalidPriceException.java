package org.example.cure.model;

public class InvalidPriceException extends Exception {
    public InvalidPriceException(String message) {
        super(message);
    }
}