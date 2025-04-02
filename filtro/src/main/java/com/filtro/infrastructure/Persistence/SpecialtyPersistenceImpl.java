package com.filtro.infrastructure.Persistence;

import com.filtro.domain.entities.Specialty;
import com.filtro.domain.repository.SpecialtyRepository;
import com.filtro.infrastructure.database.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialtyPersistenceImpl implements SpecialtyRepository {
    private final ConnectionDb connection;
    
    public SpecialtyPersistenceImpl(ConnectionDb connection) {
        this.connection = connection;
    }

    @Override
    public Specialty save(Specialty specialty) {
        String sql = "INSERT INTO specialty (name) VALUES (?)";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, specialty.getName());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating specialty failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    specialty.setId(generatedKeys.getInt(1));
                }
            }
            return specialty;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving specialty", e);
        }
    }

    @Override
    public Optional<Specialty> findById(Integer id) {
        String sql = "SELECT * FROM specialty WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToSpecialty(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding specialty by id", e);
        }
    }

    @Override
    public List<Specialty> findAll() {
        List<Specialty> specialties = new ArrayList<>();
        String sql = "SELECT * FROM specialty";
        try (Connection conn = connection.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                specialties.add(mapRowToSpecialty(rs));
            }
            return specialties;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all specialties", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM specialty WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting specialty", e);
        }
    }

    @Override
    public Specialty update(Specialty specialty) {
        String sql = "UPDATE specialty SET name = ? WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, specialty.getName());
            stmt.setInt(2, specialty.getId());
            
            stmt.executeUpdate();
            return specialty;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating specialty", e);
        }
    }

    private Specialty mapRowToSpecialty(ResultSet rs) throws SQLException {
        return new Specialty(
            rs.getInt("id"),
            rs.getString("name")
        );
    }
}