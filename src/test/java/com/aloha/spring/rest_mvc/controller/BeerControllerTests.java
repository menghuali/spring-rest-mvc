package com.aloha.spring.rest_mvc.controller;

import static com.aloha.spring.rest_mvc.controller.BeerController.ROOT_PATH;
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

import java.math.BigDecimal;
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

import com.aloha.spring.rest_mvc.model.Beer;
import com.aloha.spring.rest_mvc.model.BeerStyle;
import com.aloha.spring.rest_mvc.service.BeerService;
import com.aloha.spring.rest_mvc.service.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = BeerController.class)
public class BeerControllerTests {

        private static final String PATH_WITH_ID = BeerController.ROOT_PATH + BeerController.SUB_PATH_WITH_ID_VAR;

        @Autowired
        private MockMvc mvc;

        @MockitoBean
        private BeerService service;

        @Autowired
        private ObjectMapper objMapper;

        @Captor
        private ArgumentCaptor<Beer> beerCaptor;

        @Captor
        private ArgumentCaptor<UUID> idCaptor;

        private Beer newBeer() {
                Beer beer = Beer.builder()
                                .beerName("Galaxy Cat")
                                .beerStyle(BeerStyle.PALE_ALE)
                                .upc("123456")
                                .price(BigDecimal.valueOf(12.99))
                                .quantityOnHand(122)
                                .build();
                return beer;
        }

        @Test
        public void getBeerById() throws Exception {
                UUID beerId = UUID.randomUUID();
                Beer beer = Beer.builder()
                                .id(beerId)
                                .version(1)
                                .beerName("Galaxy Cat")
                                .beerStyle(BeerStyle.PALE_ALE)
                                .upc("123456")
                                .price(BigDecimal.valueOf(12.99))
                                .quantityOnHand(122)
                                .createdDate(LocalDateTime.now())
                                .updatedDate(LocalDateTime.now())
                                .build();
                when(service.getBeerById(beerId)).thenReturn(beer);

                mvc.perform(get(PATH_WITH_ID, beerId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id", is(beerId.toString())))
                                .andExpect(jsonPath("$.beerName", is(beer.getBeerName())));
        }

        @Test
        public void getBeerByIdNotFound() throws Exception {
                UUID id = UUID.randomUUID();
                when(service.getBeerById(id))
                                .thenThrow(new ResourceNotFoundException(Beer.class.getSimpleName(), id.toString()));
                mvc.perform(get(PATH_WITH_ID, id).accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
        }

        @Test
        public void listBeers() throws Exception {
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
                List<Beer> beers = new ArrayList<>();
                beers.add(beer1);
                beers.add(beer2);
                beers.add(beer3);

                when(service.listBeers()).thenReturn(beers);

                mvc.perform(get(BeerController.ROOT_PATH).accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()", is(3)));
        }

        @Test
        public void createBeer() throws Exception {
                Beer beer = newBeer();
                String json = objMapper.writeValueAsString(beer);
                UUID id = UUID.randomUUID();
                when(service.create(beer)).thenAnswer(args -> {
                        Beer b = args.getArgument(0, Beer.class);
                        b.setId(id);
                        return b;
                });

                mvc.perform(post(BeerController.ROOT_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                                .andExpect(status().isCreated())
                                .andExpect(header().exists(HttpHeaders.LOCATION))
                                .andExpect(header().string(HttpHeaders.LOCATION,
                                                String.format(ROOT_PATH + "/%s", id)));

        }

        @Test
        public void updateById() throws Exception {
                UUID id = UUID.randomUUID();
                Beer beer = newBeer();
                mvc.perform(put(PATH_WITH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objMapper.writeValueAsString(beer)))
                                .andExpect(status().isNoContent());
                verify(service).updateById(id, beer);
        }

        @Test
        public void testDelete() throws Exception {
                UUID id = UUID.randomUUID();
                mvc.perform(delete(PATH_WITH_ID, id))
                                .andExpect(status().isNoContent());
                verify(service).delete(id);
        }

        @Test
        public void testPatch() throws Exception {
                UUID id = UUID.randomUUID();
                Beer beer = newBeer();
                mvc.perform(patch(PATH_WITH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objMapper.writeValueAsString(beer)))
                                .andExpect(status().isNoContent());
                // Different way to verify the service inputs using arugment captors.
                verify(service).patch(idCaptor.capture(), beerCaptor.capture());
                assertEquals(id, idCaptor.getValue());
                assertEquals(beer, beerCaptor.getValue());
                assertEquals(beer.getBeerName(), beerCaptor.getValue().getBeerName());
        }

}
