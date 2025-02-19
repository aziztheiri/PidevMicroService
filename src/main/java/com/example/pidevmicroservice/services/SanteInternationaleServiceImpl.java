package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.SanteInternationale;
import com.example.pidevmicroservice.repositories.SanteInternationaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SanteInternationaleServiceImpl implements ISanteInternationaleService {
    private final SanteInternationaleRepository santeInternationaleRepository;

    @Override
    public SanteInternationale addSanteInternationale(SanteInternationale santeInternationale) {
        return santeInternationaleRepository.save(santeInternationale);
    }

    @Override
    public List<SanteInternationale> getAllSanteInternationales() {
        return santeInternationaleRepository.findAll();
    }

    @Override
    public SanteInternationale getSanteInternationaleById(Long id) {
        return santeInternationaleRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteSanteInternationale(Long id) {
        santeInternationaleRepository.deleteById(id);
    }

    @Override
    public SanteInternationale updateSanteInternationale(SanteInternationale santeInternationale) {
        return santeInternationaleRepository.save(santeInternationale);
    }
}