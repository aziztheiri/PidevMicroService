package com.microsp.microspaiement.services;

import com.microsp.microspaiement.repo.PaiementSurPlaceRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreneauService {
    private static final List<String> CRENEAUX_VALIDES = Arrays.asList(
            "08:00", "08:20", "08:40", "09:00", "09:20", "09:40",
            "10:00", "10:20", "10:40", "11:00", "11:20", "11:40",
            "14:00", "14:20", "14:40", "15:00", "15:20", "15:40",
            "16:00", "16:20", "16:40", "17:00"
    );
    private PaiementSurPlaceRepository repository;

    public List<String> getCreneauxDisponibles(String agence, String date) {
        List<String> creneauxReserves = repository.findCreneauxOccupes(agence, date);
        return CRENEAUX_VALIDES.stream()
                .filter(c -> !creneauxReserves.contains(c))
                .collect(Collectors.toList());
    }
}
