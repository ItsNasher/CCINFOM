package petdaycare;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class PetBoarding {
    // Fields for PetBoarding
    public int Transaction_ID;
    public int Owner_ID;
    public String Start_Date;
    public String End_Date;

    // Lists for storing PetBoarding records
    public ArrayList<Integer> Transaction_IDlist = new ArrayList<>();
    public ArrayList<Integer> Owner_IDlist = new ArrayList<>();
    public ArrayList<String> Start_Datelist = new ArrayList<>();
    public ArrayList<String> End_Datelist = new ArrayList<>();

    public PetBoarding() {}

    public void bookBoardingService() {
        Scanner sc = new Scanner(System.in);
        try {
            DBConnect db = new DBConnect();
            System.out.print("Enter Owner ID: ");
            Owner_ID = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Start Date (YYYY-MM-DD): ");
            Start_Date = sc.nextLine();

            System.out.print("Enter End Date (YYYY-MM-DD): ");
            End_Date = sc.nextLine();

            // Validate and process dates
            LocalDate start = LocalDate.parse(Start_Date);
            LocalDate end = LocalDate.parse(End_Date);

            if (end.isBefore(start)) {
                System.out.println("Error: End date cannot be before start date.");
                return;
            }

            // calculates the total amount based on the number of days
            long days = start.until(end).getDays() + 1; // include both start and end dates
            double totalAmount = days * 100; // Each day costs 100

        
            PreparedStatement pstmt = db.conn.prepareStatement(
                "INSERT INTO Services_Transaction (Service_ID, Owner_ID, Transaction_Date, Quantity, Total_Amount) " +
                "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, 1004); // Boarding Service ID
            pstmt.setInt(2, Owner_ID);
            pstmt.setDate(3, Date.valueOf(start)); // Transaction Date
            pstmt.setInt(4, (int) days);
            pstmt.setDouble(5, totalAmount);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    Transaction_ID = rs.getInt(1);
                    System.out.println("Transaction Created Successfully! Transaction ID: " + Transaction_ID);
                }
                rs.close();
            }

            // Insert into PetBoarding
            PreparedStatement boardingStmt = db.conn.prepareStatement(
                "INSERT INTO PetBoarding (Transaction_ID, Owner_ID, Start_Date, End_Date) VALUES (?, ?, ?, ?)");

            boardingStmt.setInt(1, Transaction_ID);
            boardingStmt.setInt(2, Owner_ID);
            boardingStmt.setDate(3, Date.valueOf(start));
            boardingStmt.setDate(4, Date.valueOf(end));

            if (boardingStmt.executeUpdate() > 0) {
                System.out.println("Boarding record created successfully!");
                System.out.println("Total Cost: " + totalAmount);
            }

            pstmt.close();
            boardingStmt.close();
            db.DBDisconnect();

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void viewBoardingRecords() {
        try {
            DBConnect db = new DBConnect();
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PetBoarding");

            System.out.printf("%-15s %-15s %-15s %-15s\n", 
                "Transaction ID", "Owner ID", "Start Date", "End Date");
            System.out.println("----------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-15d %-15d %-15s %-15s\n", 
                    rs.getInt("Transaction_ID"),
                    rs.getInt("Owner_ID"),
                    rs.getDate("Start_Date"),
                    rs.getDate("End_Date"));
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println("An error occurred while fetching records: " + e.getMessage());
        }
    }

    public void modifyBoardingRecord() {
        Scanner sc = new Scanner(System.in);
        try {
            DBConnect db = new DBConnect();
            System.out.print("Enter the Transaction ID to update: ");
            int transactionId = sc.nextInt();
            sc.nextLine();

            // Fetch current record
            PreparedStatement selectStmt = db.conn.prepareStatement(
                "SELECT * FROM PetBoarding WHERE Transaction_ID = ?");
            selectStmt.setInt(1, transactionId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No record found with Transaction ID: " + transactionId);
                db.DBDisconnect();
                return;
            }

            String currentStartDate = rs.getString("Start_Date");
            String currentEndDate = rs.getString("End_Date");

            System.out.println("\nEnter new values or 1 to keep the current value:");

            System.out.print("Start Date [" + currentStartDate + "]: ");
            String startDate = sc.nextLine();
            if (startDate.equals("1")) startDate = currentStartDate;

            System.out.print("End Date [" + currentEndDate + "]: ");
            String endDate = sc.nextLine();
            if (endDate.equals("1")) endDate = currentEndDate;

            PreparedStatement updateStmt = db.conn.prepareStatement(
                "UPDATE PetBoarding SET Start_Date = ?, End_Date = ? WHERE Transaction_ID = ?");
            updateStmt.setString(1, startDate);
            updateStmt.setString(2, endDate);
            updateStmt.setInt(3, transactionId);

            if (updateStmt.executeUpdate() > 0) {
                System.out.println("Boarding record updated successfully!");
            } else {
                System.out.println("Failed to update boarding record.");
            }

            db.DBDisconnect();

        } catch (Exception e) {
                System.out.println("An error occurred while updating: " + e.getMessage());
            }
        }

        public void deleteBoardingRecord() {
            Scanner sc = new Scanner(System.in);
            try {
                DBConnect db = new DBConnect();
                System.out.print("Enter the Transaction ID to delete: ");
                int transactionId = sc.nextInt();
                sc.nextLine();

                // Check if the transaction ID exists
                PreparedStatement checkStmt = db.conn.prepareStatement(
                    "SELECT * FROM PetBoarding WHERE Transaction_ID = ?");
                checkStmt.setInt(1, transactionId);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("No record found with Transaction ID: " + transactionId);
                    db.DBDisconnect();
                    return;
                }

                System.out.print("Are you sure you want to delete Transaction ID " + transactionId + "? (yes/no): ");
                String confirmation = sc.nextLine();

                if (confirmation.equalsIgnoreCase("yes")) {
                    PreparedStatement pstmt = db.conn.prepareStatement(
                        "DELETE FROM PetBoarding WHERE Transaction_ID = ?");
                    pstmt.setInt(1, transactionId);

                    if (pstmt.executeUpdate() > 0) {
                        System.out.println("Boarding record deleted successfully!");
                    } else {
                        System.out.println("Failed to delete the boarding record.");
                    }

                    pstmt.close();
                } else {
                    System.out.println("Deletion canceled.");
                }
                db.DBDisconnect();

            } catch (Exception e) {
                System.out.println("An error occurred while deleting: " + e.getMessage());
            }
        }

        public void generateUtilizationReport(String startDate, String endDate) {
            try {
                DBConnect db = new DBConnect();
                PreparedStatement pstmt = db.conn.prepareStatement(
                    "SELECT Report_Date, COUNT(DISTINCT Transaction_ID) AS Number_of_Pets " +
                    "FROM ( " +
                    "    SELECT DATE(Start_Date + INTERVAL seq DAY) AS Report_Date, Transaction_ID " +
                    "    FROM PetBoarding " +
                    "    JOIN ( " +
                    "        SELECT 0 AS seq UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL " +
                    "        SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 " +
                    "    ) seq_table " +
                    "    ON Start_Date + INTERVAL seq DAY <= End_Date " +
                    "    WHERE Start_Date + INTERVAL seq DAY BETWEEN ? AND ? " +
                    ") ReportDates " +
                    "GROUP BY Report_Date " +
                    "ORDER BY Report_Date;"
                );

                pstmt.setString(1, startDate);  // Set the start date
                pstmt.setString(2, endDate);    // Set the end date
        
                ResultSet rs = pstmt.executeQuery();
        
                System.out.printf("%-15s %-20s\n", "Report Date", "Number of Pets");
                System.out.println("-----------------------------------");
        
                while (rs.next()) {
                    String reportDate = rs.getString("Report_Date");
                    int numberOfPets = rs.getInt("Number_of_Pets");
        
                    System.out.printf("%-15s %-20d\n", reportDate, numberOfPets);
                }
        
                db.DBDisconnect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        

        public int boardingMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------------------------------------------");
        System.out.println("What would you like to do with the Boarding Records?");
        System.out.println("[1] - Book a new Boarding Service");
        System.out.println("[2] - View Boarding Records");
        System.out.println("[3] - Modify a Boarding Record");
        System.out.println("[4] - Delete a Boarding Record");
        System.out.println("[5] - Utilization report");
        System.out.println("[5] - Exit");
        System.out.print("Enter number to perfom: ");
        int selection = sc.nextInt();
        sc.nextLine();

        switch (selection) {
            case 1:
                bookBoardingService();
                break;
            case 2:
                viewBoardingRecords();
                break;
            case 3:
                viewBoardingRecords();
                modifyBoardingRecord();
                break;
            case 4:
                viewBoardingRecords();
                deleteBoardingRecord();
                break;
            case 5: {
                System.out.println("Generating Utilization Report.");
                System.out.print("Enter start date (YYYY-MM-DD): ");
                String startDate = sc.nextLine();
            
                System.out.print("Enter end date (YYYY-MM-DD): ");
                String endDate = sc.nextLine();
            
                generateUtilizationReport(startDate, endDate);
                break;
            }
            case 6:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid selection.");
                break;
        }

        System.out.println("Press any key to return to Boarding Functions.");
        sc.nextLine();

        return selection;
    }

    public static void main(String[] args) {
        PetBoarding pb = new PetBoarding();
        while (pb.boardingMenu() != 5) {
        }
    }
}
