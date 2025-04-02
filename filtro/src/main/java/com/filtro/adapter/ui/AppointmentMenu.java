package com.filtro.adapter.ui;

import com.filtro.Main;
import com.filtro.application.usecase.appointment.AppointmentCRUD;
import com.filtro.application.usecase.appointment.AppointmentHistory;
import com.filtro.application.usecase.doctor.DoctorCRUD;
import com.filtro.application.usecase.patient.PatientCRUD;
import com.filtro.domain.entities.Appointment;
import com.filtro.domain.Enum.AppointmentStatus;
import com.filtro.infrastructure.Persistence.AppointmentPersistenceImpl;
import com.filtro.infrastructure.Persistence.DoctorPersistenceImpl;
import com.filtro.infrastructure.Persistence.PatientPersistenceImpl;
import com.filtro.infrastructure.database.ConnectMysqlFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AppointmentMenu {
    public static void main(String[] args) {
        PatientPersistenceImpl patientRepository = new PatientPersistenceImpl(ConnectMysqlFactory.crearConexion());
        DoctorPersistenceImpl doctorRepository = new DoctorPersistenceImpl(ConnectMysqlFactory.crearConexion());
        
        PatientCRUD patientCRUD = new PatientCRUD(patientRepository);
        DoctorCRUD doctorCRUD = new DoctorCRUD(doctorRepository);
        AppointmentCRUD appointmentCRUD = new AppointmentCRUD(
            new AppointmentPersistenceImpl(ConnectMysqlFactory.crearConexion())
        );
        AppointmentHistory appointmentHistory = new AppointmentHistory(
            new AppointmentPersistenceImpl(ConnectMysqlFactory.crearConexion())
        );
        
        try (Scanner sc = new Scanner(System.in)) {
            new AppointmentMenu(appointmentCRUD, appointmentHistory, patientCRUD, doctorCRUD, sc).showMenu();
        } catch (Exception e) {
            System.out.println("Error: Please restart the application");
            e.printStackTrace();
        }
    } 

    private final AppointmentCRUD appointmentCRUD;
    private final AppointmentHistory appointmentHistory;
    private final PatientCRUD patientCRUD;
    private final DoctorCRUD doctorCRUD;
    private final Scanner scanner;

    public AppointmentMenu(AppointmentCRUD appointmentCRUD, AppointmentHistory appointmentHistory,
                          PatientCRUD patientCRUD, DoctorCRUD doctorCRUD, Scanner scanner) {
        this.appointmentCRUD = appointmentCRUD;
        this.appointmentHistory = appointmentHistory;
        this.patientCRUD = patientCRUD;
        this.doctorCRUD = doctorCRUD;
        this.scanner = scanner;
    }

    public void showMenu() {
        String menu;
        do {
            clearConsole();
            System.out.println("=== APPOINTMENT MANAGEMENT ===");
            System.out.println("1. Schedule New Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("3. Update Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Complete Appointment");
            System.out.println("6. View Patient History");
            System.out.println("7. View Doctor Schedule");
            System.out.println("8. Return to Main Menu");
            System.out.print("Select an option: ");
            menu = scanner.nextLine();
            System.out.println("");

            switch (menu) {
                case "1" -> scheduleAppointment();
                case "2" -> viewAllAppointments();
                case "3" -> updateAppointment();
                case "4" -> cancelAppointment();
                case "5" -> completeAppointment();
                case "6" -> viewPatientHistory();
                case "7" -> viewDoctorSchedule();
                case "8" -> {
                    System.out.println("Returning to main menu...");
                    Main.main(new String[]{});
                }
                default -> {
                    System.out.println("Invalid option, please try again");
                    waitForInput();
                }
            }
        } while (!menu.equals("8"));
    }

    private void scheduleAppointment() {
        try {
            System.out.println("\n--- SCHEDULE NEW APPOINTMENT ---");
            
            // List patients
            System.out.println("\nAvailable Patients:");
            patientCRUD.getAllPatients().forEach(p -> 
                System.out.println(p.getId() + ": " + p.getName() + " " + p.getLastName()));
            
            System.out.print("Enter Patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine());
            
            // List doctors
            System.out.println("\nAvailable Doctors:");
            doctorCRUD.getAllDoctors().forEach(d -> 
                System.out.println(d.getId() + ": " + d.getName() + " " + d.getLastName() + 
                                 " (Specialty: " + d.getSpecialtyId() + ")"));
            
            System.out.print("Enter Doctor ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Date and Time (yyyy-MM-dd HH:mm): ");
            String dateTimeStr = scanner.nextLine();
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            // Check if time slot is available
            if (!appointmentCRUD.isTimeSlotAvailable(doctorId, dateTimeStr)) {
                System.out.println("Error: That time slot is not available");
                waitForInput();
                return;
            }
            
            // Create appointment
            Appointment appointment = appointmentCRUD.createAppointment(
                patientId, doctorId, dateTime, AppointmentStatus.SCHEDULED);
            
            System.out.println("Appointment scheduled successfully! ID: " + appointment.getId());
            waitForInput();
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numeric IDs");
            waitForInput();
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date/time format. Use yyyy-MM-dd HH:mm");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error scheduling appointment: " + e.getMessage());
            waitForInput();
        }
    }

    private void viewAllAppointments() {
        try {
            System.out.println("\n--- ALL APPOINTMENTS ---");
            List<Appointment> appointments = appointmentCRUD.getAllAppointments();
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found.");
            } else {
                appointments.forEach(appointment -> {
                    System.out.println("ID: " + appointment.getId());
                    System.out.println("Patient ID: " + appointment.getPatientId());
                    System.out.println("Doctor ID: " + appointment.getDoctorId());
                    System.out.println("Date/Time: " + appointment.getDateTime());
                    System.out.println("Status: " + appointment.getStatus());
                    System.out.println("----------------------");
                });
            }
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error viewing appointments: " + e.getMessage());
            waitForInput();
        }
    }

    private void updateAppointment() {
        try {
            System.out.println("\n--- UPDATE APPOINTMENT ---");
            
            System.out.println("\nCurrent Appointments:");
            appointmentCRUD.getAllAppointments().forEach(a -> 
                System.out.println(a.getId() + ": Patient " + a.getPatientId() + 
                                 " with Doctor " + a.getDoctorId() + " at " + a.getDateTime()));
            
            System.out.print("Enter Appointment ID to update: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());
            
            // Get current appointment
            Optional<Appointment> appointmentOpt = appointmentCRUD.getAppointment(appointmentId);
            if (appointmentOpt.isEmpty()) {
                System.out.println("Appointment not found.");
                waitForInput();
                return;
            }
            
            Appointment appointment = appointmentOpt.get();
            
            System.out.println("\nCurrent Information:");
            System.out.println("1. Patient ID: " + appointment.getPatientId());
            System.out.println("2. Doctor ID: " + appointment.getDoctorId());
            System.out.println("3. Date/Time: " + appointment.getDateTime());
            System.out.println("4. Status: " + appointment.getStatus());
            
            System.out.println("\nEnter the number of the field to update (1-4) or 0 to save:");
            
            while (true) {
                System.out.print("Field number (0 to save): ");
                String field = scanner.nextLine();
                
                if (field.equals("0")) {
                    appointmentCRUD.updateAppointment(
                        appointment.getId(),
                        appointment.getPatientId(),
                        appointment.getDoctorId(),
                        appointment.getDateTime(),
                        appointment.getStatus()
                    );
                    System.out.println("Appointment updated successfully!");
                    break;
                }
                
                switch (field) {
                    case "1":
                        System.out.print("New Patient ID: ");
                        appointment.setPatientId(Integer.parseInt(scanner.nextLine()));
                        break;
                    case "2":
                        System.out.print("New Doctor ID: ");
                        appointment.setDoctorId(Integer.parseInt(scanner.nextLine()));
                        break;
                    case "3":
                        System.out.print("New Date/Time (yyyy-MM-dd HH:mm): ");
                        String dateTimeStr = scanner.nextLine();
                        appointment.setDateTime(LocalDateTime.parse(dateTimeStr, 
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                        break;
                    case "4":
                        System.out.println("Available Statuses: SCHEDULED, COMPLETED, CANCELLED");
                        System.out.print("New Status: ");
                        appointment.setStatus(AppointmentStatus.valueOf(scanner.nextLine().toUpperCase()));
                        break;
                    default:
                        System.out.println("Invalid field number.");
                }
            }
            waitForInput();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numeric values");
            waitForInput();
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date/time format. Use yyyy-MM-dd HH:mm");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error updating appointment: " + e.getMessage());
            waitForInput();
        }
    }

    private void cancelAppointment() {
        try {
            System.out.println("\n--- CANCEL APPOINTMENT ---");
            
            System.out.println("\nCurrent Appointments:");
            appointmentCRUD.getAllAppointments().forEach(a -> 
                System.out.println(a.getId() + ": Patient " + a.getPatientId() + 
                                 " with Doctor " + a.getDoctorId() + " at " + a.getDateTime()));
            
            System.out.print("Enter Appointment ID to cancel: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());
            
            // Get current appointment
            Optional<Appointment> appointmentOpt = appointmentCRUD.getAppointment(appointmentId);
            if (appointmentOpt.isEmpty()) {
                System.out.println("Appointment not found.");
                waitForInput();
                return;
            }
            
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentCRUD.updateAppointment(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getDateTime(),
                appointment.getStatus()
            );
            
            System.out.println("Appointment cancelled successfully!");
            waitForInput();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid appointment ID");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error cancelling appointment: " + e.getMessage());
            waitForInput();
        }
    }

    private void completeAppointment() {
        try {
            System.out.println("\n--- COMPLETE APPOINTMENT ---");
            
            System.out.println("\nCurrent Appointments:");
            appointmentCRUD.getAllAppointments().forEach(a -> 
                System.out.println(a.getId() + ": Patient " + a.getPatientId() + 
                                 " with Doctor " + a.getDoctorId() + " at " + a.getDateTime()));
            
            System.out.print("Enter Appointment ID to complete: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());
            
            // Get current appointment
            Optional<Appointment> appointmentOpt = appointmentCRUD.getAppointment(appointmentId);
            if (appointmentOpt.isEmpty()) {
                System.out.println("Appointment not found.");
                waitForInput();
                return;
            }
            
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointmentCRUD.updateAppointment(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getDateTime(),
                appointment.getStatus()
            );
            
            System.out.println("Appointment marked as completed!");
            waitForInput();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid appointment ID");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error completing appointment: " + e.getMessage());
            waitForInput();
        }
    }

    private void viewPatientHistory() {
        try {
            System.out.println("\n--- PATIENT HISTORY ---");
            
            System.out.println("\nAvailable Patients:");
            patientCRUD.getAllPatients().forEach(p -> 
                System.out.println(p.getId() + ": " + p.getName() + " " + p.getLastName()));
            
            System.out.print("Enter Patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine());
            
            List<Appointment> history = appointmentHistory.getPatientHistory(patientId);
            
            if (history.isEmpty()) {
                System.out.println("No appointments found for this patient.");
            } else {
                System.out.println("\nAppointment History:");
                history.forEach(appointment -> {
                    System.out.println("ID: " + appointment.getId());
                    System.out.println("Doctor ID: " + appointment.getDoctorId());
                    System.out.println("Date/Time: " + appointment.getDateTime());
                    System.out.println("Status: " + appointment.getStatus());
                    System.out.println("----------------------");
                });
            }
            waitForInput();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid patient ID");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error viewing patient history: " + e.getMessage());
            waitForInput();
        }
    }

    private void viewDoctorSchedule() {
        try {
            System.out.println("\n--- DOCTOR SCHEDULE ---");
            
            System.out.println("\nAvailable Doctors:");
            doctorCRUD.getAllDoctors().forEach(d -> 
                System.out.println(d.getId() + ": " + d.getName() + " " + d.getLastName()));
            
            System.out.print("Enter Doctor ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine());
            
            List<Appointment> schedule = appointmentHistory.getDoctorSchedule(doctorId);
            
            if (schedule.isEmpty()) {
                System.out.println("No appointments found for this doctor.");
            } else {
                System.out.println("\nDoctor's Schedule:");
                schedule.forEach(appointment -> {
                    System.out.println("ID: " + appointment.getId());
                    System.out.println("Patient ID: " + appointment.getPatientId());
                    System.out.println("Date/Time: " + appointment.getDateTime());
                    System.out.println("Status: " + appointment.getStatus());
                    System.out.println("----------------------");
                });
            }
            waitForInput();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid doctor ID");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error viewing doctor schedule: " + e.getMessage());
            waitForInput();
        }
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Couldn't clear console");
        }
    }

    private void waitForInput() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}