package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.Capitalisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapitalisationRepository extends JpaRepository<Capitalisation, Long> {
}