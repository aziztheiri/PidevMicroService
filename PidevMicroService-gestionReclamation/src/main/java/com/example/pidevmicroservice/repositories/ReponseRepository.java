package com.example.pidevmicroservice.repositories;

import com.example.pidevmicroservice.entities.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
    Optional<Reponse> findByReclamationId(Long reclamationId);
}
