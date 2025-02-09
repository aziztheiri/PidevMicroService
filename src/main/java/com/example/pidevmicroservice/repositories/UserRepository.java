package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    public User findByEmail(String email);
}
