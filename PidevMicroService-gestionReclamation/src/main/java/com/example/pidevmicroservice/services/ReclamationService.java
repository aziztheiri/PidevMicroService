package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Reclamation;
import com.example.pidevmicroservice.repositories.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReclamationService {
    @Autowired
    private ReclamationRepository repository;

    public Reclamation createReclamation(Reclamation reclamation) {
        return repository.save(reclamation);
    }

    public List<Reclamation> getAllReclamations() {
        return repository.findAll();
    }

    public Optional<Reclamation> getReclamationById(Long id) {
        return repository.findById(id);
    }

    public Reclamation updateReclamation(Long id, Reclamation updatedReclamation) {
        return repository.findById(id).map(reclamation -> {
            reclamation.setSujet(updatedReclamation.getSujet());
            reclamation.setDescription(updatedReclamation.getDescription());
            reclamation.setType(updatedReclamation.getType());
            reclamation.setStatut(updatedReclamation.getStatut());
            return repository.save(reclamation);
        }).orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));
    }

    public void deleteReclamation(Long id) {
        repository.deleteById(id);
    }
}
