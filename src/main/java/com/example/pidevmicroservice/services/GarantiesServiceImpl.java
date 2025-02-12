package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Garanties;
import com.example.pidevmicroservice.repositories.GarantiesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GarantiesServiceImpl implements IGarantiesService {
    private final GarantiesRepository garantiesRepository;

    @Override
    public Garanties addGaranties(Garanties garanties) {
        return garantiesRepository.save(garanties);
    }

    @Override
    public List<Garanties> getAllGaranties() {
        return garantiesRepository.findAll();
    }

    @Override
    public Garanties getGarantiesById(Long id) {
        return garantiesRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteGaranties(Long id) {
        garantiesRepository.deleteById(id);
    }

    @Override
    public Garanties updateGaranties(Garanties garanties) {
        return garantiesRepository.save(garanties);
    }
}