package com.concrete.desafiojava.service;

import com.concrete.desafiojava.exception.AuthenticationFailureException;
import com.concrete.desafiojava.exception.InvalidSessionException;
import com.concrete.desafiojava.exception.UserNotFoundException;
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
    void findByEmail() {
        assertDoesNotThrow(() -> userService.findByEmail("test@user.com"));
    }

    @Test
    void findByEmailThrowsException() {
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(""));
    }

    @Test
    void findById() {
        assertDoesNotThrow(() -> userService.findById(testUserSaved.getId()));
    }

    @Test
    void findByIdThrowsException() {
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
    void loginThrowsException() {
        UserLoginDetails loginDetails = new UserLoginDetails("test@user.com", "wrongpassword");
        assertThrows(AuthenticationFailureException.class, () -> userService.login(loginDetails));
    }

    @Test
    void profile() {
        assertDoesNotThrow(() -> userService.profile(testUserSaved.getId(), testUserSaved.getToken()));
    }

    @Test
    void profileThrowsAuthenticationFailure() {
        assertThrows(AuthenticationFailureException.class, () -> userService.profile(testUserSaved.getId(), "wrongtoken"));
    }

    @Test
    void profileThrowsInvalidSession() {
        LocalDateTime loginTime = testUserSaved.getLastLogin().minusMinutes(31);
        testUserSaved.setLastLogin(loginTime);
        userRepository.save(testUserSaved);
        assertThrows(InvalidSessionException.class, () -> userService.profile(testUserSaved.getId(), testUserSaved.getToken()));
    }
}
