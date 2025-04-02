package com.filtro.adapter.ui;

import com.filtro.Main;
import com.filtro.application.usecase.doctor.DoctorCRUD;
import com.filtro.application.usecase.specialty.SpecialtyCRUD;
import com.filtro.infrastructure.database.ConnectMysqlFactory;
import com.filtro.infrastructure.Persistence.DoctorPersistenceImpl;
import com.filtro.infrastructure.Persistence.SpecialtyPersistenceImpl;
import java.util.Scanner;

public class AdminMenu {

    private static DoctorCRUD doctorCRUD;
    private static SpecialtyCRUD specialtyCRUD;

    public static void main(String[] args) {
        DoctorPersistenceImpl doctorRepository = new DoctorPersistenceImpl(ConnectMysqlFactory.crearConexion());
        SpecialtyPersistenceImpl specialtyRepository = new SpecialtyPersistenceImpl(ConnectMysqlFactory.crearConexion());

        doctorCRUD = new DoctorCRUD(doctorRepository);
        specialtyCRUD = new SpecialtyCRUD(specialtyRepository);

        try (Scanner sc = new Scanner(System.in)) {
            String showMenu;
            do {
                clearConsole();
                System.out.println("=== ADMIN showMenu ===");
                System.out.println("1. Manage Specialties");
                System.out.println("2. Manage Doctors");
                System.out.println("3. Return to Main showMenu");
                System.out.print("Select an option: ");
                showMenu = sc.nextLine();
                System.out.println("");

                switch (showMenu) {
                    case "1" -> manageSpecialties(sc);
                    case "2" -> manageDoctors(sc);
                    case "3" -> returnToMainshowMenu(args);
                    default -> {
                        System.out.println("Invalid option, please try again");
                        waitForInput(sc);
                    }
                }
            } while (!showMenu.equals("3"));
        } catch (Exception e) {
            System.out.println("Error: Please restart the application");
            e.printStackTrace();
        }
    }

    private static void manageSpecialties(Scanner sc) {
        try {
            new SpecialtyMenu(specialtyCRUD, sc).showMenu();
        } catch (Exception e) {
            System.out.println("Error managing specialties: " + e.getMessage());
            waitForInput(sc);
        }
    }

    private static void manageDoctors(Scanner sc) {
        try {
            new DoctorMenu(doctorCRUD, specialtyCRUD, sc).showMenu();
        } catch (Exception e) {
            System.out.println("Error managing doctors: " + e.getMessage());
            waitForInput(sc);
        }
    }

    private static void returnToMainshowMenu(String[] args) {
        System.out.println("Returning to main showMenu...");
        Main.main(args);
    }

    private static void clearConsole() {
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

    private static void waitForInput(Scanner sc) {
        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }
}