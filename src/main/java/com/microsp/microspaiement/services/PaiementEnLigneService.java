package com.microsp.microspaiement.services;

import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.repo.PaiementEnLigneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaiementEnLigneService {

    @Autowired
    private PaiementEnLigneRepository paiementEnLigneRepository;

    public PaiementEnLigne ajouterPaiementEnLigne(PaiementEnLigne paiement) {
        return paiementEnLigneRepository.save(paiement);
    }

    public List<PaiementEnLigne> getAllPaiementsEnLigne() {
        return paiementEnLigneRepository.findAll();
    }

    public Optional<PaiementEnLigne> getPaiementEnLigneById(Long id) {
        return paiementEnLigneRepository.findById(id);
    }

    public PaiementEnLigne modifierPaiementEnLigne(Long id, PaiementEnLigne paiement) {
        PaiementEnLigne paiementExistant = paiementEnLigneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement en ligne introuvable avec l'id " + id));

        paiementExistant.setMontant(paiement.getMontant());
        paiementExistant.setNumeroCarte(paiement.getNumeroCarte());
        paiementExistant.setCvv(paiement.getCvv());
        paiementExistant.setExpiration(paiement.getExpiration());
        paiementExistant.setDate_paiement(paiement.getDate_paiement());

        return paiementEnLigneRepository.save(paiementExistant);
    }

    public void supprimerPaiementEnLigne(Long id) {
        paiementEnLigneRepository.deleteById(id);
    }
}
