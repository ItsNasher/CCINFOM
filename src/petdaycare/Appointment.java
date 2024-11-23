package petdaycare;

import java.sql.*;
import java.util.*;

public class Appointment {
    public int appointmentID;
    public String appointmentDate;
    public int pet_id;
    public String petName;
    public int serviceID;
    public int employeeID;

    public void addAppointment() {
        Scanner sc = new Scanner(System.in);
         try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            // Input details for the appointment details
            System.out.print("Enter Appointment ID: ");
            appointmentID = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
            appointmentDate = sc.nextLine();
            sc.nextLine();

            System.out.print("Enter Pet Name: ");
            petName = sc.nextLine();
            sc.nextLine();

            System.out.print("Enter Service ID: ");
            serviceID = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Employee ID: ");
            employeeID = sc.nextInt();
            sc.nextLine();

            // Prepare SQL INSERT statement
            String sql = "INSERT INTO appointment_record (Appointment_ID, Appointment_Date, PetName, ServiceID, EmployeeID) " +
            "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = db.conn.prepareStatement(sql);

            // Set the values for the prepared statement
            pstmt.setInt(1, appointmentID);
            pstmt.setString(2, appointmentDate);
            pstmt.setString(3, petName);
            pstmt.setInt(4, serviceID);
            pstmt.setInt(5, employeeID);

            // Execute the INSERT statement
            int added = pstmt.executeUpdate();
            if (added > 0) {
            System.out.println("New appointment added successfully.");
            }

            // Close the database connection
            db.DBDisconnect();
            } catch (Exception e) {
            System.out.println(e.getMessage());
            } finally {
            sc.close();
        }

    }
    public void viewAppointment() {
        try {
            DBConnect db = new DBConnect();
            String sql = "SELECT * FROM appointment_record WHERE Appointment_ID = ?";
            PreparedStatement pstmt = db.conn.prepareStatement(sql);
            pstmt.setInt(1, appointmentID);
            ResultSet rs = pstmt.executeQuery();
    
            System.out.printf("%-15s %-20s %-20s %-15s %-15s\n",
                "Appointment_ID", "Appointment_Date", "PetName", "ServiceID", "EmployeeID");
            System.out.println("---------------------------------------------------------------------------------------------");
    
            if (rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String date = rs.getString("Appointment_Date");
                String petName = rs.getString("PetName");
                int serviceID = rs.getInt("ServiceID");
                int employeeID = rs.getInt("EmployeeID");
    
                System.out.printf("%-15d %-20s %-20s %-15d %-15d\n",
                    id, date, petName, serviceID, employeeID);
            } else {
                System.out.println("No appointment found with ID: " + appointmentID);
            }
    
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Appointment a = new Appointment();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("------------------------------------------------------");
            System.out.println("Choose an option:");
            System.out.println("[1]. Add Appointment");
            System.out.println("[2]. View Appointment");
            System.out.println("[3]. Exit");
            System.out.print("Enter number to perform: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (choice == 1) {
                a.addAppointment();
            } else if (choice == 2) {
                a.viewAppointment();
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }
}
