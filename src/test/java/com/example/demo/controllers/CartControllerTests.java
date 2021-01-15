package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTests {

    private CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final String testUserName = "testUserName";

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);

        Cart cart = new Cart();

        User userToReturn =  new User();
        userToReturn.setId(1L);
        userToReturn.setUsername(testUserName);
        userToReturn.setCart(cart);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToReturn));
        when(userRepository.findByUsername(testUserName)).thenReturn(userToReturn);


        when(cartRepository.save(cart)).thenReturn(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("test");
        item.setPrice(BigDecimal.valueOf(1L));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addToCart() {
        ModifyCartRequest request = createModifyCartRequest();
        final ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);

        assertNotNull(cartResponseEntity);
        assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
        Cart cart = cartResponseEntity.getBody();

        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
    }

    @Test()
    public void removeFromCart() {
        ModifyCartRequest request = createModifyCartRequest();
        final ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(request);

        assertNotNull(cartResponseEntity);
        assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
        Cart cart = cartResponseEntity.getBody();

        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
    }

    private ModifyCartRequest createModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(testUserName);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);
        return modifyCartRequest;
    }
}
