package tn.esprit.pidevmicro.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevmicro.entities.Document;
import tn.esprit.pidevmicro.repositorys.DocumentRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@Validated
@CrossOrigin(origins = "*")
public class DocumentRestController {

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Optional<Document> document = documentRepository.findById(id);
        return document.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/adddocument")
    public Document createDocument(@Valid @RequestBody Document document) {
        return documentRepository.save(document);
    }

    @PutMapping("/updatedocument/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @Valid @RequestBody Document documentDetails) {
        return documentRepository.findById(id).map(document -> {
            document.setType(documentDetails.getType());
            document.setChemin(documentDetails.getChemin());
            document.setDateAjout(documentDetails.getDateAjout());
            Document updatedDocument = documentRepository.save(document);
            return ResponseEntity.ok(updatedDocument);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deletedocument/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}