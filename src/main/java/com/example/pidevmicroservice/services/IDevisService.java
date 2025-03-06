package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Devis;
import com.example.pidevmicroservice.enums.StatutDevis;

import java.util.List;

public interface IDevisService {
    Devis addDevis(Devis devis);
    List<Devis> getAllDevis();
    Devis getDevisById(Long id);
    void deleteDevis(Long id);
    Devis updateDevis(Devis devis);
    Devis updateDevisStatus(Long id, StatutDevis statut);
    List<Devis> getDevisByCin(String cin);
}