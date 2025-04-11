package com.microsp.microspaiement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.entities.User;
import com.microsp.microspaiement.services.PaiementEnLigneService;
import com.microsp.microspaiement.services.PdfPaiementEnLigneService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.braintreegateway.*;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
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


    private String getAccessToken() {
        try {
            // URL pour obtenir un token d'accès
            String url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";

            // L'authentification de base
            String auth = "AU6MDYYQAx_o--v0mcocH47xwnKdLjYisVK9fqhCPXsBaDR4ObNSkrl9yE3gUY2D8UMR5xHasoi0BeMS:EJ-OnQxXtGtS6cQBOqiQ5CMTjMRUAExr2u1BdIBc-qdq7q2HkSkzwSotSheJBQxjMlYELgld2M-EQQrQ";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Basic " + encodedAuth);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // Paramètres à envoyer dans la requête
            StringEntity params = new StringEntity("grant_type=client_credentials");
            post.setEntity(params);

            // Effectuer la requête et obtenir la réponse
            HttpResponse response = client.execute(post);
            String responseBody = EntityUtils.toString(response.getEntity());

            // Extraire le token d'accès de la réponse JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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


    @PostMapping("/paypal/validate")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> validatePaypalOrder(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderID");
        System.out.println("Received order ID: " + orderId);

        try {
            // Récupérer l'access token
            String accessToken = getAccessToken();
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Erreur lors de l'obtention de l'access token PayPal."));
            }

            // Appel vers l'API REST PayPal pour vérifier l'état de la commande
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderId);

            // Utilisation du Bearer token pour l'authentification
            post.setHeader("Authorization", "Bearer " + accessToken);
            post.setHeader("Content-Type", "application/json");

            HttpResponse response = client.execute(post);
            String responseBody = EntityUtils.toString(response.getEntity());

            // Analyser la réponse JSON de PayPal
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            String status = jsonNode.get("status").asText();

            // Si le paiement est complet
            if ("COMPLETED".equals(status)) {
                double amount = jsonNode.get("purchase_units").get(0).get("amount").get("value").asDouble();
                User.addFunds(amount);  // Simuler le rechargement du portefeuille de l'utilisateur
                return ResponseEntity.ok(Map.of("message", "Paiement confirmé. Solde: " + User.getWalletBalance()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Paiement non confirmé."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors de la vérification PayPal."));
        }
    }

    @GetMapping("/filtrer")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<PaiementEnLigne>> filtrerPaiements(
            @RequestParam(required = false) BigDecimal minMontant,
            @RequestParam(required = false) BigDecimal maxMontant,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datePaiement,
            @RequestParam(required = false) String paymentMethodNonce
    ) {
        if (minMontant == null) minMontant = BigDecimal.ZERO;
        if (maxMontant == null) maxMontant = BigDecimal.valueOf(Double.MAX_VALUE);

        List<PaiementEnLigne> filtres = paiementEnLigneService.filtrerPaiements(minMontant, maxMontant, datePaiement, paymentMethodNonce);
        return ResponseEntity.ok(filtres);
    }



}
