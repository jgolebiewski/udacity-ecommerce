package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTests {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        String searchedUserName = "test";
        User userToReturn =  new User();
        userToReturn.setId(1L);
        userToReturn.setUsername(searchedUserName);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToReturn));
        when(userRepository.findByUsername(searchedUserName)).thenReturn(userToReturn);
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
    }


    @Test
    public void createUser() {
        CreateUserRequest userRequest = createUserRequest("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(userRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());

        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void createUserPasswordToShort() {
        CreateUserRequest userRequest = createUserRequest("short");

        final ResponseEntity<User> responseEntity = userController.createUser(userRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void findUserByName() {
       String searchedUserName = "test";


        final ResponseEntity<User> responseEntityForFinding = userController.findByUserName(searchedUserName);

        assertNotNull(responseEntityForFinding);
        assertEquals(HttpStatus.OK, responseEntityForFinding.getStatusCode());

        User foundedUser = responseEntityForFinding.getBody();

        assertNotNull(foundedUser);
        assertEquals(searchedUserName, foundedUser.getUsername());

    }


    @Test
    public void findUserById() {
        long userId = 1L;

        final ResponseEntity<User> responseEntityForFinding = userController.findById(userId);

        assertNotNull(responseEntityForFinding);
        assertEquals(HttpStatus.OK, responseEntityForFinding.getStatusCode());

        User foundedUser = responseEntityForFinding.getBody();

        assertNotNull(foundedUser);
        assertEquals(userId, foundedUser.getId());

    }

    private CreateUserRequest createUserRequest(String password) {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword(password);
        userRequest.setConfirmPassword(password);
        return userRequest;
    }
}
