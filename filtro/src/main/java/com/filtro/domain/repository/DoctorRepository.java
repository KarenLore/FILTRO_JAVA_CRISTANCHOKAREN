package com.filtro.domain.repository;


import com.filtro.domain.entities.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    Doctor save(Doctor doctor);
    Optional<Doctor> findById(Integer id);
    List<Doctor> findAll();
    void delete(Integer id);
    Doctor update(Doctor doctor);
    List<Doctor> findBySpecialtyId(Integer specialtyId);
}
