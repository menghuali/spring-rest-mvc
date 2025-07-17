package com.aloha.spring.rest_mvc.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aloha.spring.rest_mvc.model.Customer;
import com.aloha.spring.rest_mvc.service.CustomerService;
import com.aloha.spring.rest_mvc.service.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTests {

        private final static String PATH_WITH_ID = CustomerController.ROOT_PATH
                        + CustomerController.SUB_PATH_WITH_ID_VAR;

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objMapper;

        @MockitoBean
        private CustomerService service;

        @Captor
        private ArgumentCaptor<UUID> idCaptor;

        @Captor
        private ArgumentCaptor<Customer> customerCaptor;

        @Test
        public void listCustomers() throws Exception {
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
                List<Customer> customers = new ArrayList<>();
                customers.add(c1);
                customers.add(c2);
                customers.add(c3);
                when(service.listCustomers()).thenReturn(customers);

                mvc.perform(get(CustomerController.ROOT_PATH).accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()", is(3)));
        }

        @Test
        public void getCustomerById() throws Exception {
                UUID id = UUID.randomUUID();
                Customer c1 = Customer.builder()
                                .id(id)
                                .name("Peter Parker")
                                .version(1)
                                .createdDate(LocalDateTime.now())
                                .lastModifiedDate(LocalDateTime.now())
                                .build();
                when(service.getCustomerById(id)).thenReturn(c1);

                mvc.perform(get(PATH_WITH_ID, id).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id", is(id.toString())));
        }

        @Test
        public void getCustomerByIdNotFound() throws Exception {
                UUID id = UUID.randomUUID();
                when(service.getCustomerById(id))
                                .thenThrow(new ResourceNotFoundException(Customer.class.getSimpleName(), id.toString()));
                mvc.perform(get(PATH_WITH_ID, id).accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
        }

        @Test
        public void createCustomer() throws JsonProcessingException, Exception {
                Customer customer = newCustomer();
                UUID id = UUID.randomUUID();

                when(service.createCustomer(customer)).thenAnswer(args -> {
                        Customer c = args.getArgument(0, Customer.class);
                        c.setId(id);
                        return c;
                });

                mvc.perform(post(CustomerController.ROOT_PATH).contentType(MediaType.APPLICATION_JSON)
                                .content(objMapper.writeValueAsString(customer)))
                                .andExpect(status().isCreated())
                                .andExpect(header().exists(HttpHeaders.LOCATION))
                                .andExpect(header().string(HttpHeaders.LOCATION,
                                                String.format(CustomerController.ROOT_PATH + "/%s", id)));
        }

        @Test
        public void update() throws JsonProcessingException, Exception {
                Customer customer = newCustomer();
                UUID id = UUID.randomUUID();

                mvc.perform(put(PATH_WITH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objMapper.writeValueAsString(customer)))
                                .andExpect(status().isNoContent());
                verify(service).update(id, customer);
        }

        private Customer newCustomer() {
                Customer customer = Customer.builder()
                                .name("Peter Parker")
                                .build();
                return customer;
        }

        @Test
        void testDelete() throws Exception {
                UUID id = UUID.randomUUID();
                mvc.perform(delete(PATH_WITH_ID, id))
                                .andExpect(status().isNoContent());
                verify(service).delete(id);
        }

        @Test
        public void testPatch() throws Exception {
                UUID id = UUID.randomUUID();
                Customer customer = newCustomer();
                mvc.perform(patch(PATH_WITH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objMapper.writeValueAsString(customer)))
                                .andExpect(status().isNoContent());
                verify(service).patch(idCaptor.capture(), customerCaptor.capture());
                assertEquals(id, idCaptor.getValue());
                assertEquals(customer.getName(), customerCaptor.getValue().getName());
        }

}
