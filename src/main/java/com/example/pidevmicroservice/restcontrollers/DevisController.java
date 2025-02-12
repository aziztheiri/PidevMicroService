package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Devis;
import com.example.pidevmicroservice.services.IDevisService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devis")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class DevisController {
    private final IDevisService devisService;

    @PostMapping
    public Devis addDevis(@RequestBody Devis devis) {
        return devisService.addDevis(devis);
    }

    @GetMapping
    public List<Devis> getAllDevis() {
        return devisService.getAllDevis();
    }

    @GetMapping("/{id}")
    public Devis getDevisById(@PathVariable Long id) {
        return devisService.getDevisById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDevis(@PathVariable Long id) {
        devisService.deleteDevis(id);
    }

    @PutMapping
    public Devis updateDevis(@RequestBody Devis devis) {
        return devisService.updateDevis(devis);
    }
}