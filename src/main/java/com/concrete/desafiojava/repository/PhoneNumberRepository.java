package com.concrete.desafiojava.repository;

import com.concrete.desafiojava.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}
