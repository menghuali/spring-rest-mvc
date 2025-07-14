package com.aloha.spring.rest_mvc.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeerControllerTests {

    @Autowired
    private BeerController controller;

    @Test
    public void testGetBeerById() {
        assertNotNull(controller.getBeerById(UUID.randomUUID()));
    }

}
