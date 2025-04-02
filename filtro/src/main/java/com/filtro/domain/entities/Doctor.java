package com.filtro.domain.entities;

public class Doctor {
    private Integer id;
    private String name;
    private String lastName;
    private Integer specialtyId;
    private String scheduleStart;
    private String scheduleEnd;

    public Doctor() {}

    public Doctor(Integer id, String name, String lastName, 
                 Integer specialtyId, String scheduleStart, String scheduleEnd) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.specialtyId = specialtyId;
        this.scheduleStart = scheduleStart;
        this.scheduleEnd = scheduleEnd;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Integer getSpecialtyId() { return specialtyId; }
    public void setSpecialtyId(Integer specialtyId) { this.specialtyId = specialtyId; }
    public String getScheduleStart() { return scheduleStart; }
    public void setScheduleStart(String scheduleStart) { this.scheduleStart = scheduleStart; }
    public String getScheduleEnd() { return scheduleEnd; }
    public void setScheduleEnd(String scheduleEnd) { this.scheduleEnd = scheduleEnd; }

    @Override
    public String toString() {
        return id + " - " + name + " " + lastName + " " + specialtyId + " " + scheduleStart + " " + scheduleEnd + " ";
    }
}
