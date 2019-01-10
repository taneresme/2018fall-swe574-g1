package com.boun.swe.mnemosyne.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemoryNotFoundException extends RuntimeException {

    public MemoryNotFoundException(String message) {
        super(message);
    }
}
