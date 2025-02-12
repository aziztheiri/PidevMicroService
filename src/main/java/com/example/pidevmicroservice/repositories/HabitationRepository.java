package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.Habitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitationRepository extends JpaRepository<Habitation, Long> {
}