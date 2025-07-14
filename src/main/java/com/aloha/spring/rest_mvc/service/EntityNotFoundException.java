package com.aloha.spring.rest_mvc.service;

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String type, String id) {
        super(String.format("Did not find entity %s with id %s", type, id));
    }

}
