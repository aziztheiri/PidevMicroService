package com.example.pidevmicroservice.restcontrollers;

import com.example.pidevmicroservice.entities.Accidents;
import com.example.pidevmicroservice.services.IAccidentsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accidents")
@AllArgsConstructor
public class AccidentsController {
    private final IAccidentsService accidentsService;

    @PostMapping
    public Accidents addAccidents(@RequestBody Accidents accidents) {
        return accidentsService.addAccidents(accidents);
    }

    @GetMapping
    public List<Accidents> getAllAccidents() {
        return accidentsService.getAllAccidents();
    }

    @GetMapping("/{id}")
    public Accidents getAccidentsById(@PathVariable Long id) {
        return accidentsService.getAccidentsById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAccidents(@PathVariable Long id) {
        accidentsService.deleteAccidents(id);
    }

    @PutMapping
    public Accidents updateAccidents(@RequestBody Accidents accidents) {
        return accidentsService.updateAccidents(accidents);
    }
}