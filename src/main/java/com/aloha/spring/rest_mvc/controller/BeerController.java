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

import com.aloha.spring.rest_mvc.model.Beer;
import com.aloha.spring.rest_mvc.service.BeerService;
import com.aloha.spring.rest_mvc.service.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/beers")
@RestController
public class BeerController {

    private final BeerService service;

    @GetMapping("/{id}")
    public Beer getBeerById(@PathVariable(name = "id", required = true) UUID id) {
        log.debug("Get beer by id in contoller. id: {}", id);
        return service.getBeerById(id);
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
        return ResponseEntity.created(URI.create("/api/v1/beers/" + created.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable UUID id, @RequestBody Beer beer) {
        try {
            service.updateById(id, beer);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) // Without using ResponseEntity
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(@PathVariable UUID id, @RequestBody Beer beer) {
        try {
            service.path(id, beer);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
