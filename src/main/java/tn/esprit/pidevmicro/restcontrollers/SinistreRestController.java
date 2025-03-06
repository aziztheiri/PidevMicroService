package tn.esprit.pidevmicro.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevmicro.entities.Sinistre;
import tn.esprit.pidevmicro.services.Sinistres.SinistreService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sinistres")
@Validated
@CrossOrigin(origins = "*")
public class SinistreRestController {

    @Autowired
    private SinistreService sinistreService;

    @GetMapping
    public List<Sinistre> getAllSinistres() {
        return sinistreService.getAllSinistres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sinistre> getSinistreById(@PathVariable Long id) {
        return sinistreService.getSinistreById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/addsinistre")
    public Sinistre createSinistre(@Valid @RequestBody Sinistre sinistre) {
        return sinistreService.saveSinistre(sinistre);
    }

    @PutMapping("/updatesinistre/{id}")
    public ResponseEntity<Sinistre> updateSinistre(@PathVariable Long id, @Valid @RequestBody Sinistre sinistreDetails) {
        return sinistreService.updateSinistre(id, sinistreDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deletesinistre/{id}")
    public ResponseEntity<Void> deleteSinistre(@PathVariable Long id) {
        return sinistreService.deleteSinistre(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}