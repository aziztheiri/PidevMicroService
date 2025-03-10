    package com.microsp.microspaiement.controller;

    import com.microsp.microspaiement.entities.PaiementSurPlace;
    import com.microsp.microspaiement.services.PaiementSurPlaceService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @CrossOrigin(origins = "*")
    @RequestMapping("/paiements/surplace")

    public class PaiementSurPlaceController {
        @Autowired


        private PaiementSurPlaceService paiementSurPlaceService;

        @PostMapping
        @CrossOrigin(origins = "*")
        public ResponseEntity<PaiementSurPlace> creerPaiementSurPlace(@RequestBody PaiementSurPlace paiement) {
            return ResponseEntity.ok(paiementSurPlaceService.ajouterPaiementSurPlace(paiement));
        }

        @GetMapping
        @CrossOrigin(origins = "*")
        public List<PaiementSurPlace> getPaiementsSurPlace() {
            return paiementSurPlaceService.getAllPaiementsSurPlace();
        }

        @GetMapping("/{id}")
        @CrossOrigin(origins = "*")
        public ResponseEntity<PaiementSurPlace> getPaiementSurPlaceById(@PathVariable Long id) {
            return paiementSurPlaceService.getPaiementSurPlaceById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        @CrossOrigin(origins = "*")
        public ResponseEntity<PaiementSurPlace> modifierPaiementSurPlace(@PathVariable Long id, @RequestBody PaiementSurPlace paiement) {
            return ResponseEntity.ok(paiementSurPlaceService.modifierPaiementSurPlace(id, paiement));
        }

        @DeleteMapping("/{id}")
        @CrossOrigin(origins = "*")
        public ResponseEntity<String> supprimerPaiementSurPlace(@PathVariable Long id) {
            paiementSurPlaceService.supprimerPaiementSurPlace(id);
            return ResponseEntity.ok("Paiement sur place supprimé");
        }
    }
