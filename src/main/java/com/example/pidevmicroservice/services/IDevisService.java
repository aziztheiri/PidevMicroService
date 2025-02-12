package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Devis;

import java.util.List;

public interface IDevisService {
    Devis addDevis(Devis devis);
    List<Devis> getAllDevis();
    Devis getDevisById(Long id);
    void deleteDevis(Long id);
    Devis updateDevis(Devis devis);
}