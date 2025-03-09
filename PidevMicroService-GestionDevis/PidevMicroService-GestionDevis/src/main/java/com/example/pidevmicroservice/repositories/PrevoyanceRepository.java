package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.Prevoyance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrevoyanceRepository extends JpaRepository<Prevoyance, Long> {
}