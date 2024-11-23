package petdaycare;

import java.sql.*;
import java.util.*;

public class Appointment {
    Scanner sc = new Scanner(System.in);
    public int appointmentID;
    public String appointmentDate;
    public int ownerID;
    public String ownerFirstName;
    public String ownerLastName;
    public String contactNumber;
    public String city;
    public String petBreed;
    public int petAge;
    public int petid;
    public String petHealth;
    public String petName;
    public int serviceID;
    public int employeeID;
    public double totalBill;

    public void addAppointment() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");
    
            // Input details for the appointment
            System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
            String appointmentDate = sc.nextLine();
    
            System.out.print("Enter Owner First Name: ");
            String ownerFirstName = sc.nextLine();
    
            System.out.print("Enter Owner Last Name: ");
            String ownerLastName = sc.nextLine();
    
            System.out.print("Enter Contact Number (11 digits): ");
            String contactNumber = sc.nextLine();
    
            System.out.print("Enter Home City: ");
            String city = sc.nextLine();
    
            System.out.print("Enter Pet Name: ");
            String petName = sc.nextLine();
    
            System.out.print("Enter Pet Breed: ");
            String petBreed = sc.nextLine();
    
            System.out.print("Enter Pet Age: ");
            int petAge = sc.nextInt();
            sc.nextLine(); // Consume newline
    
            System.out.print("Enter Pet Health (Sick/Healthy): ");
            String petHealth = sc.nextLine();
    
            System.out.print("Enter Service ID: ");
            System.out.println("Services: (1001) Grooming, (1002) Checkups, (1003) Vaccinations, (1004) Boarding");
            int serviceID = sc.nextInt();
            sc.nextLine(); // Consume newline
            
            // Set total bill according to service ID
            if(serviceID == 1001) {
                totalBill = 500.00;
            }
            else if (serviceID == 1002) {
                totalBill = 350.00;
            }
            else if (serviceID == 1003) {
                totalBill = 1250.00;
            }
            else if (serviceID == 1004) {
                totalBill = 100.00;
            }
    
            System.out.print("Enter Employee ID to assign for the job: ");
            System.out.println("Employee:");
            System.out.println("(101) John Doe - Groomer");
            System.out.println("(102) Jane Smith - Veterinarian");
            System.out.println("(104) Michael Brown - Caretaker");
            int employeeID = sc.nextInt();
            sc.nextLine(); // Consume newline

    
            // Insert owner details into the owner table
            String ownerSql = "INSERT INTO owner (First_Name, Last_Name, Contact_Info, City) VALUES (?, ?, ?, ?)";
            PreparedStatement ownerPstmt = db.conn.prepareStatement(ownerSql, Statement.RETURN_GENERATED_KEYS);
            ownerPstmt.setString(1, ownerFirstName);
            ownerPstmt.setString(2, ownerLastName);
            ownerPstmt.setString(3, contactNumber);
            ownerPstmt.setString(4, city);
            ownerPstmt.executeUpdate();
    
            // Retrieve the generated Owner_ID
            ResultSet ownerRs = ownerPstmt.getGeneratedKeys();
            int ownerID = 0;
            if (ownerRs.next()) {
                ownerID = ownerRs.getInt(1);
            }
    
            // Insert pet details into the pet table
            String petSql = "INSERT INTO pet (Pet_Name, Breed, Age, HealthInformation, Owner_ID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement petPstmt = db.conn.prepareStatement(petSql, Statement.RETURN_GENERATED_KEYS);
            petPstmt.setString(1, petName);
            petPstmt.setString(2, petBreed);
            petPstmt.setInt(3, petAge);
            petPstmt.setString(4, petHealth);
            petPstmt.setInt(5, ownerID);
            petPstmt.executeUpdate();
    
            // Retrieve the generated Pet_ID
            ResultSet petRs = petPstmt.getGeneratedKeys();
            int petID = 0;
            if (petRs.next()) {
                petID = petRs.getInt(1);
            }
    
