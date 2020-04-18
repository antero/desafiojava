package com.concrete.desafiojava.service;

import com.concrete.desafiojava.exception.*;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.model.UserLoginDetails;
import com.concrete.desafiojava.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private User testUserUnsaved;
    private User testUserSaved;

    public UserServiceImplTest() {
        testUserUnsaved = new User();
        testUserUnsaved.setName("Test User");
        testUserUnsaved.setEmail("test@user.com");
        testUserUnsaved.setPassword("password1");
    }

    @BeforeAll
    void persistTestUser() {
        testUserSaved = userService.create(testUserUnsaved);
    }

    @Test
    void create() {
        User newUser = new User();
        newUser.setName("Vengeful Spirit");
        newUser.setEmail("vengeful@desafio.com");
        newUser.setPassword("magicmissile");
        User user = userService.create(newUser);

        assertEquals(user.getEmail(), newUser.getEmail());
        assertNotEquals("magicmissile", user.getPassword());
        assertNotNull(user.getToken());
        assertEquals(user.getCreated(), user.getLastLogin());
    }

    @Test
    void createThrowsEmailAlreadyExistsException() {
        User newUser = new User();
        newUser.setName("Vengeful Spirit");
        newUser.setEmail(testUserSaved.getEmail());
        newUser.setPassword("magicmissile");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(newUser));
    }

    @Test
    void findByEmail() {
        assertDoesNotThrow(() -> userService.findByEmail("test@user.com"));
    }

    @Test
    void findByEmailThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(""));
    }

    @Test
    void findById() {
        assertDoesNotThrow(() -> userService.findById(testUserSaved.getId()));
    }

    @Test
    void findByIdThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.findById(UUID.randomUUID()));
    }

    @Test
    void findAll() {
        User newUser = new User();
        newUser.setName("Alleria Windrunner");
        newUser.setEmail("alleria@desafio.com");
        newUser.setPassword("shackleshot");
        newUser = userService.create(newUser);

        List<UUID> ids = Arrays.asList(newUser.getId(), testUserSaved.getId());
        List<User> users = userService.findAll()
                                      .stream()
                                      .filter(u -> ids.contains(u.getId()))
                                      .collect(Collectors.toList());

        assertEquals(users.size(), 2);
    }

    @Test
    void login() {
        UserLoginDetails loginDetails = new UserLoginDetails("test@user.com", "password1");
        User user = userService.login(loginDetails);

        assertEquals(testUserSaved.getId(), user.getId());
    }

    @Test
    void loginThrowsAuthenticationFailureException() {
        UserLoginDetails loginDetails = new UserLoginDetails("test@user.com", "wrongpassword");
        assertThrows(AuthenticationFailureException.class, () -> userService.login(loginDetails));
    }

    @Test
    void profile() {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("token", testUserSaved.getToken());
        assertDoesNotThrow(() -> userService.profile(testUserSaved.getId(), payload));
    }

    @Test
    void profileThrowsTokenMismatchException() {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("token", "wrongtoken");
        assertThrows(TokenMismatchException.class, () -> userService.profile(testUserSaved.getId(), payload));
    }

    @Test
    void profileThrowsInvalidSession() {
        LocalDateTime loginTime = testUserSaved.getLastLogin().minusMinutes(31);
        testUserSaved.setLastLogin(loginTime);
        userRepository.save(testUserSaved);
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("token", testUserSaved.getToken());
        assertThrows(InvalidSessionException.class, () -> userService.profile(testUserSaved.getId(), payload));
    }

    @Test
    void profileThrowsEmptyTokenException() {
        assertThrows(EmptyTokenException.class, () -> userService.profile(testUserSaved.getId(), new HashMap<>()));
    }
}
