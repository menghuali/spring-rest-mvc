package com.aloha.spring.rest_mvc.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String type, String id) {
        super(String.format("Did not find entity %s with id %s", type, id));
    }

}
