package com.microsp.microspaiement.services;

import com.microsp.microspaiement.entities.PaiementSurPlace;
import com.microsp.microspaiement.repo.PaiementSurPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementSurPlaceService {
    @Autowired
    private PaiementSurPlaceRepository paiementSurPlaceRepository;

    public PaiementSurPlace ajouterPaiementSurPlace(PaiementSurPlace paiement) {
        paiement.setDate_paiement(LocalDateTime.now());
        return paiementSurPlaceRepository.save(paiement);
    }

    public List<PaiementSurPlace> getAllPaiementsSurPlace() {
        return paiementSurPlaceRepository.findAll();
    }

    public Optional<PaiementSurPlace> getPaiementSurPlaceById(Long id) {
        return paiementSurPlaceRepository.findById(id);
    }

    public PaiementSurPlace modifierPaiementSurPlace(Long id, PaiementSurPlace details) {
        return paiementSurPlaceRepository.findById(id)
                .map(p -> {
                    p.setAgence(details.getAgence());
                    p.setDate_rdv(details.getDate_rdv());
                    return paiementSurPlaceRepository.save(p);
                }).orElseThrow(() -> new RuntimeException("Paiement sur place non trouvé"));
    }

    public void supprimerPaiementSurPlace(Long id) {
        paiementSurPlaceRepository.deleteById(id);
    }
}
