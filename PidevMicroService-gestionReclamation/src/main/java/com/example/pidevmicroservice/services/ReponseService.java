package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Reclamation;
import com.example.pidevmicroservice.entities.Reponse;
import com.example.pidevmicroservice.repositories.ReclamationRepository;
import com.example.pidevmicroservice.repositories.ReponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReponseService {
    @Autowired
    private ReponseRepository reponseRepository;

    @Autowired
    private ReclamationRepository reclamationRepository;

    public Reponse createReponse(Long reclamationId, String statut) {
        Optional<Reclamation> reclamation = reclamationRepository.findById(reclamationId);

        if (reclamation.isPresent()) {
            Reponse reponse = new Reponse(statut, reclamation.get());
            return reponseRepository.save(reponse);
        } else {
            throw new RuntimeException("Réclamation non trouvée");
        }
    }

    public Optional<Reponse> getReponseByReclamationId(Long reclamationId) {
        return reponseRepository.findByReclamationId(reclamationId);
    }

    public void deleteReponse(Long id) {
        reponseRepository.deleteById(id);
    }
}
