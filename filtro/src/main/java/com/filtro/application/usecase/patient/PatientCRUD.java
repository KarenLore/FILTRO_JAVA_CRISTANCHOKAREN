package com.filtro.application.usecase.patient;

import com.filtro.domain.entities.Patient;
import com.filtro.domain.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PatientCRUD {
    private final PatientRepository patientRepository;

    public PatientCRUD(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(String name, String lastName, 
                               LocalDate birthDate, String address, String phone, String email) {
        Patient patient = new Patient(null, name, lastName, birthDate, address, phone, email);
        return patientRepository.save(patient);
    }

    public Optional<Patient> getPatient(Integer id) {
        return patientRepository.findById(id);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public void deletePatient(Integer id) {
        patientRepository.delete(id);
    }

    public Patient updatePatient(Integer id, String name, String lastName, 
                               LocalDate birthDate, String address, String phone, String email) {
        Patient patient = new Patient(id, name, lastName, birthDate, address, phone, email);
        return patientRepository.update(patient);
    }
}
