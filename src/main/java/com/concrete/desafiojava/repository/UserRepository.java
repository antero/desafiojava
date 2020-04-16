package com.concrete.desafiojava.repository;

import com.concrete.desafiojava.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
