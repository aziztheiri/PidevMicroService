    package com.microsp.microspaiement.controller;

    import com.microsp.microspaiement.entities.PaiementSurPlace;
    import com.microsp.microspaiement.services.CreneauService;
    import com.microsp.microspaiement.services.PaiementSurPlaceService;
    import com.microsp.microspaiement.services.PdfPaiementService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.io.ByteArrayInputStream;
    import java.util.List;

    @RestController
    @CrossOrigin(origins = "*")
    @RequestMapping("/paiements/surplace")

    public class PaiementSurPlaceController {
        @Autowired
        private PdfPaiementService pdfPaiementService;
        @Autowired
        private PaiementSurPlaceService paiementSurPlaceService;

        private CreneauService creneauService;

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

        @GetMapping("/creneaux-disponibles")
        @CrossOrigin(origins = "*")
        public List<String> getCreneauxDisponibles(
                @RequestParam String agence,
                @RequestParam String date) {
            return creneauService.getCreneauxDisponibles(agence, date);
        }

        @GetMapping("/pdf")
        @CrossOrigin(origins = "*")
        public ResponseEntity<byte[]> telechargerPaiementsPdf() {
            List<PaiementSurPlace> paiements = paiementSurPlaceService.getAllPaiementsSurPlace();
            ByteArrayInputStream bis = pdfPaiementService.export(paiements);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=paiements-sur-place.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(bis.readAllBytes());
        }
    }
