package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Devis;
import com.example.pidevmicroservice.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DevisServiceImpl implements IDevisService {
    private final DevisRepository devisRepository;
    private final EcoleRepository ecoleRepository;
    private final VoyageRepository voyageRepository;
    private final HabitationRepository habitationRepository;
    private final AccidentsRepository accidentsRepository;
    private final CapitalisationRepository capitalisationRepository;
    private final PrevoyanceRepository prevoyanceRepository;
    private final SanteInternationaleRepository santeInternationaleRepository;

    @Override
    public Devis addDevis(Devis devis) {
        // Check if idAssurance is unique across all tables
        if (!isIdUniqueAcrossTables(devis.getIdAssurance())) {
            throw new RuntimeException("ID already exists in another table");
        }
        return devisRepository.save(devis);
    }

    @Override
    public List<Devis> getAllDevis() {
        return devisRepository.findAll();
    }

    @Override
    public Devis getDevisById(Long id) {
        return devisRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDevis(Long id) {
        devisRepository.deleteById(id);
    }

    @Override
    public Devis updateDevis(Devis devis) {
        // Check if idAssurance is unique across all tables
        if (!isIdUniqueAcrossTables(devis.getIdAssurance())) {
            throw new RuntimeException("ID already exists in another table");
        }
        return devisRepository.save(devis);
    }

    // Helper method to check if ID is unique across all tables
    private boolean isIdUniqueAcrossTables(Long id) {
        return !ecoleRepository.existsById(id) &&
                !voyageRepository.existsById(id) &&
                !habitationRepository.existsById(id) &&
                !accidentsRepository.existsById(id) &&
                !capitalisationRepository.existsById(id) &&
                !prevoyanceRepository.existsById(id) &&
                !santeInternationaleRepository.existsById(id);
    }
}