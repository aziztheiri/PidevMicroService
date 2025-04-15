package com.microsp.microspaiement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsp.microspaiement.entities.PaiementEnLigne;
import com.microsp.microspaiement.entities.User;
import com.microsp.microspaiement.repo.UserRepository;
import com.microsp.microspaiement.services.PaiementEnLigneService;
import com.microsp.microspaiement.services.PdfPaiementEnLigneService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/paiements/enligne")
public class PaiementEnLigneController {

    private static final String CLIENT_ID = "AUYTje5NW-sYODGgNPK4_Pt1KZHA-ZU6npn2nCi0-COGDfuxuhtOTd5raD5N9bOoYIZ7MibZ7ns7m_lX";
    private static final String SECRET = "EHkMB0FiNHTr5tbfmJllNHU1C277joOaQQKRbg-RL2dnWYlSjxEGyc3qIY5vKjHkamkBqQGRLTRm_F7q";
    private BraintreeGateway gateway;
    @Autowired
    private UserRepository userRepository;

    //private User user;

    public PaiementEnLigneController() {
        gateway = new BraintreeGateway(
                Environment.SANDBOX,
                "jw4y8bbgg3bn4995",
                "929jzrnnns3btmg2",
                "149250b7cce5d48563f8d01e53c638ba"
        );
    }


