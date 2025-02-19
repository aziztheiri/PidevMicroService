package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.Prevoyance;
import com.example.pidevmicroservice.repositories.PrevoyanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PrevoyanceServiceImpl implements IPrevoyanceService {
    private final PrevoyanceRepository prevoyanceRepository;

    @Override
    public Prevoyance addPrevoyance(Prevoyance prevoyance) {
        return prevoyanceRepository.save(prevoyance);
    }

    @Override
    public List<Prevoyance> getAllPrevoyances() {
        return prevoyanceRepository.findAll();
    }

    @Override
    public Prevoyance getPrevoyanceById(Long id) {
        return prevoyanceRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePrevoyance(Long id) {
        prevoyanceRepository.deleteById(id);
    }

    @Override
    public Prevoyance updatePrevoyance(Prevoyance prevoyance) {
        return prevoyanceRepository.save(prevoyance);
    }
}