package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.SanteInternationale;
import com.example.pidevmicroservice.services.ISanteInternationaleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sante-internationale")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SanteInternationaleController {
    private final ISanteInternationaleService santeInternationaleService;

    @PostMapping
    public SanteInternationale addSanteInternationale(@RequestBody SanteInternationale santeInternationale) {
        return santeInternationaleService.addSanteInternationale(santeInternationale);
    }

    @GetMapping
    public List<SanteInternationale> getAllSanteInternationales() {
        return santeInternationaleService.getAllSanteInternationales();
    }

    @GetMapping("/{id}")
    public SanteInternationale getSanteInternationaleById(@PathVariable Long id) {
        return santeInternationaleService.getSanteInternationaleById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSanteInternationale(@PathVariable Long id) {
        santeInternationaleService.deleteSanteInternationale(id);
    }

    @PutMapping
    public SanteInternationale updateSanteInternationale(@RequestBody SanteInternationale santeInternationale) {
        return santeInternationaleService.updateSanteInternationale(santeInternationale);
    }
}