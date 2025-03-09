package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Capitalisation;
import com.example.pidevmicroservice.services.ICapitalisationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capitalisation")
@AllArgsConstructor
public class CapitalisationController {
    private final ICapitalisationService capitalisationService;

    @PostMapping
    public Capitalisation addCapitalisation(@RequestBody Capitalisation capitalisation) {
        return capitalisationService.addCapitalisation(capitalisation);
    }

    @GetMapping
    public List<Capitalisation> getAllCapitalisations() {
        return capitalisationService.getAllCapitalisations();
    }

    @GetMapping("/{id}")
    public Capitalisation getCapitalisationById(@PathVariable Long id) {
        return capitalisationService.getCapitalisationById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCapitalisation(@PathVariable Long id) {
        capitalisationService.deleteCapitalisation(id);
    }

    @PutMapping
    public Capitalisation updateCapitalisation(@RequestBody Capitalisation capitalisation) {
        return capitalisationService.updateCapitalisation(capitalisation);
    }
}