package com.shareDiscount.controller.exception;

public class ResourceAlreadyExistException extends IllegalArgumentException{

    public ResourceAlreadyExistException(String resource) {
        super(String.format("Resource %s already exist", resource));
    }
}