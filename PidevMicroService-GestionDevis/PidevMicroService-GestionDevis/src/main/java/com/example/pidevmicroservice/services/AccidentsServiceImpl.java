package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Accidents;
import com.example.pidevmicroservice.repositories.AccidentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccidentsServiceImpl implements IAccidentsService {
    private final AccidentsRepository accidentsRepository;

    @Override
    public Accidents addAccidents(Accidents accidents) {
        return accidentsRepository.save(accidents);
    }

    @Override
    public List<Accidents> getAllAccidents() {
        return accidentsRepository.findAll();
    }

    @Override
    public Accidents getAccidentsById(Long id) {
        return accidentsRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAccidents(Long id) {
        accidentsRepository.deleteById(id);
    }

    @Override
    public Accidents updateAccidents(Accidents accidents) {
        return accidentsRepository.save(accidents);
    }
}