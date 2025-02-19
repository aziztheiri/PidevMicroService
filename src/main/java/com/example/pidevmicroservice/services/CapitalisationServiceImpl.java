package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Capitalisation;
import com.example.pidevmicroservice.repositories.CapitalisationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CapitalisationServiceImpl implements ICapitalisationService {
    private final CapitalisationRepository capitalisationRepository;

    @Override
    public Capitalisation addCapitalisation(Capitalisation capitalisation) {
        return capitalisationRepository.save(capitalisation);
    }

    @Override
    public List<Capitalisation> getAllCapitalisations() {
        return capitalisationRepository.findAll();
    }

    @Override
    public Capitalisation getCapitalisationById(Long id) {
        return capitalisationRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCapitalisation(Long id) {
        capitalisationRepository.deleteById(id);
    }

    @Override
    public Capitalisation updateCapitalisation(Capitalisation capitalisation) {
        return capitalisationRepository.save(capitalisation);
    }
}