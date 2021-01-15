package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);


        Item item = new Item();
        item.setId(1L);
        item.setName("test");
        item.setPrice(BigDecimal.valueOf(1L));

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByName("test")).thenReturn(items);
        when(itemRepository.findAll()).thenReturn(items);
    }

    @Test
    public void getItems() {
        final ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Item> items = responseEntity.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemsByName() {
        final ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("test");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Item> items = responseEntity.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemById() {
        final ResponseEntity<Item> responseEntity = itemController.getItemById(1L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Item item = responseEntity.getBody();

        assertNotNull(item);
        assertEquals("test", item.getName());
    }
}
