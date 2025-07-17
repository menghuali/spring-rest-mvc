package com.aloha.spring.rest_mvc.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.spring.rest_mvc.model.Beer;
import com.aloha.spring.rest_mvc.service.BeerService;
import com.aloha.spring.rest_mvc.service.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(BeerController.ROOT_PATH)
@RestController
public class BeerController {

    public static final String ROOT_PATH = "/api/v1/beers";
    public static final String SUB_PATH_WITH_ID_VAR = "/{id}";
    private final BeerService service;

    @GetMapping(SUB_PATH_WITH_ID_VAR)
    public Beer getBeerById(@PathVariable UUID id) throws ResourceNotFoundException {
        log.debug("Get beer by id in contoller. id: {}", id);
        Beer beer = service.getBeerById(id);
        if (beer == null) {
            throw new ResourceNotFoundException(Beer.class.getSimpleName(), id.toString());
        } else {
            return beer;
        }
    }

    @GetMapping
    public List<Beer> listBeers() {
        return service.listBeers();
    }

    @PostMapping
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer created = service.create(beer);
        // HttpHeaders headers = new HttpHeaders();
        // headers.add(HttpHeaders.LOCATION, "/api/v1/beers/" + created.getId());
        // return new ResponseEntity<>(created, headers, HttpStatus.CREATED);

        // A more concise implementation ...
        return ResponseEntity.created(URI.create(ROOT_PATH + "/" + created.getId())).build();
    }

    @PutMapping(SUB_PATH_WITH_ID_VAR)
    public ResponseEntity<Void> updateById(@PathVariable UUID id, @RequestBody Beer beer) {
        try {
            service.updateById(id, beer);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) // Without using ResponseEntity
    @DeleteMapping(SUB_PATH_WITH_ID_VAR)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PatchMapping(SUB_PATH_WITH_ID_VAR)
    public ResponseEntity<Void> patch(@PathVariable UUID id, @RequestBody Beer beer) {
        try {
            service.patch(id, beer);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Exception handler for specific controller
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
