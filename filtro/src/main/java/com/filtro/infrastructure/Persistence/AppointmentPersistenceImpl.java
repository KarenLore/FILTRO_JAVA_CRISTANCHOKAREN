package com.filtro.infrastructure.Persistence;

import com.filtro.domain.entities.Appointment;
import com.filtro.domain.Enum.AppointmentStatus;
import com.filtro.domain.repository.AppointmentRepository;
import com.filtro.infrastructure.database.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentPersistenceImpl implements AppointmentRepository {
    private final ConnectionDb connection;
    
    public AppointmentPersistenceImpl(ConnectionDb connection) {
        this.connection = connection;
    }

    @Override
    public Appointment save(Appointment appointment) {
        String sql = "INSERT INTO appointment (patient_id, doctor_id, date_time, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getDateTime()));
            stmt.setString(4, appointment.getStatus().name());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setId(generatedKeys.getInt(1));
                }
            }
            return appointment;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment", e);
        }
    }

    @Override
    public Optional<Appointment> findById(Integer id) {
        String sql = "SELECT * FROM appointment WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToAppointment(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment by id", e);
        }
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment";
        try (Connection conn = connection.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all appointments", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM appointment WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment", e);
        }
    }

    @Override
    public Appointment update(Appointment appointment) {
        String sql = "UPDATE appointment SET patient_id = ?, doctor_id = ?, date_time = ?, status = ? WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getDateTime()));
            stmt.setString(4, appointment.getStatus().name());
            stmt.setInt(5, appointment.getId());
            
            stmt.executeUpdate();
            return appointment;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment", e);
        }
    }

    @Override
    public List<Appointment> findByPatientId(Integer patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE patient_id = ? ORDER BY date_time DESC";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapRowToAppointment(rs));
                }
            }
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by patient", e);
        }
    }

    @Override
    public List<Appointment> findByDoctorId(Integer doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE doctor_id = ? ORDER BY date_time DESC";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapRowToAppointment(rs));
                }
            }
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by doctor", e);
        }
    }

    @Override
    public boolean isTimeSlotAvailable(Integer doctorId, String dateTime) {
        String sql = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND date_time = ? AND status != 'CANCELLED'";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId);
            stmt.setString(2, dateTime);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking appointment availability", e);
        }
    }

    private Appointment mapRowToAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
            rs.getInt("id"),
            rs.getInt("patient_id"),
            rs.getInt("doctor_id"),
            rs.getTimestamp("date_time").toLocalDateTime(),
            AppointmentStatus.valueOf(rs.getString("status"))
        );
    }
}