package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Devis;
import com.example.pidevmicroservice.entities.User;
import com.example.pidevmicroservice.feign.UserClient;
import com.example.pidevmicroservice.enums.StatutDevis;
import com.example.pidevmicroservice.services.IDevisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devis")
@RequiredArgsConstructor
public class DevisController {
    private final IDevisService devisService;
    private final UserClient userClient;

    @PostMapping("/{cin}")
    public Devis addDevis(@RequestBody Devis devis,@PathVariable String cin) {
        User user = userClient.getUserByCin(cin);

        if (user == null) {
            throw new RuntimeException("User with CIN " + cin + " does not exist!");
        }
        devis.setCin(cin);
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
    // New endpoint to update the status of a devis
    @PatchMapping("/{id}/statut")
    public Devis updateDevisStatus(@PathVariable Long id, @RequestParam String statut) {
        // Convert the String to StatutDevis enum
        StatutDevis statutDevis = StatutDevis.valueOf(statut.toUpperCase());
        return devisService.updateDevisStatus(id, statutDevis);
    }

    @GetMapping("/cin/{cin}")
    public List<Devis> getDevisByCin(@PathVariable String cin) {
        return devisService.getDevisByCin(cin);
    }
}