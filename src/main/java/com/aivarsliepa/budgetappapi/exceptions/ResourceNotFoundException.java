package com.aivarsliepa.budgetappapi.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String cause) {
        super(cause);
    }
}
