package com.filtro.application.usecase.specialty;

import com.filtro.domain.entities.Specialty;
import com.filtro.domain.repository.SpecialtyRepository;
import java.util.List;
import java.util.Optional;

public class SpecialtyCRUD {
    private final SpecialtyRepository specialtyRepository;

    public SpecialtyCRUD(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public Specialty createSpecialty(String name) {
        Specialty specialty = new Specialty(null, name);
        return specialtyRepository.save(specialty);
    }

    public Optional<Specialty> getSpecialty(Integer id) {
        return specialtyRepository.findById(id);
    }

    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }

    public void deleteSpecialty(Integer id) {
        specialtyRepository.delete(id);
    }

    public Specialty updateSpecialty(Integer id, String name) {
        Specialty specialty = new Specialty(id, name);
        return specialtyRepository.update(specialty);
    }
}
