package com.example.meal2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class GenericForbiddenException extends RuntimeException {
    public GenericForbiddenException(String message){super(message);}
    public GenericForbiddenException(String message, Throwable cause){super(message, cause);}
}
