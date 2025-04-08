package com.microsp.microspaiement.services;

import com.microsp.microspaiement.entities.Paiement;
import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.entities.PaiementSurPlace;
import com.microsp.microspaiement.repo.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaiementService {
    @Autowired
    private PaiementRepository paiementRepository;

        
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

}
