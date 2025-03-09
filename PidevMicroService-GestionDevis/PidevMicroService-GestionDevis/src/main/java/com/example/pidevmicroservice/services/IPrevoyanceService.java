package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Prevoyance;
import java.util.List;

public interface IPrevoyanceService {
    Prevoyance addPrevoyance(Prevoyance prevoyance);
    List<Prevoyance> getAllPrevoyances();
    Prevoyance getPrevoyanceById(Long id);
    void deletePrevoyance(Long id);
    Prevoyance updatePrevoyance(Prevoyance prevoyance);
}