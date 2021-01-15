package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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

public class OrderControllerTests {

    private OrderController orderController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("test");
        item.setPrice(BigDecimal.valueOf(1L));

        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);

        User userToReturn =  new User();
        userToReturn.setId(1L);
        userToReturn.setUsername("testUserName");
        userToReturn.setCart(cart);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToReturn));
        when(userRepository.findByUsername("testUserName")).thenReturn(userToReturn);


        UserOrder order = new UserOrder();

        List<UserOrder> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findByUser(userToReturn)).thenReturn(orders);
        when(orderRepository.save(order)).thenReturn(order);
    }

    @Test
    public void getOrdersForUser() {
        final ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("testUserName");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final List<UserOrder> userOrders = responseEntity.getBody();

        assertNotNull(userOrders);
        assertEquals(1, userOrders.size());
    }

    @Test
    public void submitOrder() {
        final ResponseEntity<UserOrder> responseEntity = orderController.submit("testUserName");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final UserOrder userOrder = responseEntity.getBody();

        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().size());
    }
}
