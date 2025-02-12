package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Voyage;
import com.example.pidevmicroservice.repositories.VoyageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VoyageServiceImpl implements IVoyageService {
    private final VoyageRepository voyageRepository;

    @Override
    public Voyage addVoyage(Voyage voyage) {
        return voyageRepository.save(voyage);
    }

    @Override
    public List<Voyage> getAllVoyages() {
        return voyageRepository.findAll();
    }

    @Override
    public Voyage getVoyageById(Long id) {
        return voyageRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteVoyage(Long id) {
        voyageRepository.deleteById(id);
    }

    @Override
    public Voyage updateVoyage(Voyage voyage) {
        return voyageRepository.save(voyage);
    }
}