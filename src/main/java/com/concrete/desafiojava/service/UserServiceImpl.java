package com.concrete.desafiojava.service;

import com.concrete.desafiojava.exception.UserNotFoundException;
import com.concrete.desafiojava.model.PhoneNumber;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.repository.PhoneNumberRepository;
import com.concrete.desafiojava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneNumberRepository phoneRepository;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User create(User newUser) {
//        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        List<PhoneNumber> phones = newUser.getPhones();
        newUser.setPhones(null);

        User user = userRepository.save(newUser);
        user.setPhones(phoneRepository.saveAll(phones));
        user.setLastLogin(user.getCreated());

        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username)
                             .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
