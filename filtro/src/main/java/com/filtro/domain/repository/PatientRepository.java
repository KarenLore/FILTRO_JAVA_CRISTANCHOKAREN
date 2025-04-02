package com.filtro.domain.repository;


import com.filtro.domain.entities.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    Patient save(Patient patient);
    Optional<Patient> findById(Integer id);
    List<Patient> findAll();
    void delete(Integer id);
    Patient update(Patient patient);
}