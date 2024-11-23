package petdaycare;

import java.sql.*;
import java.util.Scanner;

public class Sales_Transaction {
    public int Transaction_ID;
    public int Product_ID;
    public int Quantity_Sold;
    public String Sale_Date;
    public double Total_Price;

    public void add_sales_transaction() {
        Scanner sc = new Scanner(System.in);

        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            // Input details for the sales transaction
            System.out.print("Enter Product ID: ");
            Product_ID = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Quantity Sold: ");
            Quantity_Sold = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Sale Date (YYYY-MM-DD): ");
            Sale_Date = sc.nextLine();

            // Fetch the product price from the database
            String fetchPriceQuery = "SELECT Price FROM Product WHERE Product_ID = ?";
            double productPrice = 0;
            try (PreparedStatement fetchPriceStmt = db.conn.prepareStatement(fetchPriceQuery)) {
                fetchPriceStmt.setInt(1, Product_ID);
                ResultSet rs = fetchPriceStmt.executeQuery();
                if (rs.next()) {
                    productPrice = rs.getDouble("Price");
                } else {
                    System.out.println("Product not found. Please check the Product ID.");
                    return;
                }
            }

            // Calculate the total price
            Total_Price = productPrice * Quantity_Sold;

            // Insert the transaction into the database
            String insertQuery = "INSERT INTO Sales_Transaction (Product_ID, Quantity_Sold, Sale_Date, Total_Price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = db.conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, Product_ID);
                pstmt.setInt(2, Quantity_Sold);
                pstmt.setString(3, Sale_Date);
                pstmt.setDouble(4, Total_Price);

                int added = pstmt.executeUpdate();
                if (added > 0) {
                    System.out.println("New sales transaction added successfully.");
                }

                // Retrieve the generated Transaction_ID
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Transaction_ID = generatedKeys.getInt(1);
                    System.out.println("Generated Transaction_ID: " + Transaction_ID);
                }
            }
            db.DBDisconnect();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to generate sales reports
    public void generate_report() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            // Daily Sales Report
            System.out.println("\n--- Daily Sales Report ---");
            String dailyQuery = "SELECT Sale_Date, SUM(Quantity_Sold) AS Total_Quantity, SUM(Total_Price) AS Total_Revenue " +
                                "FROM Sales_Transaction GROUP BY Sale_Date ORDER BY Sale_Date";
            try (PreparedStatement pstmt = db.conn.prepareStatement(dailyQuery)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Date: " + rs.getDate("Sale_Date"));
                    System.out.println("Total Quantity Sold: " + rs.getInt("Total_Quantity"));
                    System.out.println("Total Revenue: " + rs.getDouble("Total_Revenue"));
                    System.out.println("----------------------------");
                }
            }

            // Weekly Sales Report
            System.out.println("\n--- Weekly Sales Report ---");
            String weeklyQuery = "SELECT YEAR(Sale_Date) AS Year, WEEK(Sale_Date) AS Week, " +
                                 "SUM(Quantity_Sold) AS Total_Quantity, SUM(Total_Price) AS Total_Revenue " +
                                 "FROM Sales_Transaction GROUP BY Year, Week ORDER BY Year, Week";
            try (PreparedStatement pstmt = db.conn.prepareStatement(weeklyQuery)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Year: " + rs.getInt("Year") + ", Week: " + rs.getInt("Week"));
                    System.out.println("Total Quantity Sold: " + rs.getInt("Total_Quantity"));
                    System.out.println("Total Revenue: " + rs.getDouble("Total_Revenue"));
                    System.out.println("----------------------------");
                }
            }
            db.DBDisconnect();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void delete_sales_transaction() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Transaction_ID to delete: ");
        int deleteId = sc.nextInt();

        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            PreparedStatement pstmt = db.conn.prepareStatement(
                "DELETE FROM Sales_Transaction WHERE Transaction_ID = ?"
            );
            pstmt.setInt(1, deleteId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Sales transaction deleted successfully.");
            } else {
                System.out.println("Transaction_ID not found.");
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to update inventory status after sales
    public void update_inventory_status() {
        try{
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            // Fetch sales transactions that haven't been updated in inventory
            String transactionQuery = "SELECT Transaction_ID, Product_ID, Quantity_Sold FROM Sales_Transaction WHERE isUpdated = FALSE";
            try (PreparedStatement pstmt = db.conn.prepareStatement(transactionQuery)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int transactionId = rs.getInt("Transaction_ID");
                    int productId = rs.getInt("Product_ID");
                    int quantitySold = rs.getInt("Quantity_Sold");
    
                    // Update inventory for each product
                    String updateQuery = "UPDATE Inventory SET Stock_Quantity = Stock_Quantity - ? WHERE Product_ID = ?";
                    try (PreparedStatement updateStmt = db.conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, quantitySold);
                        updateStmt.setInt(2, productId);
                        int updated = updateStmt.executeUpdate();
                        if (updated > 0) {
                            System.out.println("Updated inventory for Product_ID: " + productId);
    
                            // Mark the transaction as updated
                            String markUpdatedQuery = "UPDATE Sales_Transaction SET isUpdated = TRUE WHERE Transaction_ID = ?";
                            try (PreparedStatement markStmt = db.conn.prepareStatement(markUpdatedQuery)) {
                                markStmt.setInt(1, transactionId);
                                markStmt.executeUpdate();
                                System.out.println("Transaction_ID: " + transactionId + " marked as updated.");
                            }
                        } else {
                            System.out.println("Failed to update inventory for Product_ID: " + productId);
                        }
                    }
                }
            }
    
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Menu function to manage the program
    public int function() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("------------------------------------------------------");
            System.out.println("What would you like to do?");
            System.out.println("[1] - Record Sales Transaction");
            System.out.println("[2] - Delete Record Sales Transaction");
            System.out.println("[3] - Generate Sales Report");
            System.out.println("[4] - Update Inventory Status");
            System.out.println("[5] - Exit");
            System.out.print("Enter number to perform: ");
            int selection = sc.nextInt();
            sc.nextLine();

            if (selection == 1) {
                add_sales_transaction();
            } 
            else if (selection == 2) {
                delete_sales_transaction();
            } 
            else if (selection == 3) {
                generate_report();
            } 
            else if(selection==4){
                update_inventory_status();
            }
            else if (selection == 5) {
                System.out.println("Exiting Inventory Function selected");
                System.out.println("Function terminated");
                return selection;
            } else {
                System.out.println("Selection not valid");
            }
    
            System.out.println("Press any key to return to Inventory Functions");
            sc.nextLine();
            return selection;
        }
    }

    public static void main(String[] args) {
        Sales_Transaction st = new Sales_Transaction();
        while(st.function() !=5) {}
    }
}
