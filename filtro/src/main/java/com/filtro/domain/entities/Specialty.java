package com.filtro.domain.entities;

public class Specialty {
    private Integer id;
    private String name;

    public Specialty() {}

    public Specialty(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return id + " - " + name + " ";
    }
}