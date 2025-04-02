package com.filtro.domain.entities;


import com.filtro.domain.Enum.AppointmentStatus;
import java.time.LocalDateTime;

public class Appointment {
    private Integer id;
    private Integer patientId;
    private Integer doctorId;
    private LocalDateTime dateTime;
    private AppointmentStatus status;

    public Appointment() {}

    public Appointment(Integer id, Integer patientId, Integer doctorId, 
                      LocalDateTime dateTime, AppointmentStatus status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return id + " - " + patientId + " " + doctorId + " " + dateTime + " " + status + " ";
    }
}
