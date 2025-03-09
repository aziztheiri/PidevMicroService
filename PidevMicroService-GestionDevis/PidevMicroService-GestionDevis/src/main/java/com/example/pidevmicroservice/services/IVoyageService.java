package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Voyage;

import java.util.List;

public interface IVoyageService {
    Voyage addVoyage(Voyage voyage);
    List<Voyage> getAllVoyages();
    Voyage getVoyageById(Long id);
    void deleteVoyage(Long id);
    Voyage updateVoyage(Voyage voyage);
}