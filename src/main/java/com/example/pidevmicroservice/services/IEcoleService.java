package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Ecole;

import java.util.List;

public interface IEcoleService {
    Ecole addEcole(Ecole ecole);
    List<Ecole> getAllEcoles();
    Ecole getEcoleById(Long id);
    void deleteEcole(Long id);
    Ecole updateEcole(Ecole ecole);
}