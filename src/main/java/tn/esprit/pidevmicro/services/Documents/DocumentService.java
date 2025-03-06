package tn.esprit.pidevmicro.services.Documents;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevmicro.entities.Document;
import tn.esprit.pidevmicro.repositorys.DocumentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService implements IDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Optional<Document> updateDocument(Long id, Document documentDetails) {
        return documentRepository.findById(id).map(document -> {
            document.setType(documentDetails.getType());
            document.setChemin(documentDetails.getChemin());
            document.setDateAjout(documentDetails.getDateAjout());
            return documentRepository.save(document);
        });
    }

    @Override
    public boolean deleteDocument(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
