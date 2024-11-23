package petdaycare;

import java.time.LocalDate;
import java.util.Scanner;
import java.sql.*;

public class Services_Transaction {
    public int Transaction_ID;
    public String Transaction_Date;
    public int Quantity;
    public double Total_Amount;
    
    /*public void makeTransaction(){
        Scanner sc = new Scanner(System.in);
        Owner o = new Owner();
        Services s = new Services();

        try {
            DBConnect db = new DBConnect();

            System.out.println("Available Owners:");
            o.viewOwner();
    
            System.out.print("Enter Owner ID for the transaction: ");
            int ownerId = sc.nextInt();
            String findOwner = "SELECT Owner_ID FROM Owner WHERE Owner_ID = ?";
            try (PreparedStatement validateOwnerStmt = db.conn.prepareStatement(findOwner)) {
                validateOwnerStmt.setInt(1, ownerId);
                ResultSet rs = validateOwnerStmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("Invalid Owner ID. Transaction aborted.");
                    return;
                }
            }
    
            System.out.println("Available Services:");
            s.viewServices();
    
            System.out.print("Enter Service ID: ");
            int serviceId = sc.nextInt();
            double servicePrice = 0.0;
            String validateServiceQuery = "SELECT Price FROM Services WHERE Service_ID = ?";
            try (PreparedStatement validateServiceStmt = db.conn.prepareStatement(validateServiceQuery)) {
                validateServiceStmt.setInt(1, serviceId);
                ResultSet rs = validateServiceStmt.executeQuery();
                if (rs.next()) {
                    servicePrice = rs.getDouble("Price");
                } else {
                    System.out.println("Invalid Service ID. Transaction aborted.");
                    return;
                }
            }
    
            System.out.print("Enter the quantity for the service: ");
            Quantity = sc.nextInt();
            
            Total_Amount = servicePrice * Quantity;

            Transaction_Date = LocalDate.now().toString();
            String insertTransactionQuery = 
            "INSERT INTO Services_Transaction (Service_ID, Owner_ID, Transaction_Date, Quantity, Total_Amount) " +
            "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = db.conn.prepareStatement(insertTransactionQuery, Statement.RETURN_GENERATED_KEYS);

            insertStmt.setInt(1, serviceId);
            insertStmt.setInt(2, ownerId);
            insertStmt.setString(3, Transaction_Date);
            insertStmt.setInt(4, Quantity);
            insertStmt.setDouble(5, Total_Amount);

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int transactionId = generatedKeys.getInt(1);
                    System.out.println("Transaction successfully recorded with Transaction ID: " + transactionId);
                }
            } else {
                System.out.println("Failed to record transaction.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } */

