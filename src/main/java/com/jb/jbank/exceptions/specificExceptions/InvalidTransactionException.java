package com.jb.jbank.exceptions.specificExceptions;

public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String error) {
        super(error);
    }
}