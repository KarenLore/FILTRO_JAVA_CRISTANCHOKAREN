package com.filtro.application.usecase.doctor;

import com.filtro.domain.entities.Doctor;
import com.filtro.domain.repository.DoctorRepository;
import java.util.List;
import java.util.Optional;

public class DoctorCRUD {
    private final DoctorRepository doctorRepository;

    public DoctorCRUD(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor createDoctor(String name, String lastName, 
                             Integer specialtyId, String scheduleStart, String scheduleEnd) {
        Doctor doctor = new Doctor(null, name, lastName, specialtyId, scheduleStart, scheduleEnd);
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> getDoctor(Integer id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public void deleteDoctor(Integer id) {
        doctorRepository.delete(id);
    }

    public Doctor updateDoctor(Integer id, String name, String lastName, 
                             Integer specialtyId, String scheduleStart, String scheduleEnd) {
        Doctor doctor = new Doctor(id, name, lastName, specialtyId, scheduleStart, scheduleEnd);
        return doctorRepository.update(doctor);
    }

    public List<Doctor> getDoctorsBySpecialty(Integer specialtyId) {
        return doctorRepository.findBySpecialtyId(specialtyId);
    }
}