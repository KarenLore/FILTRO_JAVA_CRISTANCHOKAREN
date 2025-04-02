package com.filtro.infrastructure.Persistence;

import com.filtro.domain.entities.Doctor;
import com.filtro.domain.repository.DoctorRepository;
import com.filtro.infrastructure.database.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorPersistenceImpl implements DoctorRepository {
    private final ConnectionDb connection;
    
    public DoctorPersistenceImpl(ConnectionDb connection) {
        this.connection = connection;
    }

    @Override
    public Doctor save(Doctor doctor) {
        String sql = "INSERT INTO doctor (name, last_name, specialty_id, schedule_start, schedule_end) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getLastName());
            stmt.setInt(3, doctor.getSpecialtyId());
            stmt.setString(4, doctor.getScheduleStart());
            stmt.setString(5, doctor.getScheduleEnd());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    doctor.setId(generatedKeys.getInt(1));
                }
            }
            return doctor;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving doctor", e);
        }
    }

    @Override
    public Optional<Doctor> findById(Integer id) {
        String sql = "SELECT * FROM doctor WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToDoctor(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding doctor by id", e);
        }
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctor";
        try (Connection conn = connection.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                doctors.add(mapRowToDoctor(rs));
            }
            return doctors;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all doctors", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM doctor WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting doctor", e);
        }
    }

    @Override
    public Doctor update(Doctor doctor) {
        String sql = "UPDATE doctor SET name = ?, last_name = ?, specialty_id = ?, schedule_start = ?, schedule_end = ? WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getLastName());
            stmt.setInt(3, doctor.getSpecialtyId());
            stmt.setString(4, doctor.getScheduleStart());
            stmt.setString(5, doctor.getScheduleEnd());
            stmt.setInt(6, doctor.getId());
            
            stmt.executeUpdate();
            return doctor;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating doctor", e);
        }
    }

    @Override
    public List<Doctor> findBySpecialtyId(Integer specialtyId) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctor WHERE specialty_id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, specialtyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapRowToDoctor(rs));
                }
            }
            return doctors;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding doctors by specialty", e);
        }
    }

    private Doctor mapRowToDoctor(ResultSet rs) throws SQLException {
        return new Doctor(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("last_name"),
            rs.getInt("specialty_id"),
            rs.getString("schedule_start"),
            rs.getString("schedule_end")
        );
    }
}