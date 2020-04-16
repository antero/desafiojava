package com.concrete.desafiojava.service;

import com.concrete.desafiojava.exception.AuthenticationFailureException;
import com.concrete.desafiojava.exception.InvalidSessionException;
import com.concrete.desafiojava.exception.UserNotFoundException;
import com.concrete.desafiojava.model.PhoneNumber;
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
import java.util.UUID;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneNumberRepository phoneRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User create(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        List<PhoneNumber> phones = newUser.getPhones();
        newUser.setPhones(null);

        User user = userRepository.save(newUser);
        user.setPhones(phoneRepository.saveAll(phones));
        user.setLastLogin(user.getCreated());
        user.setToken(jwtService.create(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username)
                             .orElseThrow(() -> new UserNotFoundException(username));
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
        User user = findByUsername(details.getEmail());

        if (!passwordEncoder.matches(details.getPassword(), user.getPassword())) {
            throw new AuthenticationFailureException();
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }

    @Override
    public User profile(UUID id, String token) {
        User user = findById(id);

        if (!jwtService.verify(token, user.getPassword())) {
            throw new AuthenticationFailureException();
        }

        if (sessionExpired(user.getLastLogin())) {
            throw new InvalidSessionException();
        }

        return user;
    }

    private boolean sessionExpired(LocalDateTime lastLogin) {
        Duration duration = Duration.between(lastLogin, LocalDateTime.now());
        return duration.toSeconds() > 30 * 60;
    }
}
