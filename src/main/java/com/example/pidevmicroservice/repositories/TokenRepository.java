package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<VerificationToken,Long> {
    public VerificationToken findByUserEmail(String email);
}
