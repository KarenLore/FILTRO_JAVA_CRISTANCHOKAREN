package com.filtro.infrastructure.Persistence;

import com.filtro.domain.entities.Patient;
import com.filtro.domain.repository.PatientRepository;
import com.filtro.infrastructure.database.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientPersistenceImpl implements PatientRepository {
    private final ConnectionDb connection;
    
    public PatientPersistenceImpl(ConnectionDb connection) {
        this.connection = connection;
    }

    @Override
    public Patient save(Patient patient) {
        String sql = "INSERT INTO patient (name, last_name, birth_date, address, phone, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getLastName());
            stmt.setDate(3, Date.valueOf(patient.getBirthDate()));
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getPhone());
            stmt.setString(6, patient.getEmail());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getInt(1));
                }
            }
            return patient;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving patient", e);
        }
    }

    @Override
    public Optional<Patient> findById(Integer id) {
        String sql = "SELECT * FROM patient WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToPatient(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding patient by id", e);
        }
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient";
        try (Connection conn = connection.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patients.add(mapRowToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all patients", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM patient WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting patient", e);
        }
    }

    @Override
    public Patient update(Patient patient) {
        String sql = "UPDATE patient SET name = ?, last_name = ?, birth_date = ?, address = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getLastName());
            stmt.setDate(3, Date.valueOf(patient.getBirthDate()));
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getPhone());
            stmt.setString(6, patient.getEmail());
            stmt.setInt(7, patient.getId());
            
            stmt.executeUpdate();
            return patient;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating patient", e);
        }
    }

    private Patient mapRowToPatient(ResultSet rs) throws SQLException {
        return new Patient(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("last_name"),
            rs.getDate("birth_date").toLocalDate(),
            rs.getString("address"),
            rs.getString("phone"),
            rs.getString("email")
        );
    }
}