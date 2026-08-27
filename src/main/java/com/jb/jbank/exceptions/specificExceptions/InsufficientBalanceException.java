package com.jb.jbank.exceptions.specificExceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String error) {
        super(error);
    }
}