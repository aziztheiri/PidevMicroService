    package com.microsp.microspaiement.controller;

    import com.microsp.microspaiement.entities.PaiementSurPlace;
    import com.microsp.microspaiement.entities.User;
    import com.microsp.microspaiement.feign.UserClient;
    import com.microsp.microspaiement.services.PaiementSurPlaceService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/paiements/surplace")
    @RequiredArgsConstructor
    public class PaiementSurPlaceController {
        @Autowired
        private PaiementSurPlaceService paiementSurPlaceService;
        private final UserClient userClient;
        @PostMapping("/{cin}")
        public ResponseEntity<PaiementSurPlace> creerPaiementSurPlace(@RequestBody PaiementSurPlace paiement,@PathVariable String cin) {
            User user = userClient.getUserByCin(cin);
            if (user == null) {
                throw new RuntimeException("User with CIN " + cin + " does not exist!");
            }
            paiement.setCin(cin);
            return ResponseEntity.ok(paiementSurPlaceService.ajouterPaiementSurPlace(paiement));
        }

        @GetMapping
        public List<PaiementSurPlace> getPaiementsSurPlace() {
            return paiementSurPlaceService.getAllPaiementsSurPlace();
        }

        @GetMapping("/{id}")
        public ResponseEntity<PaiementSurPlace> getPaiementSurPlaceById(@PathVariable Long id) {
            return paiementSurPlaceService.getPaiementSurPlaceById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<PaiementSurPlace> modifierPaiementSurPlace(@PathVariable Long id, @RequestBody PaiementSurPlace paiement) {
            return ResponseEntity.ok(paiementSurPlaceService.modifierPaiementSurPlace(id, paiement));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> supprimerPaiementSurPlace(@PathVariable Long id) {
            paiementSurPlaceService.supprimerPaiementSurPlace(id);
            return ResponseEntity.ok("Paiement sur place supprimé");
        }
    }
