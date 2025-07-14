package com.aloha.spring.rest_mvc.service;

import java.util.List;
import java.util.UUID;

import com.aloha.spring.rest_mvc.model.Beer;

public interface BeerService {

    Beer getBeerById(UUID id);

    List<Beer> listBeers();

    Beer create(Beer beer);

    void updateById(UUID id, Beer beer) throws EntityNotFoundException;

    void delete(UUID id);

    void path(UUID id, Beer beer) throws EntityNotFoundException;

}
