package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Reclamation;
import com.example.pidevmicroservice.services.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {
    @Autowired
    private ReclamationService service;

    @PostMapping
    public Reclamation createReclamation(@RequestBody Reclamation reclamation) {
        return service.createReclamation(reclamation);
    }

    @GetMapping
    public List<Reclamation> getAllReclamations() {
        return service.getAllReclamations();
    }

    @GetMapping("/{id}")
    public Reclamation getReclamation(@PathVariable Long id) {
        return service.getReclamationById(id).orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));
    }

    @DeleteMapping("/{id}")
    public void deleteReclamation(@PathVariable Long id) {
        service.deleteReclamation(id);
    }
}
