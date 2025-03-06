package tn.esprit.pidevmicro.services.Sinistres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevmicro.entities.Sinistre;
import tn.esprit.pidevmicro.repositorys.SinistreRepository;
import tn.esprit.pidevmicro.services.Sinistres.ISinistreService;

import java.util.List;
import java.util.Optional;

@Service
public class SinistreService implements ISinistreService {

    @Autowired
    private SinistreRepository sinistreRepository;

    @Override
    public List<Sinistre> getAllSinistres() {
        return sinistreRepository.findAll();
    }

    @Override
    public Optional<Sinistre> getSinistreById(Long id) {
        return sinistreRepository.findById(id);
    }

    @Override
    public Sinistre saveSinistre(Sinistre sinistre) {
        return sinistreRepository.save(sinistre);
    }

    @Override
    public Optional<Sinistre> updateSinistre(Long id, Sinistre sinistreDetails) {
        return sinistreRepository.findById(id).map(sinistre -> {
            sinistre.setDescription(sinistreDetails.getDescription());
            sinistre.setDateDeclaration(sinistreDetails.getDateDeclaration());
            sinistre.setStatut(sinistreDetails.getStatut());
            return sinistreRepository.save(sinistre);
        });
    }

    @Override
    public boolean deleteSinistre(Long id) {
        if (sinistreRepository.existsById(id)) {
            sinistreRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
