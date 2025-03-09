package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Habitation;
import com.example.pidevmicroservice.repositories.HabitationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HabitationServiceImpl implements IHabitationService {
    private final HabitationRepository habitationRepository;

    @Override
    public Habitation addHabitation(Habitation habitation) {
        return habitationRepository.save(habitation);
    }

    @Override
    public List<Habitation> getAllHabitations() {
        return habitationRepository.findAll();
    }

    @Override
    public Habitation getHabitationById(Long id) {
        return habitationRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteHabitation(Long id) {
        habitationRepository.deleteById(id);
    }

    @Override
    public Habitation updateHabitation(Habitation habitation) {
        return habitationRepository.save(habitation);
    }
}