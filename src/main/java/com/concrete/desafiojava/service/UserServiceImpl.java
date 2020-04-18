package com.concrete.desafiojava.service;

import com.concrete.desafiojava.exception.*;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.model.UserLoginDetails;
import com.concrete.desafiojava.repository.PhoneNumberRepository;
import com.concrete.desafiojava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final long EXPIRATION_TIME_IN_SECONDS = 30 * 60;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneNumberRepository phoneRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User create(User newUser) {
        try {
            findByEmail(newUser.getEmail());
            throw new EmailAlreadyExistsException(newUser.getEmail());
        } catch (UserNotFoundException e) {}

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User user = userRepository.save(newUser);
        user.setLastLogin(user.getCreated());
        user.setToken(jwtService.create(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User login(UserLoginDetails details) {
        User user = findByEmail(details.getEmail());

        if (!passwordEncoder.matches(details.getPassword(), user.getPassword())) {
            throw new AuthenticationFailureException();
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }

    @Override
    public User profile(UUID id, Map<String, Object> payload) {
        if (!payload.containsKey("token")) {
            throw new EmptyTokenException();
        }

        User user = findById(id);

        if (!jwtService.verify((String) payload.get("token"), user.getPassword())) {
            throw new TokenMismatchException();
        }

        if (sessionExpired(user.getLastLogin())) {
            String expiryTime = EXPIRATION_TIME_IN_SECONDS / 60 + " minutes";
            throw new InvalidSessionException(expiryTime);
        }

        return user;
    }

    private boolean sessionExpired(LocalDateTime lastLogin) {
        Duration duration = Duration.between(lastLogin, LocalDateTime.now());
        return duration.toSeconds() > EXPIRATION_TIME_IN_SECONDS;
    }
}
