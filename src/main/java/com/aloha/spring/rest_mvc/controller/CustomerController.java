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
import com.aloha.spring.rest_mvc.service.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping(CustomerController.ROOT_PATH)
@RestController
public class CustomerController {

    public static final String ROOT_PATH = "/api/v1/customers";
    public static final String SUB_PATH_WITH_ID_VAR = "/{id}";
    private final CustomerService service;

    @GetMapping
    public List<Customer> listCustomers() {
        return service.listCustomers();
    }

    @GetMapping(SUB_PATH_WITH_ID_VAR)
    public Customer getCustomerById(@PathVariable UUID id) throws ResourceNotFoundException {
        Customer customer = service.getCustomerById(id);
        if (customer == null) {
            throw new ResourceNotFoundException(Customer.class.getSimpleName(), id.toString());
        } else {
            return customer;
        }
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer) {
        Customer created = service.createCustomer(customer);
        return ResponseEntity.created(URI.create(ROOT_PATH + "/" + created.getId())).build();
    }

    @PutMapping(SUB_PATH_WITH_ID_VAR)
    public ResponseEntity<Void> updateCustomer(@PathVariable UUID id, @RequestBody Customer customer) {
        try {
            service.update(id, customer);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(SUB_PATH_WITH_ID_VAR)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PatchMapping(SUB_PATH_WITH_ID_VAR)
    public ResponseEntity<Void> patch(@PathVariable UUID id, @RequestBody Customer customer) {
        try {
            service.patch(id, customer);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