            // Insert appointment details into the appointment_record table
            String sql = "INSERT INTO appointment_record (Appointment_Date, Owner_ID, Owner_First_Name, Owner_Last_Name, PetID, PetName, ServiceID, EmployeeID) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = db.conn.prepareStatement(sql);
            
            // Set the values for the prepared statement
            pstmt.setString(1, appointmentDate);
            pstmt.setInt(2, ownerID);
            pstmt.setString(3, ownerFirstName);
            pstmt.setString(4, ownerLastName);
            pstmt.setInt(5, petID);
            pstmt.setString(6, petName);
            pstmt.setInt(7, serviceID);
            pstmt.setInt(8, employeeID);
            
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
    public void viewAppointments() {
        try {
            DBConnect db = new DBConnect();
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM appointment_record");
    
            System.out.printf("%-15s %-20s %-15s %-15s %-15s %-15s %-15s %-15s\n",
                "Appointment_ID", "Appointment_Date", "Owner_ID", "Owner_First_Name", "Owner_Last_Name", "PetID", "PetName", "ServiceID", "EmployeeID");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
    
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentDate = rs.getString("Appointment_Date");
                int ownerID = rs.getInt("Owner_ID");
                String ownerFirstName = rs.getString("Owner_First_Name");
                String ownerLastName = rs.getString("Owner_Last_Name");
                int petID = rs.getInt("PetID");
                String petName = rs.getString("PetName");
                int serviceID = rs.getInt("ServiceID");
                int employeeID = rs.getInt("EmployeeID");
    
                System.out.printf("%-15d %-20s %-15d %-15s %-15s %-15d %-15s %-15d %-15d\n",
                    appointmentID, appointmentDate, ownerID, ownerFirstName, ownerLastName, petID, petName, serviceID, employeeID);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void alterHealthInformation() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");
            viewAppointments();
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Pet ID: ");
            int petID = sc.nextInt();
            sc.nextLine(); // Consume newline
    
            System.out.print("Enter New Health Information: ");
            String newHealthInfo = sc.nextLine();
    
            String sql = "UPDATE pet SET HealthInformation = ? WHERE PetID = ?";
            PreparedStatement pstmt = db.conn.prepareStatement(sql);
            pstmt.setString(1, newHealthInfo);
            pstmt.setInt(2, petID);
    
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Health information updated successfully.");
            }
    
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void generateBilling() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");
            viewAppointments();
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Appointment ID: ");
            int appointmentID = sc.nextInt();
            sc.nextLine(); // Consume newline
    
            String sql = "SELECT ServiceID FROM appointment_record WHERE Appointment_ID = ?";
            PreparedStatement pstmt = db.conn.prepareStatement(sql);
            pstmt.setInt(1, appointmentID);
            ResultSet rs = pstmt.executeQuery();
    
            int serviceID = 0;
            if (rs.next()) {
                serviceID = rs.getInt("ServiceID");
            }
    
            double totalBill = 0.0;
            if (serviceID == 1001) {
                totalBill = 500.00;
            } else if (serviceID == 1002) {
                totalBill = 350.00;
            } else if (serviceID == 1003) {
                totalBill = 1250.00;
            } else if (serviceID == 1004) {
                totalBill = 100.00;
            }
    
            System.out.println("Total Bill: " + totalBill);
    
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
            System.out.println("[3]. Alter Health Information");
            System.out.println("[4]. Generate Billing");
            System.out.println("[5]. Exit");
            System.out.print("Enter number to perform: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (choice == 1) {
                a.addAppointment();
            } else if (choice == 2) {
                a.viewAppointments();
            } else if (choice == 5) {
                break;
            } else if (choice == 3) {
                a.alterHealthInformation();
            } else if (choice == 4) {
                a.generateBilling();
            }else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }
}
