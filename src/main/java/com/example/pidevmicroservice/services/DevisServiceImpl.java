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

        return devisRepository.save(devis);
    }

}