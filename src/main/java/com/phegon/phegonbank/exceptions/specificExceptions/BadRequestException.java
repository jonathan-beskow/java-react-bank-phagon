package com.phegon.phegonbank.exceptions.specificExceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String error) {
        super(error);
    }
}
