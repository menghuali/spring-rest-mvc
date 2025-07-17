package com.aloha.spring.rest_mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aloha.spring.rest_mvc.service.ResourceNotFoundException;

// Global exception handler
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
