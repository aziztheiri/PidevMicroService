package com.microsp.microspaiement.controller;

import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.services.PaiementEnLigneService;
import com.microsp.microspaiement.services.PdfPaiementEnLigneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.braintreegateway.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/paiements/enligne")
public class PaiementEnLigneController {
    private BraintreeGateway gateway;

    public PaiementEnLigneController() {
        gateway = new BraintreeGateway(
                Environment.SANDBOX,
                "jw4y8bbgg3bn4995",
                "929jzrnnns3btmg2",
                "149250b7cce5d48563f8d01e53c638ba"
        );
    }



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

    @GetMapping("/token")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> getClientToken() {
        String token = gateway.clientToken().generate();

        // Créer un objet JSON contenant le token
        Map<String, String> response = new HashMap<>();
        response.put("clientToken", token);

        return ResponseEntity.ok(response);
    }





}
