package com.filtro.domain.repository;


import com.filtro.domain.entities.Appointment;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(Integer id);
    List<Appointment> findAll();
    void delete(Integer id);
    Appointment update(Appointment appointment);
    List<Appointment> findByPatientId(Integer patientId);
    List<Appointment> findByDoctorId(Integer doctorId);
    boolean isTimeSlotAvailable(Integer doctorId, String dateTime);
}
