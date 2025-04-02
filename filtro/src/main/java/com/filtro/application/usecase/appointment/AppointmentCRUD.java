package com.filtro.application.usecase.appointment;

import com.filtro.domain.entities.Appointment;
import com.filtro.domain.Enum.AppointmentStatus;
import com.filtro.domain.repository.AppointmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AppointmentCRUD {
    private final AppointmentRepository appointmentRepository;

    public AppointmentCRUD(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(Integer patientId, Integer doctorId, 
                                       LocalDateTime dateTime, AppointmentStatus status) {
        Appointment appointment = new Appointment(null, patientId, doctorId, dateTime, status);
        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> getAppointment(Integer id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void deleteAppointment(Integer id) {
        appointmentRepository.delete(id);
    }

    public Appointment updateAppointment(Integer id, Integer patientId, Integer doctorId, 
                                       LocalDateTime dateTime, AppointmentStatus status) {
        Appointment appointment = new Appointment(id, patientId, doctorId, dateTime, status);
        return appointmentRepository.update(appointment);
    }

    public boolean isTimeSlotAvailable(Integer doctorId, String dateTime) {
        return appointmentRepository.isTimeSlotAvailable(doctorId, dateTime);
    }
}
