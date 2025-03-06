package tn.esprit.pidevmicro.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevmicro.entities.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    // Ajoute des méthodes personnalisées si nécessaire
}
