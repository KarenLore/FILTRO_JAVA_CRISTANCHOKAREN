package com.filtro.adapter.ui;

import com.filtro.Main;
import com.filtro.application.usecase.doctor.DoctorCRUD;
import com.filtro.application.usecase.specialty.SpecialtyCRUD;
import com.filtro.domain.entities.Doctor;
import com.filtro.infrastructure.Persistence.DoctorPersistenceImpl;
import com.filtro.infrastructure.Persistence.SpecialtyPersistenceImpl;
import com.filtro.infrastructure.database.ConnectMysqlFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DoctorMenu {
    public static void main(String[] args) {
        // Initialize repositories with ConnectMysqlFactory
        DoctorPersistenceImpl doctorRepository = new DoctorPersistenceImpl(ConnectMysqlFactory.crearConexion());
        SpecialtyPersistenceImpl specialtyRepository = new SpecialtyPersistenceImpl(ConnectMysqlFactory.crearConexion());
        
        // Initialize use cases
        DoctorCRUD doctorCRUD = new DoctorCRUD(doctorRepository);
        SpecialtyCRUD specialtyCRUD = new SpecialtyCRUD(specialtyRepository);
        
        try (Scanner sc = new Scanner(System.in)) {
            new DoctorMenu(doctorCRUD, specialtyCRUD, sc).showMenu();
        } catch (Exception e) {
            System.out.println("Error: Please restart the application");
            e.printStackTrace();
        }
    }

    private final DoctorCRUD doctorCRUD;
    private final SpecialtyCRUD specialtyCRUD;
    private final Scanner scanner;

    public DoctorMenu(DoctorCRUD doctorCRUD, SpecialtyCRUD specialtyCRUD, Scanner scanner) {
        this.doctorCRUD = doctorCRUD;
        this.specialtyCRUD = specialtyCRUD;
        this.scanner = scanner;
    }

    public void showMenu() {
        String menu;
        do {
            clearConsole();
            System.out.println("=== DOCTOR MANAGEMENT ===");
            System.out.println("1. Add New Doctor");
            System.out.println("2. View All Doctors");
            System.out.println("3. Update Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("5. View Doctors by Specialty");
            System.out.println("6. Return to Main Menu");
            System.out.print("Select an option: ");
            menu = scanner.nextLine();
            System.out.println("");

            switch (menu) {
                case "1" -> addDoctor();
                case "2" -> viewAllDoctors();
                case "3" -> updateDoctor();
                case "4" -> deleteDoctor();
                case "5" -> viewDoctorsBySpecialty();
                case "6" -> {
                    System.out.println("Returning to main menu...");
                    Main.main(new String[]{});
                }
                default -> {
                    System.out.println("Invalid option, please try again");
                    waitForInput();
                }
            }
        } while (!menu.equals("6"));
    }

    private void addDoctor() {
        try {
            System.out.println("\n--- ADD NEW DOCTOR ---");
            
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.println("\nAvailable Specialties:");
            specialtyCRUD.getAllSpecialties().forEach(s -> 
                System.out.println(s.getId() + ": " + s.getName()));
            
            System.out.print("Enter Specialty ID: ");
            int specialtyId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Schedule Start (HH:MM): ");
            String scheduleStart = scanner.nextLine();
            
            System.out.print("Enter Schedule End (HH:MM): ");
            String scheduleEnd = scanner.nextLine();
            
            doctorCRUD.createDoctor(firstName, lastName, specialtyId, scheduleStart, scheduleEnd);
            System.out.println("Doctor added successfully!");
            waitForInput();
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numeric IDs");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error adding doctor: " + e.getMessage());
            waitForInput();
        }
    }

    private void viewAllDoctors() {
        try {
            System.out.println("\n--- ALL DOCTORS ---");
            doctorCRUD.getAllDoctors().forEach(doctor -> {
                System.out.println("ID: " + doctor.getId());
                System.out.println("Name: " + doctor.getName() + " " + doctor.getLastName());
                System.out.println("Specialty ID: " + doctor.getSpecialtyId());
                System.out.println("Schedule: " + doctor.getScheduleStart() + " - " + doctor.getScheduleEnd());
                System.out.println("----------------------");
            });
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error viewing doctors: " + e.getMessage());
            waitForInput();
        }
    }

    private void updateDoctor() {
        try {
            System.out.println("\n--- UPDATE DOCTOR ---");
            
            System.out.println("\nCurrent Doctors:");
            doctorCRUD.getAllDoctors().forEach(d -> 
                System.out.println(d.getId() + ": " + d.getName() + " " + d.getLastName()));
            
            System.out.print("Enter Doctor ID to update: ");
            int doctorId = Integer.parseInt(scanner.nextLine());
            
            // Get current doctor info
            Optional<Doctor> doctorOpt = doctorCRUD.getDoctor(doctorId);
            if (doctorOpt.isEmpty()) {
                System.out.println("Doctor not found.");
                waitForInput();
                return;
            }
            
            Doctor doctor = doctorOpt.get();
            
            System.out.println("\nCurrent Information:");
            System.out.println("1. First Name: " + doctor.getName());
            System.out.println("2. Last Name: " + doctor.getLastName());
            System.out.println("3. Specialty ID: " + doctor.getSpecialtyId());
            System.out.println("4. Schedule Start: " + doctor.getScheduleStart());
            System.out.println("5. Schedule End: " + doctor.getScheduleEnd());
            
            System.out.println("\nEnter the number of the field to update (1-5) or 0 to save:");
            
            while (true) {
                System.out.print("Field number (0 to save): ");
                String field = scanner.nextLine();
                
                if (field.equals("0")) {
                    doctorCRUD.updateDoctor(doctor.getId(), doctor.getName(), doctor.getLastName(), 
                                          doctor.getSpecialtyId(), doctor.getScheduleStart(), doctor.getScheduleEnd());
                    System.out.println("Doctor updated successfully!");
                    break;
                }
                
                switch (field) {
                    case "1":
                        System.out.print("New First Name: ");
                        doctor.setName(scanner.nextLine());
                        break;
                    case "2":
                        System.out.print("New Last Name: ");
                        doctor.setLastName(scanner.nextLine());
                        break;
                    case "3":
                        System.out.print("New Specialty ID: ");
                        doctor.setSpecialtyId(Integer.parseInt(scanner.nextLine()));
                        break;
                    case "4":
                        System.out.print("New Schedule Start (HH:MM): ");
                        doctor.setScheduleStart(scanner.nextLine());
                        break;
                    case "5":
                        System.out.print("New Schedule End (HH:MM): ");
                        doctor.setScheduleEnd(scanner.nextLine());
                        break;
                    default:
                        System.out.println("Invalid field number.");
                }
            }
            waitForInput();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numeric values");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error updating doctor: " + e.getMessage());
            waitForInput();
        }
    }

    private void deleteDoctor() {
        try {
            System.out.println("\n--- DELETE DOCTOR ---");
            
            System.out.println("\nCurrent Doctors:");
            doctorCRUD.getAllDoctors().forEach(d -> 
                System.out.println(d.getId() + ": " + d.getName() + " " + d.getLastName()));
            
            System.out.print("Enter Doctor ID to delete: ");
            int doctorId = Integer.parseInt(scanner.nextLine());
            
            doctorCRUD.deleteDoctor(doctorId);
            System.out.println("Doctor deleted successfully!");
            waitForInput();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid doctor ID");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
            waitForInput();
        }
    }

    private void viewDoctorsBySpecialty() {
        try {
            System.out.println("\n--- DOCTORS BY SPECIALTY ---");
            
            System.out.println("\nAvailable Specialties:");
            specialtyCRUD.getAllSpecialties().forEach(s -> 
                System.out.println(s.getId() + ": " + s.getName()));
            
            System.out.print("Enter Specialty ID: ");
            int specialtyId = Integer.parseInt(scanner.nextLine());
            
            List<Doctor> doctors = doctorCRUD.getDoctorsBySpecialty(specialtyId);
            
            if (doctors.isEmpty()) {
                System.out.println("No doctors found for this specialty.");
            } else {
                doctors.forEach(doctor -> {
                    System.out.println("ID: " + doctor.getId());
                    System.out.println("Name: " + doctor.getName() + " " + doctor.getLastName());
                    System.out.println("Schedule: " + doctor.getScheduleStart() + " - " + doctor.getScheduleEnd());
                    System.out.println("----------------------");
                });
            }
            waitForInput();
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid specialty ID");
            waitForInput();
        } catch (Exception e) {
            System.out.println("Error viewing doctors by specialty: " + e.getMessage());
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