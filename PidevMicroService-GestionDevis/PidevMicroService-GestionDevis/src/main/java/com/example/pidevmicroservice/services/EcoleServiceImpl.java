package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Ecole;
import com.example.pidevmicroservice.repositories.EcoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EcoleServiceImpl implements IEcoleService {
    private final EcoleRepository ecoleRepository;

    @Override
    public Ecole addEcole(Ecole ecole) {
        return ecoleRepository.save(ecole);
    }

    @Override
    public List<Ecole> getAllEcoles() {
        return ecoleRepository.findAll();
    }

    @Override
    public Ecole getEcoleById(Long id) {
        return ecoleRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteEcole(Long id) {
        ecoleRepository.deleteById(id);
    }

    @Override
    public Ecole updateEcole(Ecole ecole) {
        return ecoleRepository.save(ecole);
    }
}