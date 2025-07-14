package com.aloha.spring.rest_mvc.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.spring.rest_mvc.model.Customer;
import com.aloha.spring.rest_mvc.service.CustomerService;
import com.aloha.spring.rest_mvc.service.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public List<Customer> listCustomers() {
        return service.listCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable(name = "id", required = true) UUID id) {
        return service.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<Void> postMethodName(@RequestBody Customer customer) {
        Customer created = service.createCustomer(customer);
        return ResponseEntity.created(URI.create("/api/v1/customers/" + created.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable UUID id, @RequestBody Customer customer) {
        try {
            service.update(id, customer);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(@PathVariable UUID id, @RequestBody Customer customer) {
        try {
            service.path(id, customer);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
