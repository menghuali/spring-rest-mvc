package com.aloha.spring.rest_mvc.service;

import java.util.List;
import java.util.UUID;

import com.aloha.spring.rest_mvc.model.Customer;

public interface CustomerService {

    List<Customer> listCustomers();

    Customer getCustomerById(UUID id) throws ResourceNotFoundException;

    Customer createCustomer(Customer customer);

    void update(UUID id, Customer customer) throws ResourceNotFoundException;

    void delete(UUID id);

    void patch(UUID id, Customer customer) throws ResourceNotFoundException;

}
