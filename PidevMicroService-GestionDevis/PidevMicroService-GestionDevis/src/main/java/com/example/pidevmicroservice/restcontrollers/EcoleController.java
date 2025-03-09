package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Ecole;
import com.example.pidevmicroservice.services.IEcoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ecole")
@AllArgsConstructor
public class EcoleController {
    private final IEcoleService ecoleService;

    @PostMapping
    public Ecole addEcole(@RequestBody Ecole ecole) {
        return ecoleService.addEcole(ecole);
    }

    @GetMapping
    public List<Ecole> getAllEcoles() {
        return ecoleService.getAllEcoles();
    }

    @GetMapping("/{id}")
    public Ecole getEcoleById(@PathVariable Long id) {
        return ecoleService.getEcoleById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEcole(@PathVariable Long id) {
        ecoleService.deleteEcole(id);
    }

    @PutMapping
    public Ecole updateEcole(@RequestBody Ecole ecole) {
        return ecoleService.updateEcole(ecole);
    }
}