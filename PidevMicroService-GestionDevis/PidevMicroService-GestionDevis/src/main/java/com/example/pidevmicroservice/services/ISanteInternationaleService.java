package com.example.pidevmicroservice.services;

import com.example.pidevmicroservice.entities.SanteInternationale;
import java.util.List;

public interface ISanteInternationaleService {
    SanteInternationale addSanteInternationale(SanteInternationale santeInternationale);
    List<SanteInternationale> getAllSanteInternationales();
    SanteInternationale getSanteInternationaleById(Long id);
    void deleteSanteInternationale(Long id);
    SanteInternationale updateSanteInternationale(SanteInternationale santeInternationale);
}