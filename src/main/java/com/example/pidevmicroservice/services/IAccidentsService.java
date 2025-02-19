package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Accidents;
import java.util.List;

public interface IAccidentsService {
    Accidents addAccidents(Accidents accidents);
    List<Accidents> getAllAccidents();
    Accidents getAccidentsById(Long id);
    void deleteAccidents(Long id);
    Accidents updateAccidents(Accidents accidents);
}