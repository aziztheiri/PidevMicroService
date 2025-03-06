package tn.esprit.pidevmicro.services.Sinistres;

import tn.esprit.pidevmicro.entities.Sinistre;
import java.util.List;
import java.util.Optional;

public interface ISinistreService {

    List<Sinistre> getAllSinistres();

    Optional<Sinistre> getSinistreById(Long id);

    Sinistre saveSinistre(Sinistre sinistre);

    Optional<Sinistre> updateSinistre(Long id, Sinistre sinistreDetails);

    boolean deleteSinistre(Long id);
}
