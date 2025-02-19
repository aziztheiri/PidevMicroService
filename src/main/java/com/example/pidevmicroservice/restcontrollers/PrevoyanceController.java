package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Prevoyance;
import com.example.pidevmicroservice.services.IPrevoyanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prevoyance")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PrevoyanceController {
    private final IPrevoyanceService prevoyanceService;

    @PostMapping
    public Prevoyance addPrevoyance(@RequestBody Prevoyance prevoyance) {
        return prevoyanceService.addPrevoyance(prevoyance);
    }

    @GetMapping
    public List<Prevoyance> getAllPrevoyances() {
        return prevoyanceService.getAllPrevoyances();
    }

    @GetMapping("/{id}")
    public Prevoyance getPrevoyanceById(@PathVariable Long id) {
        return prevoyanceService.getPrevoyanceById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePrevoyance(@PathVariable Long id) {
        prevoyanceService.deletePrevoyance(id);
    }

    @PutMapping
    public Prevoyance updatePrevoyance(@RequestBody Prevoyance prevoyance) {
        return prevoyanceService.updatePrevoyance(prevoyance);
    }
}