    public void viewTransactions(){
        try {
            DBConnect db = new DBConnect();
            String fetchTransactionsQuery = 
                "SELECT Transaction_ID, Service_ID, Owner_ID, Transaction_Date, Quantity, Total_Amount FROM Services_Transaction";
    
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery(fetchTransactionsQuery);
    
            System.out.println("---------------------------------------------------------------" +
                        "-------------------------------");
            System.out.printf("| %-15s | %-10s | %-10s | %-15s | %-10s | %-15s |\n", 
                              "Transaction ID", "Service ID", "Owner ID", "Transaction Date", "Quantity", "Total Amount");
            System.out.println("---------------------------------------------------------------" +
                        "--------------------------------");
    
            while (rs.next()) {
                int transactionId = rs.getInt("Transaction_ID");
                int serviceId = rs.getInt("Service_ID");
                int ownerId = rs.getInt("Owner_ID");
                String transactionDate = rs.getString("Transaction_Date");
                int quantity = rs.getInt("Quantity");
                double totalAmount = rs.getDouble("Total_Amount");
    
                System.out.printf("| %-15d | %-10d | %-10d | %-15s | %-10d | %-15.2f |\n", 
                                  transactionId, serviceId, ownerId, transactionDate, quantity, totalAmount);
            }
            System.out.println("---------------------------------------------------------------" + 
                        "-------------------------------");
            db.DBDisconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(){
        Scanner sc = new Scanner(System.in);

        try {
            DBConnect db = new DBConnect();

            System.out.print("Enter the Transaction ID to delete: ");
            int transactionId = sc.nextInt();
            sc.nextLine();

            String validateQuery = "SELECT Transaction_ID FROM Services_Transaction WHERE Transaction_ID = ?";
            try (PreparedStatement validateStmt = db.conn.prepareStatement(validateQuery)) {
                validateStmt.setInt(1, transactionId);
                ResultSet rs = validateStmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("Transaction ID not found. Deletion aborted.");
                    return;
                }
            }
            System.out.print("Are you sure you want to delete the transaction with ID " + transactionId + "? (yes/no): ");
            String confirmation = sc.nextLine();

            if (!confirmation.equals("yes")) {
                System.out.println("Deletion canceled by the user.");
                return;
            }

            // Perform the deletion
            String deleteQuery = "DELETE FROM Services_Transaction WHERE Transaction_ID = ?";
            try (PreparedStatement deleteStmt = db.conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, transactionId);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Transaction with ID " + transactionId + " has been successfully deleted.");
                } else {
                    System.out.println("Failed to delete the transaction.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateVeterinarian(){
        try {
            DBConnect db = new DBConnect();
    
            System.out.println("Veterinarian Visit Report:");
    
            // Query for daily results with owner first names
            String dailyQuery = "SELECT st.Transaction_Date, o.first_name AS Owner_Name, COUNT(*) AS Visits " +
                                "FROM Services_Transaction st " +
                                "JOIN Owner o ON st.Owner_ID = o.Owner_ID " +
                                "WHERE st.Service_ID IN (1002, 1003) " +
                                "GROUP BY st.Transaction_Date, o.first_name " +
                                "ORDER BY st.Transaction_Date, o.first_name";
    
            System.out.println("\nDaily Visits:");
            try (PreparedStatement dailyStmt = db.conn.prepareStatement(dailyQuery);
                 ResultSet dailyResult = dailyStmt.executeQuery()) {
                System.out.printf("%-15s | %-20s | %-10s%n", "Transaction Date", "Owner Name", "Visits");
                System.out.println("---------------------------------------------------------");
                while (dailyResult.next()) {
                    String date = dailyResult.getString("Transaction_Date");
                    String ownerName = dailyResult.getString("Owner_Name");
                    int visits = dailyResult.getInt("Visits");
                    System.out.printf("%-15s | %-20s | %-10d%n", date, ownerName, visits);
                }
            }
    
            // Query for monthly results with owner first names
            String monthlyQuery = "SELECT YEAR(st.Transaction_Date) AS Year, MONTH(st.Transaction_Date) AS Month, o.first_name AS Owner_Name, COUNT(*) AS Visits " +
                                  "FROM Services_Transaction st " +
                                  "JOIN Owner o ON st.Owner_ID = o.Owner_ID " +
                                  "WHERE st.Service_ID IN (1002, 1003) " +
                                  "GROUP BY YEAR(st.Transaction_Date), MONTH(st.Transaction_Date), o.first_name " +
                                  "ORDER BY Year, Month, o.first_name";
    
            System.out.println("\nMonthly Visits:");
            try (PreparedStatement monthlyStmt = db.conn.prepareStatement(monthlyQuery);
                 ResultSet monthlyResult = monthlyStmt.executeQuery()) {
                System.out.printf("%-10s | %-10s | %-20s | %-10s%n", "Year", "Month", "Owner Name", "Visits");
                System.out.println("--------------------------------------------------------------");
                while (monthlyResult.next()) {
                    int year = monthlyResult.getInt("Year");
                    int month = monthlyResult.getInt("Month");
                    String ownerName = monthlyResult.getString("Owner_Name");
                    int visits = monthlyResult.getInt("Visits");
                    System.out.printf("%-10d | %-10d | %-20s | %-10d%n", year, month, ownerName, visits);
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int stMenu(){
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("------------------------------------------------------");
            System.out.println("What would you like to do?");
            System.out.println("[1] - View Services Transactions");
            System.out.println("[2] - Delete a Services Transaction");
            System.out.println("[3] - Generate Veterinarian Visits Report");
            System.out.println("[4] - Exit");
            System.out.print("Enter number to perform: ");
            int selection = sc.nextInt();
            sc.nextLine();

            if (selection == 1) {
                viewTransactions();
            } 
            else if (selection == 2) {
                viewTransactions();
                deleteTransaction();
            } 
            else if (selection == 3) {
                generateVeterinarian();
            } 
            else if(selection==4){
                System.out.println("Returning...");
                return selection;
            }  else {
                System.out.println("Selection not valid");
            }
    
            System.out.println("Press any key to return to Inventory Functions");
            sc.nextLine();
            return selection;
        }
    }
    public static void main(String[] args) {
        Services_Transaction s = new Services_Transaction();
        while (s.stMenu() != 4){
        };
    }

}
