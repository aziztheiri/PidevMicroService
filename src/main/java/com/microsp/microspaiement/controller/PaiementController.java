    package com.microsp.microspaiement.controller;
    
    import com.microsp.microspaiement.entities.Paiement;
    import com.microsp.microspaiement.services.PaiementService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    
    @RestController
    @CrossOrigin(origins = "*")
    @RequestMapping("/paiements")
    public class PaiementController {
        @Autowired
        private PaiementService paiementService;

        @GetMapping
        public List<Paiement> getPaiements() {
            return paiementService.getAllPaiements();
        }

    }
