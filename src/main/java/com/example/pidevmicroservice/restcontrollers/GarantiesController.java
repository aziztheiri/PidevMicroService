package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Garanties;
import com.example.pidevmicroservice.services.IGarantiesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garanties")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class GarantiesController {
    private final IGarantiesService garantiesService;

    @PostMapping
    public Garanties addGaranties(@RequestBody Garanties garanties) {
        return garantiesService.addGaranties(garanties);
    }

    @GetMapping
    public List<Garanties> getAllGaranties() {
        return garantiesService.getAllGaranties();
    }

    @GetMapping("/{id}")
    public Garanties getGarantiesById(@PathVariable Long id) {
        return garantiesService.getGarantiesById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteGaranties(@PathVariable Long id) {
        garantiesService.deleteGaranties(id);
    }

    @PutMapping
    public Garanties updateGaranties(@RequestBody Garanties garanties) {
        return garantiesService.updateGaranties(garanties);
    }
}