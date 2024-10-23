package lk.auroraskincare.clinic.config;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.springframework.stereotype.Component;

import lk.auroraskincare.clinic.entity.*;

@Component
public class AppointmentApp {

    private static Long patientIdCounter = 1L;
    private static Long appointmentIdCounter = 1L;
    private static List<PatientEntity> patients = new ArrayList<>();
    private static List<AppointmentEntity> appointments = new ArrayList<>();
    private static List<DermatologistEntity> dermatologists = new ArrayList<>();
    private static List<TreatmentEntity> treatments = new ArrayList<>();

    public void start() {
        initializeSampleData();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            printHeader("Clinic Management System");
            System.out.println("1. Make Appointment");
            System.out.println("2. Update Appointment");
            System.out.println("3. View Appointments by Date");
            System.out.println("4. Search Appointment (by Name or ID)");
            System.out.println("5. Add Treatment");
            System.out.println("6. Generate Invoice");
            System.out.println("7. Exit");
            printLine();
            System.out.print(" Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    printSubHeader("Make Appointment");
                    makeAppointment(scanner);
                    break;
                case 2:
                    printSubHeader("Update Appointment");
                    updateAppointment(scanner);
                    break;
                case 3:
                    printSubHeader("View Appointments by Date");
                    viewAppointmentsByDate(scanner);
                    break;
                case 4:
                    printSubHeader("Search Appointment");
                    searchAppointment(scanner);
                    break;
                case 5:
                    printSubHeader("Add Treatment");
                    addTreatment(scanner);
                    break;
                case 6:
                    printSubHeader("Generate Invoice");
                    generateInvoice(scanner);
                    break;
                case 7:
                    System.out.println("Exiting... Thank you for using the system.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }

    // Utility function to print headers
    private static void printHeader(String title) {
        System.out.println("\n");
        System.out.println("===============================================");
        System.out.println("               " + title);
        System.out.println("===============================================\n");
    }

    // Utility function to print sub-headers
    private static void printSubHeader(String subTitle) {
        System.out.println("\n");
        System.out.println("-------------------------------------------------");
        System.out.println("                 " + subTitle);
        System.out.println("-------------------------------------------------");
    }

    // Utility function to print lines for cleaner separation
    private static void printLine() {
        System.out.println("-------------------------------------------------");
    }

    private static void initializeSampleData() {
        // Sample Dermatologists
        DermatologistEntity dermatologist1 = new DermatologistEntity();
        dermatologist1.setId(1L);
        dermatologist1.setName("Dr. Smith");
        dermatologists.add(dermatologist1);

        DermatologistEntity dermatologist2 = new DermatologistEntity();
        dermatologist2.setId(2L);
        dermatologist2.setName("Dr. Johnson");
        dermatologists.add(dermatologist2);

        // Sample Treatments
        for (TreatmentType type : TreatmentType.values()) {
            TreatmentEntity treatment = new TreatmentEntity();
            treatment.setId((long) (treatments.size() + 1));
            treatment.setTreatmentType(type);
            treatments.add(treatment);
        }
    }

    private static void makeAppointment(Scanner scanner) {
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter patient NIC: ");
        String nic = scanner.nextLine();
        System.out.print("Enter patient email: ");
        String email = scanner.nextLine();
        System.out.print("Enter patient phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.println("Available consultation times:");
        for (ConsultationTime time : ConsultationTime.values()) {
            System.out.println(time.getTimeSlot());
        }

        System.out.print("Enter appointment time (HH:mm): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());

        if (!validateConsultationTime(date.getDayOfWeek(), time)) {
            System.out.println("Error: The selected date and time do not match any available consultation times.");
            return;
        }

        System.out.println("\nSelect a dermatologist:");
        for (DermatologistEntity dermatologist : dermatologists) {
            System.out.println(dermatologist.getId() + ". " + dermatologist.getName());
        }
        System.out.print("\nYour choice: ");
        Long dermatologistId = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        DermatologistEntity selectedDermatologist = dermatologists.stream()
                .filter(d -> d.getId().equals(dermatologistId))
                .findFirst()
                .orElse(null);

        if (selectedDermatologist == null) {
            System.out.println("Invalid dermatologist selected.");
            return;
        }

        System.out.print("\nAccept registration fee of " + AppointmentEntity.getRegistrationFee() + " (yes/no)? ");
        String registrationFeeResponse = scanner.nextLine().toLowerCase();

        if (!registrationFeeResponse.equals("yes")) {
            System.out.println("Appointment cancelled.");
            return;
        }

        PatientEntity patient = new PatientEntity();
        patient.setId(patientIdCounter++);
        patient.setName(name);
        patient.setNic(nic);
        patient.setEmail(email);
        patient.setPhone(phone);
        patients.add(patient);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(appointmentIdCounter++);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setPatientEntity(patient);
        appointment.setDermatologistEntity(selectedDermatologist);
        appointment.setRegistrationFeeAccepted(true);
        appointments.add(appointment);

        System.out.println("\nAppointment made successfully.");
    }

    private static boolean validateConsultationTime(DayOfWeek dayOfWeek, LocalTime time) {
        for (ConsultationTime consultationTime : ConsultationTime.values()) {
            if (consultationTime.name().equals(dayOfWeek.name()) &&
                (time.equals(consultationTime.getStartTime()) || time.equals(consultationTime.getEndTime()) ||
                (time.isAfter(consultationTime.getStartTime()) && time.isBefore(consultationTime.getEndTime())))) {
                return true;
            }
        }
        return false;
    }

    private static void addTreatment(Scanner scanner) {
        System.out.print("Enter appointment ID to add treatment: ");
        Long appointmentId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        AppointmentEntity appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.println("\nSelect a treatment to add:");
        for (TreatmentEntity treatment : treatments) {
            System.out.println(treatment.getId() + ". " + treatment.getTreatmentType());
        }
        System.out.print("\nYour choice: ");
        Long treatmentId = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        TreatmentEntity selectedTreatment = treatments.stream()
                .filter(t -> t.getId().equals(treatmentId))
                .findFirst()
                .orElse(null);

        if (selectedTreatment == null) {
            System.out.println("Invalid treatment selected.");
            return;
        }

        appointment.setTreatmentEntity(selectedTreatment);
        System.out.println("\nTreatment added to the appointment successfully.");
    }

    private static void updateAppointment(Scanner scanner) {
        System.out.print("Enter appointment ID to update: ");
        Long appointmentId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        AppointmentEntity appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.print("Enter new appointment date (YYYY-MM-DD): ");
        LocalDate newDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter new appointment time (HH:mm): ");
        LocalTime newTime = LocalTime.parse(scanner.nextLine());

        appointment.setDate(newDate);
        appointment.setTime(newTime);
        System.out.println("\nAppointment updated successfully.");
    }

    private static void viewAppointmentsByDate(Scanner scanner) {
        System.out.print("Enter date to view appointments (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        List<AppointmentEntity> appointmentsOnDate = appointments.stream()
                .filter(a -> a.getDate().isEqual(date))
                .toList();

        if (appointmentsOnDate.isEmpty()) {
            System.out.println("No appointments found for this date.");
        } else {
            printSubHeader("Appointments on " + date);
            for (AppointmentEntity appointment : appointmentsOnDate) {
                System.out.println("Appointment ID: " + appointment.getId() +
                        ", Patient: " + appointment.getPatientEntity().getName() +
                        ", Time: " + appointment.getTime());
            }
        }
    }

    private static void searchAppointment(Scanner scanner) {
        System.out.print("Search by (1) Name or (2) Appointment ID: ");
        int searchOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (searchOption) {
            case 1:
                System.out.print("Enter patient name: ");
                String name = scanner.nextLine();
                searchAppointmentByName(name);
                break;
            case 2:
                System.out.print("Enter appointment ID: ");
                Long id = scanner.nextLong();
                searchAppointmentById(id);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void searchAppointmentByName(String name) {
        List<AppointmentEntity> foundAppointments = appointments.stream()
                .filter(a -> a.getPatientEntity().getName().equalsIgnoreCase(name))
                .toList();

        if (foundAppointments.isEmpty()) {
            System.out.println("No appointments found for the given name.");
        } else {
            printSubHeader("Appointments for " + name);
            for (AppointmentEntity appointment : foundAppointments) {
                System.out.println("Appointment ID: " + appointment.getId() +
                        ", Date: " + appointment.getDate() +
                        ", Time: " + appointment.getTime());
            }
        }
    }

    private static void searchAppointmentById(Long id) {
        AppointmentEntity appointment = findAppointmentById(id);
        if (appointment == null) {
            System.out.println("No appointment found for the given ID.");
        } else {
            printSubHeader("Appointment Details for ID " + id);
            System.out.println("Patient: " + appointment.getPatientEntity().getName());
            System.out.println("Date: " + appointment.getDate());
            System.out.println("Time: " + appointment.getTime());
        }
    }

    private static void generateInvoice(Scanner scanner) {
        System.out.print("Enter appointment ID to generate invoice: ");
        Long appointmentId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        AppointmentEntity appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        if (appointment.getTreatmentEntity() == null) {
            System.out.println("No treatment found for this appointment. Please add a treatment first.");
            return;
        }

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId((long) (appointment.getInvoices() != null ? appointment.getInvoices().size() + 1 : 1));
        invoice.setAppointmentEntity(appointment);

        // Get the treatment price (totalFee)
        double totalFee = appointment.getTreatmentEntity().getTreatmentType().getPrice();
        invoice.setTotalFee(totalFee);

        // Calculate tax (2.5% of totalFee)
        double taxRate = 0.025;
        double tax = totalFee * taxRate;
        invoice.setTax(tax);

        // Get the registration fee from the appointment
        double registrationFee = AppointmentEntity.getRegistrationFee();

        // Calculate final amount: tax + totalFee + registrationFee
        double finalAmount = tax + totalFee + registrationFee;
        invoice.setFinalAmount(finalAmount);

        // Ensure the invoice list is initialized in the appointment
        if (appointment.getInvoices() == null) {
            appointment.setInvoices(new ArrayList<>());
        }
        appointment.getInvoices().add(invoice);

        // Generate detailed invoice printout
        printSubHeader("Invoice");
        System.out.println("Patient Name: " + appointment.getPatientEntity().getName());
        System.out.println("Patient Email: " + appointment.getPatientEntity().getEmail());
        System.out.println("Dermatologist: " + appointment.getDermatologistEntity().getName());
        System.out.println("Treatment: " + appointment.getTreatmentEntity().getTreatmentType());
        System.out.println("Appointment Date: " + appointment.getDate());
        System.out.println("Appointment Time: " + appointment.getTime());
        System.out.println("--------------------------------------------");
        System.out.println("Registration Fee: Rs." + String.format("%.2f", registrationFee));
        System.out.println("Treatment Price: Rs." + String.format("%.2f", totalFee));
        System.out.println("Tax (2.5%): Rs." + String.format("%.2f", tax));
        System.out.println("--------------------------------------------");
        System.out.println("Total Amount: Rs." + String.format("%.2f", finalAmount));
        System.out.println("--------------------------------------------");
        System.out.println("Thank you for choosing our clinic! We appreciate your business.");
        System.out.println("--------------------------------------------\n");
    }

    private static AppointmentEntity findAppointmentById(Long id) {
        return appointments.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
