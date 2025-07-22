package com.murphy.simplebank.repository;

import com.murphy.simplebank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,Long> {
    Boolean existsByEmail(String email);
    User findByAccountNumber(String accountNumber);
    Boolean existsByAccountNumber(String accountNumber);
}
