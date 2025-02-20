package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Reponse;
import com.example.pidevmicroservice.services.ReponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reponses")
public class ReponseController {
    @Autowired
    private ReponseService service;

    @PostMapping("/{reclamationId}")
    public Reponse createReponse(@PathVariable Long reclamationId, @RequestBody String statut) {
        return service.createReponse(reclamationId, statut);
    }

    @GetMapping("/{reclamationId}")
    public Reponse getReponseByReclamation(@PathVariable Long reclamationId) {
        return service.getReponseByReclamationId(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réponse non trouvée pour cette réclamation"));
    }

    @DeleteMapping("/{id}")
    public void deleteReponse(@PathVariable Long id) {
        service.deleteReponse(id);
    }
}