    public String getAccessToken() {
        try {
            String url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
            String auth = CLIENT_ID + ":" + SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);

            post.setHeader("Authorization", "Basic " + encodedAuth);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            post.setEntity(new StringEntity("grant_type=client_credentials"));

            HttpResponse response = client.execute(post);
            String responseBody = EntityUtils.toString(response.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getClientToken(String accessToken) {
        try {
            String url = "https://api-m.sandbox.paypal.com/v1/identity/generate-token";

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);

            post.setHeader("Authorization", "Bearer " + accessToken);
            post.setHeader("Content-Type", "application/json");

            HttpResponse response = client.execute(post);
            String responseBody = EntityUtils.toString(response.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            return jsonNode.get("client_token").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/paypal/create-order-with-token")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createPaypalOrderWithToken(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> payload) {

        try {
            String montant = payload.get("montant");
            if (montant == null) return ResponseEntity.badRequest().body("Montant manquant.");

            // Création de l'ordre PayPal
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("https://api-m.sandbox.paypal.com/v2/checkout/orders");
            post.setHeader("Authorization", authHeader);  // ← ici on utilise le token fourni par l'utilisateur
            post.setHeader("Content-Type", "application/json");

            String jsonBody = """
        {
          "intent": "CAPTURE",
          "purchase_units": [{
            "amount": {
              "currency_code": "USD",
              "value": "%s"
            }
          }],
          "application_context": {
            "return_url": "https://example.com/success",
            "cancel_url": "https://example.com/cancel"
          }
        }
        """.formatted(montant);

            post.setEntity(new StringEntity(jsonBody));

            HttpResponse response = client.execute(post);
            String responseBody = EntityUtils.toString(response.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);

            String orderId = jsonNode.get("id").asText();
            String approvalUrl = "";
            for (JsonNode link : jsonNode.get("links")) {
                if ("approve".equals(link.get("rel").asText())) {
                    approvalUrl = link.get("href").asText();
                    break;
                }
            }

            Map<String, String> result = new HashMap<>();
            result.put("orderID", orderId);
            result.put("approvalUrl", approvalUrl);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de l'ordre PayPal.");
        }
    }















    @Autowired
    private PaiementEnLigneService paiementEnLigneService;
    @Autowired
    private PdfPaiementEnLigneService pdfPaiementEnLigneService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> creerPaiementEnLigne(@RequestBody PaiementEnLigne paiement) {
        String nonce = paiement.getPaymentMethodNonce();
        BigDecimal montant = BigDecimal.valueOf(paiement.getMontant());

        // Vérification basique
        if (nonce == null || montant == null) {
            return ResponseEntity.badRequest().body("Le nonce ou le montant est manquant.");
        }

        // Création de la transaction
        TransactionRequest request = new TransactionRequest()
                .amount(montant)
                .paymentMethodNonce(nonce)
                .customerId("ilyestouil")
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            paiement.setTransactionId(transaction.getId());

            // Sauvegarde en base
            PaiementEnLigne saved = paiementEnLigneService.ajouterPaiementEnLigne(paiement);
            return ResponseEntity.ok(saved);
        } else {
            // Log complet si échec
            String messageErreur = result.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur Braintree : " + messageErreur);
        }
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











    @PostMapping("/paypal/create-order")
    @CrossOrigin(origins = "*")
        public ResponseEntity<?> createPaypalOrder(@RequestBody Map<String, String> payload) {
        try {
            System.out.println("➡️ Début de la création de l'ordre PayPal");

            String montant = payload.get("montant");
            if (montant == null) {
                System.out.println("❌ Montant manquant.");
                return ResponseEntity.badRequest().body("Montant manquant.");
            }
            double montantF = Double.parseDouble(montant);
            System.out.println("✅ Montant reçu : " + montantF);

            // Étape 1 : Générer l'access token
            System.out.println("🔐 Génération de l'access token...");
            String urlToken = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
            String auth = CLIENT_ID + ":" + SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost tokenRequest = new HttpPost(urlToken);
            tokenRequest.setHeader("Authorization", "Basic " + encodedAuth);
            tokenRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            tokenRequest.setEntity(new StringEntity("grant_type=client_credentials"));
            HttpResponse tokenResponse = client.execute(tokenRequest);
            String tokenBody = EntityUtils.toString(tokenResponse.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode tokenNode = mapper.readTree(tokenBody);
            String accessToken = tokenNode.get("access_token").asText();
            System.out.println("✅ Access token obtenu.");

            // Étape 2 : Créer l'ordre PayPal
            System.out.println("🧾 Création de l'ordre PayPal...");
            HttpPost post = new HttpPost("https://api-m.sandbox.paypal.com/v2/checkout/orders");
            post.setHeader("Authorization", "Bearer " + accessToken);
            post.setHeader("Content-Type", "application/json");

            String jsonBody = """
        {
          "intent": "CAPTURE",
          "purchase_units": [{
            "amount": {
              "currency_code": "EUR",
              "value": "%s"
            }
          }],
          "application_context": {
            "return_url": "https://example.com/success",
            "cancel_url": "https://example.com/cancel"
            
          }
        }
        """.formatted(montant);
            post.setEntity(new StringEntity(jsonBody));

            HttpResponse response = client.execute(post);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode jsonNode = mapper.readTree(responseBody);
            System.out.println("✅ Ordre PayPal créé avec succès.");

            String orderId = jsonNode.get("id").asText();
            String approvalUrl = "";
            for (JsonNode link : jsonNode.get("links")) {
                if ("approve".equals(link.get("rel").asText())) {
                    approvalUrl = link.get("href").asText();
                    break;
                }
            }

            System.out.println("🆔 Order ID : " + orderId);
            System.out.println("🔗 Approval URL : " + approvalUrl);

            Map<String, String> result = new HashMap<>();
            result.put("orderID", orderId);
            result.put("approvalUrl", approvalUrl);

            System.out.println("✅ Données prêtes à être envoyées au frontend.");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la création de l'ordre PayPal : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de l'ordre PayPal.");
        }
    }


    @PostMapping("/paypal/capture-order")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> capturePaypalOrder(@RequestBody Map<String, String> payload) {
        try {
            System.out.println("➡️ Début de la capture PayPal");

            String orderId = payload.get("orderID");
            System.out.println("📦 orderID reçu : " + orderId);

            String montant = payload.get("montant");
            System.out.println("💰 Montant reçu : " + montant);
            double montantF = Double.parseDouble(montant);

            Long userId = Long.parseLong(payload.get("userId").toString());
            System.out.println("👤 userId reçu : " + userId);

            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                System.out.println("✅ Utilisateur trouvé en base.");

                User user = optionalUser.get();
                double oldBalance = user.getWalletBalance();
                user.setWalletBalance(oldBalance + montantF);

                userRepository.save(user);
                System.out.println("💼 Solde mis à jour : " + oldBalance + " ➡️ " + user.getWalletBalance());
            } else {
                System.out.println("❌ Utilisateur non trouvé pour l'ID : " + userId);
                throw new RuntimeException("Utilisateur non trouvé pour l'ID : " + userId);
            }

            if (orderId == null) {
                System.out.println("❌ orderID manquant.");
                return ResponseEntity.badRequest().body("orderID manquant.");
            }

            // 🔐 Étape 1 : Génération du token d'accès
            System.out.println("🔐 Génération de l'access token PayPal...");
            String urlToken = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
            String auth = CLIENT_ID + ":" + SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost tokenRequest = new HttpPost(urlToken);
            tokenRequest.setHeader("Authorization", "Basic " + encodedAuth);
            tokenRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            tokenRequest.setEntity(new StringEntity("grant_type=client_credentials"));
            HttpResponse tokenResponse = client.execute(tokenRequest);
            String tokenBody = EntityUtils.toString(tokenResponse.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode tokenNode = mapper.readTree(tokenBody);
            String accessToken = tokenNode.get("access_token").asText();
            System.out.println("✅ Access token obtenu.");

            // 💳 Étape 2 : Capture de l'ordre PayPal
            System.out.println("📥 Envoi de la requête de capture pour orderID : " + orderId);
            HttpPost captureRequest = new HttpPost("https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderId + "/capture");
            captureRequest.setHeader("Authorization", "Bearer " + accessToken);
            captureRequest.setHeader("Content-Type", "application/json");

            HttpResponse response = client.execute(captureRequest);
            String responseBody = EntityUtils.toString(response.getEntity());

            System.out.println("📨 Réponse de PayPal reçue :");
            System.out.println(responseBody);

            JsonNode result = mapper.readTree(responseBody);
            System.out.println("✅ Capture réussie. Retour au frontend.");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la capture de l'ordre PayPal : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la capture de l'ordre PayPal.");
        }
    }












}
