package com.filtro.adapter.ui;

import com.filtro.application.usecase.appointment.AppointmentCRUD;
import com.filtro.application.usecase.appointment.AppointmentHistory;
import com.filtro.application.usecase.doctor.DoctorCRUD;
import com.filtro.application.usecase.patient.PatientCRUD;
import com.filtro.application.usecase.specialty.SpecialtyCRUD;
import com.filtro.infrastructure.Persistence.*;
import com.filtro.infrastructure.database.ConnectMysqlFactory;

import java.util.Scanner;

public class MainMenu {
    private final PatientCRUD patientCRUD;
    private final DoctorCRUD doctorCRUD;
    private final AppointmentCRUD appointmentCRUD;
    private final AppointmentHistory appointmentHistory;
    private final Scanner scanner;

    public MainMenu() {
        // Initialize repositories
        PatientPersistenceImpl patientRepository = new PatientPersistenceImpl(ConnectMysqlFactory.crearConexion());
        DoctorPersistenceImpl doctorRepository = new DoctorPersistenceImpl(ConnectMysqlFactory.crearConexion());
        AppointmentPersistenceImpl appointmentRepository = new AppointmentPersistenceImpl(ConnectMysqlFactory.crearConexion());
        
        // Initialize use cases
        this.patientCRUD = new PatientCRUD(patientRepository);
        this.doctorCRUD = new DoctorCRUD(doctorRepository);
        this.appointmentCRUD = new AppointmentCRUD(appointmentRepository);
        this.appointmentHistory = new AppointmentHistory(appointmentRepository);
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== MEDICAL APPOINTMENT SYSTEM ===");
            System.out.println("1. Patient Portal");
            System.out.println("2. Doctor Portal");
            System.out.println("3. Appointment Portal");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    showPatientMenu();
                    break;
                case "2":
                    showDoctorMenu();
                    break;
                case "3":
                    showAppointmentMenu();
                    break;
                case "4":
                    System.out.println("Exiting system...");
                    return;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 4.");
            }
        }
    }

    private void showPatientMenu() {
        PatientMenu patientMenu = new PatientMenu(patientCRUD);
        patientMenu.showMenu();
    }

    private void showDoctorMenu() {
        SpecialtyCRUD specialtyCRUD = new SpecialtyCRUD(new SpecialtyPersistenceImpl(ConnectMysqlFactory.crearConexion()));
        DoctorMenu doctorMenu = new DoctorMenu(doctorCRUD, specialtyCRUD, scanner);
        doctorMenu.showMenu();
    }

    private void showAppointmentMenu() {
        AppointmentMenu appointmentMenu = new AppointmentMenu(appointmentCRUD, appointmentHistory, patientCRUD, doctorCRUD, scanner);
        appointmentMenu.showMenu();
    }
}