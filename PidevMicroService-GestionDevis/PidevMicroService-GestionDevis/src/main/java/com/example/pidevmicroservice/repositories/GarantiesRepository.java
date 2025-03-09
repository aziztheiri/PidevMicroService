package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.Garanties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarantiesRepository extends JpaRepository<Garanties, Long> {
}