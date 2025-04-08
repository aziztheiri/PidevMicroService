package com.microsp.microspaiement.controller;

import com.microsp.microspaiement.entities.Agency;
import com.microsp.microspaiement.repo.AgencyRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agencies")
public class AgencyController {

    private final AgencyRepository agencyRepository;

    public AgencyController(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @GetMapping
    public List<Agency> getAllAgencies() {
        return agencyRepository.findAllByOrderByNameAsc();
    }

    @PostMapping
    public Agency createAgency(@RequestBody Agency agency) {
        return agencyRepository.save(agency);
    }
}