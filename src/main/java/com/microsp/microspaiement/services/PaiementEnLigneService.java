package com.microsp.microspaiement.services;

import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.repo.PaiementEnLigneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        paiementExistant.setPaymentMethodNonce(paiement.getPaymentMethodNonce());

        return paiementEnLigneRepository.save(paiementExistant);
    }

    public void supprimerPaiementEnLigne(Long id) {
        paiementEnLigneRepository.deleteById(id);
    }

    public List<PaiementEnLigne> filtrerPaiements(
            BigDecimal minMontant,
            BigDecimal maxMontant,
            LocalDate datePaiement,
            String paymentMethodNonce
    ) {
        return paiementEnLigneRepository.findAll().stream()
                .filter(p -> {
                    BigDecimal montant = BigDecimal.valueOf(p.getMontant());

                    boolean montantOk = montant.compareTo(minMontant) >= 0 && montant.compareTo(maxMontant) <= 0;
                    boolean dateOk = (datePaiement == null || p.getDate_paiement().toLocalDate().isEqual(datePaiement));
                    boolean nonceOk = (paymentMethodNonce == null || paymentMethodNonce.equals(p.getPaymentMethodNonce()));

                    return montantOk && dateOk && nonceOk;
                })
                .collect(Collectors.toList());
    }


}
