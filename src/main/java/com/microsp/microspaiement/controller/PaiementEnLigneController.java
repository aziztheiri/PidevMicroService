package com.microsp.microspaiement.controller;

import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.services.PaiementEnLigneService;
import com.microsp.microspaiement.services.PdfPaiementEnLigneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/paiements/enligne")
public class PaiementEnLigneController {
    @Autowired
    private PaiementEnLigneService paiementEnLigneService;
    @Autowired
    private PdfPaiementEnLigneService pdfPaiementEnLigneService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<PaiementEnLigne> creerPaiementEnLigne(@RequestBody PaiementEnLigne paiement) {
        return ResponseEntity.ok(paiementEnLigneService.ajouterPaiementEnLigne(paiement));
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<PaiementEnLigne> getPaiementsEnLigne() {
        return paiementEnLigneService.getAllPaiementsEnLigne();
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<PaiementEnLigne> getPaiementEnLigneById(@PathVariable Long id) {
        return paiementEnLigneService.getPaiementEnLigneById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    public ResponseEntity<PaiementEnLigne> modifierPaiementEnLigne(@PathVariable Long id, @RequestBody PaiementEnLigne paiement) {
        return ResponseEntity.ok(paiementEnLigneService.modifierPaiementEnLigne(id, paiement));
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerPaiementEnLigne(@PathVariable Long id) {
        paiementEnLigneService.supprimerPaiementEnLigne(id);
        return ResponseEntity.ok("Paiement en ligne supprimé");
    }

    @GetMapping("/pdf")
    @CrossOrigin(origins = "*")
    public ResponseEntity<byte[]> telechargerPaiementsEnLignePdf() {
        List<PaiementEnLigne> paiements = paiementEnLigneService.getAllPaiementsEnLigne();
        ByteArrayInputStream bis = pdfPaiementEnLigneService.export(paiements);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=paiements-en-ligne.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }


}
