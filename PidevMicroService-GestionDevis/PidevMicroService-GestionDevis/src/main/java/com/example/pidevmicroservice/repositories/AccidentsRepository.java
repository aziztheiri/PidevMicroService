package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.Accidents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccidentsRepository extends JpaRepository<Accidents, Long> {
}