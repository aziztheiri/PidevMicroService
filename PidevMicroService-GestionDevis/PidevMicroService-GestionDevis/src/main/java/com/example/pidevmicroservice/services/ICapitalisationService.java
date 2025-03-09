package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Capitalisation;
import java.util.List;

public interface ICapitalisationService {
    Capitalisation addCapitalisation(Capitalisation capitalisation);
    List<Capitalisation> getAllCapitalisations();
    Capitalisation getCapitalisationById(Long id);
    void deleteCapitalisation(Long id);
    Capitalisation updateCapitalisation(Capitalisation capitalisation);
}