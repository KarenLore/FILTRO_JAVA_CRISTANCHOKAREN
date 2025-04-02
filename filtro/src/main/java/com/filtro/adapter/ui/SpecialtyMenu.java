package com.filtro.adapter.ui;

import com.filtro.application.usecase.specialty.SpecialtyCRUD;
import com.filtro.domain.entities.Specialty;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class SpecialtyMenu {
    private final SpecialtyCRUD specialtyCRUD;
    private final Scanner scanner;

    public SpecialtyMenu(SpecialtyCRUD specialtyCRUD, Scanner scanner) {
        this.specialtyCRUD = specialtyCRUD;
        this.scanner = scanner;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== SPECIALTY MANAGEMENT ===");
            System.out.println("1. Add New Specialty");
            System.out.println("2. View All Specialties");
            System.out.println("3. Update Specialty");
            System.out.println("4. Delete Specialty");
            System.out.println("5. Return to Admin Menu");
            System.out.print("Select an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    addSpecialty();
                    break;
                case 2:
                    viewAllSpecialties();
                    break;
                case 3:
                    updateSpecialty();
                    break;
                case 4:
                    deleteSpecialty();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addSpecialty() {
        System.out.println("\n--- ADD NEW SPECIALTY ---");
        
        System.out.print("Enter Specialty Name: ");
        String name = scanner.nextLine();
        
        specialtyCRUD.createSpecialty(name);
        System.out.println("Specialty added successfully!");
    }

    private void viewAllSpecialties() {
        System.out.println("\n--- ALL SPECIALTIES ---");
        List<Specialty> specialties = specialtyCRUD.getAllSpecialties();
        
        if (specialties.isEmpty()) {
            System.out.println("No specialties found.");
            return;
        }
        
        specialties.forEach(specialty -> {
            System.out.println("ID: " + specialty.getId());
            System.out.println("Name: " + specialty.getName());
            System.out.println("----------------------");
        });
    }

    private void updateSpecialty() {
        System.out.println("\n--- UPDATE SPECIALTY ---");
        
        System.out.println("\nCurrent Specialties:");
        specialtyCRUD.getAllSpecialties().forEach(s -> 
            System.out.println(s.getId() + ": " + s.getName()));
        
        System.out.print("Enter Specialty ID to update: ");
        int specialtyId = scanner.nextInt();
        scanner.nextLine();
        
        Optional<Specialty> specialtyOpt = specialtyCRUD.getSpecialty(specialtyId);
        if (specialtyOpt.isEmpty()) {
            System.out.println("Specialty not found.");
            return;
        }
        
        Specialty specialty = specialtyOpt.get();
        
        System.out.println("\nCurrent Information:");
        System.out.println("1. Name: " + specialty.getName());
        
        System.out.print("\nEnter new name or press Enter to keep current: ");
        String newName = scanner.nextLine();
        
        if (!newName.isEmpty()) {
            specialty.setName(newName);
            specialtyCRUD.updateSpecialty(specialty.getId(), specialty.getName());
            System.out.println("Specialty updated successfully!");
        } else {
            System.out.println("No changes made.");
        }
    }

    private void deleteSpecialty() {
        System.out.println("\n--- DELETE SPECIALTY ---");
        
        System.out.println("\nCurrent Specialties:");
        specialtyCRUD.getAllSpecialties().forEach(s -> 
            System.out.println(s.getId() + ": " + s.getName()));
        
        System.out.print("Enter Specialty ID to delete: ");
        int specialtyId = scanner.nextInt();
        scanner.nextLine();
        
        specialtyCRUD.deleteSpecialty(specialtyId);
        System.out.println("Specialty deleted successfully!");
    }
}
