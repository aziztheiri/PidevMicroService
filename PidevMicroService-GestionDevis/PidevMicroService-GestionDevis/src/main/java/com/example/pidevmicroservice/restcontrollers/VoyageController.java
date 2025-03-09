package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Voyage;
import com.example.pidevmicroservice.services.IVoyageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voyage")
@AllArgsConstructor
public class VoyageController {
    private final IVoyageService voyageService;

    @PostMapping
    public Voyage addVoyage(@RequestBody Voyage voyage) {
        return voyageService.addVoyage(voyage);
    }

    @GetMapping
    public List<Voyage> getAllVoyages() {
        return voyageService.getAllVoyages();
    }

    @GetMapping("/{id}")
    public Voyage getVoyageById(@PathVariable Long id) {
        return voyageService.getVoyageById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVoyage(@PathVariable Long id) {
        voyageService.deleteVoyage(id);
    }

    @PutMapping
    public Voyage updateVoyage(@RequestBody Voyage voyage) {
        return voyageService.updateVoyage(voyage);
    }
}