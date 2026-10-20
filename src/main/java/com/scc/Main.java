package com.scc;

import com.scc.dao.ScenarioDao;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner input = new Scanner(System.in);
    static ScenarioDao dao = new ScenarioDao();

    public static void main(String[] args) {

        try {
            dao.createTable();
            int choice;

            do {
                System.out.println("\n========================================");
                System.out.println("     HOTEL ROOM RESERVATION SYSTEM");
                System.out.println("========================================");
                System.out.println("[1] Add Record");
                System.out.println("[2] View Records");
                System.out.println("[3] Exit System");
                System.out.println("========================================");
                System.out.print("Enter choice: ");

                choice = readInt();

                switch (choice) {
                    case 1:
                        addRecord();
                        break;

                    case 2:
                        viewRecords();
                        break;

                    case 3:
                        System.out.println("Thank you for using the system!");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } while (choice != 3);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        input.close();
    }

    static void addRecord() throws SQLException {

        input.nextLine();

        System.out.println("\n--- ADD RESERVATION ---");

        System.out.print("Enter guest name: ");
        String guestName = input.nextLine().trim();

        if (guestName.isEmpty()) {
            System.out.println("Guest name cannot be empty.");
            return;
        }

        System.out.println("\nRoom Types:");
        System.out.println("1. Single");
        System.out.println("2. Double");
        System.out.println("3. Suite");
        System.out.print("Choose room type: ");

        int roomChoice = readInt();
        String roomType;

        switch (roomChoice) {
            case 1:
                roomType = "SINGLE";
                break;
            case 2:
                roomType = "DOUBLE";
                break;
            case 3:
                roomType = "SUITE";
                break;
            default:
                System.out.println("Invalid room type.");
                return;
        }

        System.out.print("Enter nightly rate: ");
        double nightlyRate = readDouble();

        if (nightlyRate <= 0) {
            System.out.println("Rate must be greater than zero.");
            return;
        }

        input.nextLine();

        System.out.print("Payment status (PAID/PENDING): ");
        String payment = input.nextLine().trim().toUpperCase();

        if (!payment.equals("PAID") && !payment.equals("PENDING")) {
            System.out.println("Invalid payment status.");
            return;
        }

        dao.addReservation(
                guestName, roomType, nightlyRate, payment
        );

        System.out.println("SUCCESS: Reservation recorded!");
    }

    static void viewRecords() throws SQLException {

        List<String[]> records = dao.getAllReservations();

        System.out.println("\n================ ALL RESERVATIONS ================");

        System.out.printf(
                "%-5s %-20s %-12s %15s %-10s%n",
                "ID", "GUEST NAME", "ROOM TYPE",
                "NIGHTLY RATE", "PAYMENT"
        );

        System.out.println(
                "---------------------------------------------------------------"
        );

        for (String[] record : records) {
            System.out.printf(
                    "%-5s %-20s %-12s %15s %-10s%n",
                    record[0], record[1], record[2],
                    record[3], record[4]
            );
        }

        System.out.println("---------------------------------------------------------------");
        System.out.println("Total Records: " + records.size());
    }

    static int readInt() {
        while (!input.hasNextInt()) {
            System.out.print("Enter a valid whole number: ");
            input.next();
        }
        return input.nextInt();
    }

    static double readDouble() {
        while (!input.hasNextDouble()) {
            System.out.print("Enter a valid amount: ");
            input.next();
        }
        return input.nextDouble();
    }
}
