package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Habitation;

import java.util.List;

public interface IHabitationService {
    Habitation addHabitation(Habitation habitation);
    List<Habitation> getAllHabitations();
    Habitation getHabitationById(Long id);
    void deleteHabitation(Long id);
    Habitation updateHabitation(Habitation habitation);
}