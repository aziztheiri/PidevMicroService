package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Devis;
import com.example.pidevmicroservice.repositories.DevisRepository;
import com.example.pidevmicroservice.repositories.EcoleRepository;
import com.example.pidevmicroservice.repositories.HabitationRepository;
import com.example.pidevmicroservice.repositories.VoyageRepository;
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

    @Override
    public Devis addDevis(Devis devis) {
        // Check if idAssurance is unique across Ecole, Voyage, and Habitation
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
        // Check if idAssurance is unique across Ecole, Voyage, and Habitation
        if (!isIdUniqueAcrossTables(devis.getIdAssurance())) {
            throw new RuntimeException("ID already exists in another table");
        }
        return devisRepository.save(devis);
    }

    // Helper method to check if ID is unique across Ecole, Voyage, and Habitation
    private boolean isIdUniqueAcrossTables(Long id) {
        return !ecoleRepository.existsById(id) &&
                !voyageRepository.existsById(id) &&
                !habitationRepository.existsById(id);
    }
}