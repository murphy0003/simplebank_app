package com.murphy.simplebank.repository;

import com.murphy.simplebank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    User findByAccountNumber(String accountNumber);
    Boolean existsByAccountNumber(String accountNumber);
}
