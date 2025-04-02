package com.filtro.application.usecase.appointment;

import com.filtro.domain.entities.Appointment;
import com.filtro.domain.repository.AppointmentRepository;
import java.util.List;

public class AppointmentHistory {
    private final AppointmentRepository appointmentRepository;

    public AppointmentHistory(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getPatientHistory(Integer patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getDoctorSchedule(Integer doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
}
