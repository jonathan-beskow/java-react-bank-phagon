package com.phegon.phegonbank.exceptions.specificExceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String error) {
        super(error);
    }
}
