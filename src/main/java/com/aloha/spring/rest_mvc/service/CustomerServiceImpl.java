package com.aloha.spring.rest_mvc.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aloha.spring.rest_mvc.model.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> repo;

    public CustomerServiceImpl() {
        repo = new HashMap<>();

        Customer c1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Peter Parker")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer c2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Bruce Wayne")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer c3 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Clark Kent")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        repo.put(c1.getId(), c1);
        repo.put(c2.getId(), c2);
        repo.put(c3.getId(), c3);
    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return repo.get(id);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer created = Customer.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        repo.put(created.getId(), created);
        return created;
    }

    @Override
    public void update(UUID id, Customer customer) throws EntityNotFoundException {
        Customer existing = repo.get(id);
        if (existing == null) {
            throw new EntityNotFoundException(Customer.class.getSimpleName(), id.toString());
        }
        existing.setName(customer.getName());
        existing.setVersion(existing.getVersion() + 1);
        existing.setLastModifiedDate(LocalDateTime.now());
    }

    @Override
    public void delete(UUID id) {
        repo.remove(id);
    }

    @Override
    public void path(UUID id, Customer customer) throws EntityNotFoundException {
        Customer existing = repo.get(id);
        if (existing == null) {
            throw new EntityNotFoundException(Customer.class.getSimpleName(), id.toString());
        }
        if (StringUtils.hasText(customer.getName())) {
            existing.setName(customer.getName());
        }
        existing.setVersion(existing.getVersion() + 1);
        existing.setLastModifiedDate(LocalDateTime.now());
    }

}
