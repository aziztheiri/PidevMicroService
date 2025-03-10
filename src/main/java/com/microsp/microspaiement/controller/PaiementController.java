    package com.microsp.microspaiement.controller;
    
    import com.microsp.microspaiement.entities.Paiement;
    import com.microsp.microspaiement.services.PaiementService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.CrossOrigin;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    
    import java.util.List;
    
    @RestController
    @CrossOrigin(origins = "*")
    @RequestMapping("/paiements")
    public class PaiementController {
        @Autowired
        private PaiementService paiementService;
    
        // ✅ Récupérer tous les paiements (en ligne + sur place)
        @GetMapping
        public List<Paiement> getPaiements() {
            return paiementService.getAllPaiements();
        }
    }
