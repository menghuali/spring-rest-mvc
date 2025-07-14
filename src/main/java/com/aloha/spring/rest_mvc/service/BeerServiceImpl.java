package com.aloha.spring.rest_mvc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aloha.spring.rest_mvc.model.Beer;
import com.aloha.spring.rest_mvc.model.BeerStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, Beer> repo;

    public BeerServiceImpl() {
        repo = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(BigDecimal.valueOf(12.99))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("1235662")
                .price(BigDecimal.valueOf(11.99))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(BigDecimal.valueOf(13.99))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        repo.put(beer1.getId(), beer1);
        repo.put(beer2.getId(), beer2);
        repo.put(beer3.getId(), beer3);
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get beer by id in service. id: {}", id);
        return repo.get(id);
    }

    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public Beer create(Beer beer) {
        Beer created = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();

        repo.put(created.getId(), created);
        return created;
    }

    @Override
    public void updateById(UUID id, Beer beer) throws EntityNotFoundException {
        Beer existing = repo.get(id);
        if (existing == null) {
            throw new EntityNotFoundException(Beer.class.getSimpleName(), id.toString());
        }
        existing.setBeerName(beer.getBeerName());
        existing.setPrice(beer.getPrice());
        existing.setBeerStyle(beer.getBeerStyle());
        existing.setUpc(beer.getUpc());
        existing.setQuantityOnHand(beer.getQuantityOnHand());
        existing.setPrice(beer.getPrice());
        existing.setVersion(existing.getVersion() + 1);
        existing.setUpdatedDate(LocalDateTime.now());
    }

    @Override
    public void delete(UUID id) {
        repo.remove(id);
    }

    @Override
    public void path(UUID id, Beer beer) throws EntityNotFoundException {
        Beer existing = repo.get(id);
        if (existing == null) {
            throw new EntityNotFoundException(Beer.class.getSimpleName(), id.toString());
        }

        if (StringUtils.hasText(beer.getBeerName())) {
            existing.setBeerName(beer.getBeerName());
        }
        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }
        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }
        if (beer.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }
        existing.setVersion(existing.getVersion() + 1);
        existing.setUpdatedDate(LocalDateTime.now());
    }

}
