package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Habitation;
import com.example.pidevmicroservice.services.IHabitationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitation")
@AllArgsConstructor
public class HabitationController {
    private final IHabitationService habitationService;

    @PostMapping
    public Habitation addHabitation(@RequestBody Habitation habitation) {
        return habitationService.addHabitation(habitation);
    }

    @GetMapping
    public List<Habitation> getAllHabitations() {
        return habitationService.getAllHabitations();
    }

    @GetMapping("/{id}")
    public Habitation getHabitationById(@PathVariable Long id) {
        return habitationService.getHabitationById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteHabitation(@PathVariable Long id) {
        habitationService.deleteHabitation(id);
    }

    @PutMapping
    public Habitation updateHabitation(@RequestBody Habitation habitation) {
        return habitationService.updateHabitation(habitation);
    }
}