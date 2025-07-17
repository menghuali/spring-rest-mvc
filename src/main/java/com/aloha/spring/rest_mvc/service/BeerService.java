package com.aloha.spring.rest_mvc.service;

import java.util.List;
import java.util.UUID;

import com.aloha.spring.rest_mvc.model.Beer;

public interface BeerService {

    Beer getBeerById(UUID id) throws ResourceNotFoundException;

    List<Beer> listBeers();

    Beer create(Beer beer);

    void updateById(UUID id, Beer beer) throws ResourceNotFoundException;

    void delete(UUID id);

    void patch(UUID id, Beer beer) throws ResourceNotFoundException;

}
