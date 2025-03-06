package tn.esprit.pidevmicro.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevmicro.entities.Sinistre;

@Repository
public interface SinistreRepository extends JpaRepository<Sinistre, Long> {
    // Ajoute des méthodes personnalisées si nécessaire
}
