package com.microsp.microspaiement.repo;

import com.microsp.microspaiement.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {}
