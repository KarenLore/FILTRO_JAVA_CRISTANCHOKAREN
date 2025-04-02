package com.filtro.adapter.ui;

import com.filtro.application.usecase.patient.PatientCRUD;
import com.filtro.domain.entities.Patient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PatientMenu {
    private final PatientCRUD patientCRUD;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    public PatientMenu(PatientCRUD patientCRUD) {
        this.patientCRUD = patientCRUD;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== PATIENT PORTAL ===");
            System.out.println("1. Register New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Return to Main Menu");
            System.out.print("Select an option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    registerPatient();
                    break;
                case "2":
                    viewAllPatients();
                    break;
                case "3":
                    updatePatient();
                    break;
                case "4":
                    deletePatient();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    waitForInput();
            }
        }
    }

    private void registerPatient() {
        System.out.println("\n--- REGISTER NEW PATIENT ---");
        
        String firstName = getNonEmptyInput("Enter First Name: ");
        String lastName = getNonEmptyInput("Enter Last Name: ");
        LocalDate birthDate = getValidDateInput("Enter Birth Date (yyyy-MM-dd): ");
        String address = getInput("Enter Address: ");
        String phone = getPhoneInput();
        String email = getEmailInput();
        
        try {
            Patient patient = patientCRUD.createPatient(firstName, lastName, birthDate, address, phone, email);
            System.out.println("\nPatient registered successfully! ID: " + patient.getId());
            waitForInput();
        } catch (Exception e) {
            System.out.println("\nError registering patient: " + e.getMessage());
            waitForInput();
        }
    }

    private void viewAllPatients() {
        System.out.println("\n--- ALL PATIENTS ---");
        List<Patient> patients = patientCRUD.getAllPatients();
        
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            patients.forEach(patient -> {
                System.out.println("\nID: " + patient.getId());
                System.out.println("Name: " + patient.getName() + " " + patient.getLastName());
                System.out.println("Birth Date: " + patient.getBirthDate().format(dateFormatter));
                System.out.println("Address: " + patient.getAddress());
                System.out.println("Phone: " + patient.getPhone());
                System.out.println("Email: " + patient.getEmail());
                System.out.println("----------------------");
            });
        }
        waitForInput();
    }

    private void updatePatient() {
        System.out.println("\n--- UPDATE PATIENT ---");
        
        if (patientCRUD.getAllPatients().isEmpty()) {
            System.out.println("No patients available to update.");
            waitForInput();
            return;
        }

        System.out.println("\nCurrent Patients:");
        patientCRUD.getAllPatients().forEach(p -> 
            System.out.println(p.getId() + ": " + p.getName() + " " + p.getLastName()));
        
        Integer patientId = getIntegerInput("\nEnter Patient ID to update: ");
        if (patientId == null) {
            return;
        }

        Optional<Patient> patientOpt = patientCRUD.getPatient(patientId);
        if (patientOpt.isEmpty()) {
            System.out.println("Patient not found.");
            waitForInput();
            return;
        }
        
        Patient patient = patientOpt.get();
        
        System.out.println("\nCurrent Information:");
        System.out.println("1. First Name: " + patient.getName());
        System.out.println("2. Last Name: " + patient.getLastName());
        System.out.println("3. Birth Date: " + patient.getBirthDate().format(dateFormatter));
        System.out.println("4. Address: " + patient.getAddress());
        System.out.println("5. Phone: " + patient.getPhone());
        System.out.println("6. Email: " + patient.getEmail());
        
        System.out.println("\nEnter the number of the field to update (1-6) or 0 to save:");
        
        while (true) {
            System.out.print("Field number (0 to save): ");
            String fieldInput = scanner.nextLine();
            
            if (fieldInput.equals("0")) {
                try {
                    patientCRUD.updatePatient(
                        patient.getId(), 
                        patient.getName(), 
                        patient.getLastName(),
                        patient.getBirthDate(), 
                        patient.getAddress(), 
                        patient.getPhone(), 
                        patient.getEmail()
                    );
                    System.out.println("\nPatient updated successfully!");
                } catch (Exception e) {
                    System.out.println("\nError updating patient: " + e.getMessage());
                }
                waitForInput();
                break;
            }
            
            try {
                int field = Integer.parseInt(fieldInput);
                switch (field) {
                    case 1:
                        patient.setName(getNonEmptyInput("New First Name: "));
                        break;
                    case 2:
                        patient.setLastName(getNonEmptyInput("New Last Name: "));
                        break;
                    case 3:
                        patient.setBirthDate(getValidDateInput("New Birth Date (yyyy-MM-dd): "));
                        break;
                    case 4:
                        patient.setAddress(getInput("New Address: "));
                        break;
                    case 5:
                        patient.setPhone(getPhoneInput());
                        break;
                    case 6:
                        patient.setEmail(getEmailInput());
                        break;
                    default:
                        System.out.println("Invalid field number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void deletePatient() {
        System.out.println("\n--- DELETE PATIENT ---");
        
        if (patientCRUD.getAllPatients().isEmpty()) {
            System.out.println("No patients available to delete.");
            waitForInput();
            return;
        }

        System.out.println("\nCurrent Patients:");
        patientCRUD.getAllPatients().forEach(p -> 
            System.out.println(p.getId() + ": " + p.getName() + " " + p.getLastName()));
        
        Integer patientId = getIntegerInput("\nEnter Patient ID to delete: ");
        if (patientId == null) {
            return;
        }

        try {
            patientCRUD.deletePatient(patientId);
            System.out.println("\nPatient deleted successfully!");
        } catch (Exception e) {
            System.out.println("\nError deleting patient: " + e.getMessage());
        }
        waitForInput();
    }

    // Helper methods for input validation
    private String getNonEmptyInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error: This field cannot be empty.");
        }
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private LocalDate getValidDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateInput = scanner.nextLine().trim();
                return LocalDate.parse(dateInput, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please use yyyy-MM-dd (e.g., 2025-04-02)");
            }
        }
    }

    private String getPhoneInput() {
        while (true) {
            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine().trim();
            if (phone.matches("\\d{7,15}")) { // Basic phone number validation
                return phone;
            }
            System.out.println("Error: Phone number should contain 7-15 digits.");
        }
    }

    private String getEmailInput() {
        while (true) {
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            if (email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // Basic email validation
                return email;
            }
            System.out.println("Error: Please enter a valid email address (e.g., user@example.com)");
        }
    }

    private Integer getIntegerInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    private void waitForInput() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}