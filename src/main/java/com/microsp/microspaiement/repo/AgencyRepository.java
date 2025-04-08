package com.microsp.microspaiement.repo;

import com.microsp.microspaiement.entities.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    List<Agency> findAllByOrderByNameAsc();
}