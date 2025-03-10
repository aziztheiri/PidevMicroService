package com.microsp.microspaiement.controller;

import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.entities.User;
import com.microsp.microspaiement.feign.UserClient;
import com.microsp.microspaiement.services.PaiementEnLigneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paiements/enligne")
@RequiredArgsConstructor
public class PaiementEnLigneController {
    @Autowired
    private PaiementEnLigneService paiementEnLigneService;
    private final UserClient userClient;
    @PostMapping("/{cin}")
    public ResponseEntity<PaiementEnLigne> creerPaiementEnLigne(@RequestBody PaiementEnLigne paiement,@PathVariable String cin) {
        User user = userClient.getUserByCin(cin);

        if (user == null) {
            throw new RuntimeException("User with CIN " + cin + " does not exist!");
        }
        paiement.setCin(cin);
        return ResponseEntity.ok(paiementEnLigneService.ajouterPaiementEnLigne(paiement));
    }
    @GetMapping
    public List<PaiementEnLigne> getPaiementsEnLigne() {
        return paiementEnLigneService.getAllPaiementsEnLigne();
    }
    @GetMapping("/{id}")
    public ResponseEntity<PaiementEnLigne> getPaiementEnLigneById(@PathVariable Long id) {
        return paiementEnLigneService.getPaiementEnLigneById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<PaiementEnLigne> modifierPaiementEnLigne(@PathVariable Long id, @RequestBody PaiementEnLigne paiement) {
        return ResponseEntity.ok(paiementEnLigneService.modifierPaiementEnLigne(id, paiement));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerPaiementEnLigne(@PathVariable Long id) {
        paiementEnLigneService.supprimerPaiementEnLigne(id);
        return ResponseEntity.ok("Paiement en ligne supprimé");
    }
}
