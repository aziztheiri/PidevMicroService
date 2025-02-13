package com.microsp.microspaiement.repo;

import com.microsp.microspaiement.entities.PaiementEnLigne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementEnLigneRepository extends JpaRepository<PaiementEnLigne, Long> {}