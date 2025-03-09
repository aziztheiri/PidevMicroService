package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Garanties;

import java.util.List;

public interface IGarantiesService {
    Garanties addGaranties(Garanties garanties);
    List<Garanties> getAllGaranties();
    Garanties getGarantiesById(Long id);
    void deleteGaranties(Long id);
    Garanties updateGaranties(Garanties garanties);
}