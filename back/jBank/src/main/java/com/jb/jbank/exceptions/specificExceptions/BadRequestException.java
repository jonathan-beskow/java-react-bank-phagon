package com.jb.jbank.exceptions.specificExceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String error) {
        super(error);
    }
}
