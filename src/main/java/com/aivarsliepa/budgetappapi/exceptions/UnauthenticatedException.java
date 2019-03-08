package com.aivarsliepa.budgetappapi.exceptions;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException(String cause) {
        super(cause);
    }
}
