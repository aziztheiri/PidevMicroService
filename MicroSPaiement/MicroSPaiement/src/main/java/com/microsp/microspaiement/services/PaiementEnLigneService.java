package com.microsp.microspaiement.services;

import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.repo.PaiementEnLigneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementEnLigneService {
    @Autowired
    private PaiementEnLigneRepository paiementEnLigneRepository;

    public PaiementEnLigne ajouterPaiementEnLigne(PaiementEnLigne paiement) {
        paiement.setDate_paiement(LocalDateTime.now());
        return paiementEnLigneRepository.save(paiement);
    }

    public List<PaiementEnLigne> getAllPaiementsEnLigne() {
        return paiementEnLigneRepository.findAll();
    }

    public Optional<PaiementEnLigne> getPaiementEnLigneById(Long id) {
        return paiementEnLigneRepository.findById(id);
    }

    public PaiementEnLigne modifierPaiementEnLigne(Long id, PaiementEnLigne details) {
        return paiementEnLigneRepository.findById(id)
                .map(p -> {
                    p.setNumeroCarte(details.getNumeroCarte());
                    p.setCvv(details.getCvv());
                    p.setExpiration(details.getExpiration());
                    return paiementEnLigneRepository.save(p);
                }).orElseThrow(() -> new RuntimeException("Paiement en ligne non trouvé"));
    }

    public void supprimerPaiementEnLigne(Long id) {
        paiementEnLigneRepository.deleteById(id);
    }
}
