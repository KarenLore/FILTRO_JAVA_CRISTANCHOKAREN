package com.filtro.domain.repository;


import com.filtro.domain.entities.Specialty;
import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository {
    Specialty save(Specialty specialty);
    Optional<Specialty> findById(Integer id);
    List<Specialty> findAll();
    void delete(Integer id);
    Specialty update(Specialty specialty);
}