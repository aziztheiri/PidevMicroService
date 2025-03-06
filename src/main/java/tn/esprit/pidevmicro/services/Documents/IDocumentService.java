package tn.esprit.pidevmicro.services.Documents;

import tn.esprit.pidevmicro.entities.Document;
import java.util.List;
import java.util.Optional;

public interface IDocumentService {

    List<Document> getAllDocuments();

    Optional<Document> getDocumentById(Long id);

    Document saveDocument(Document document);

    Optional<Document> updateDocument(Long id, Document documentDetails);

    boolean deleteDocument(Long id);
}
