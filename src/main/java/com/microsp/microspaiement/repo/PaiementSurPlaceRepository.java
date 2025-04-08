package com.microsp.microspaiement.repo;

import com.microsp.microspaiement.entities.PaiementSurPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaiementSurPlaceRepository extends JpaRepository<PaiementSurPlace, Long> {
    @Query("SELECT p.creneau FROM PaiementSurPlace p WHERE p.agence = :agence AND p.date_rdv = :date")
    List<String> findCreneauxOccupes(
            @Param("agence") String agence,
            @Param("date") String date);
}