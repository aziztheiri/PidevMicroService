package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.Ecole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcoleRepository extends JpaRepository<Ecole, Long> {
}