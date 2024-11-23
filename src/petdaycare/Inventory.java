package petdaycare;

import java.util.*;
import java.sql.*;

public class Inventory {
    public int Inventory_ID;
    public int Product_ID;
    public int Stock_Quantity;
    public String Supply_Date;

    public Inventory() {
    }

    public int add_inventory() {
    
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            // Validate Product_ID
            PreparedStatement checkProductStmt = db.conn.prepareStatement(
                    "SELECT COUNT(*) AS count FROM Product WHERE Product_ID = ?");
            checkProductStmt.setInt(1, Product_ID);
            ResultSet rs = checkProductStmt.executeQuery();
            rs.next();
            if (rs.getInt("count") == 0) {
                System.out.println("Error: Product_ID " + Product_ID + " does not exist.");
                return 0;
            }

            // Validate Stock_Quantity
            if (Stock_Quantity <= 0) {
                System.out.println("Error: Stock_Quantity must be greater than 0.");
                return 0;
            }

            // Validate Supply_Date
            if (!Supply_Date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Error: Supply_Date must be in the format YYYY-MM-DD.");
                return 0;
            }

            // Prevent Duplicate Inventory Records
            PreparedStatement checkDuplicateStmt = db.conn.prepareStatement(
                    "SELECT COUNT(*) AS count FROM Inventory WHERE Product_ID = ? AND Supply_Date = ?");
            checkDuplicateStmt.setInt(1, Product_ID);
            checkDuplicateStmt.setString(2, Supply_Date);
            rs = checkDuplicateStmt.executeQuery();
            rs.next();
            if (rs.getInt("count") > 0) {
                System.out.println("Error: Inventory record for Product_ID " + Product_ID +
                        " and Supply_Date " + Supply_Date + " already exists.");
                return 0;
            }

            // Insert Invetory Record

                PreparedStatement pstmt = db.conn.prepareStatement(
                        "INSERT INTO Inventory (Product_ID, Stock_Quantity, Supply_Date) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);

                pstmt.setInt(1, Product_ID);
                pstmt.setInt(2, Stock_Quantity);
                pstmt.setString(3, Supply_Date);

                int added = pstmt.executeUpdate();
                if (added > 0) {
                    System.out.println("New inventory record added successfully.");
                }

                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Inventory_ID = generatedKeys.getInt(1);
                    System.out.println("Generated Inventory_ID: " + Inventory_ID);
                }
                db.DBDisconnect();
                return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void view_inventory() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Inventory_ID to view: ");
            int inventoryID = sc.nextInt();
            sc.nextLine();

            PreparedStatement pstmt = db.conn.prepareStatement(
                    "SELECT * FROM Inventory WHERE Inventory_ID = ?");
            pstmt.setInt(1, inventoryID);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Inventory_ID: " + rs.getInt("Inventory_ID"));
                System.out.println("Product_ID: " + rs.getInt("Product_ID"));
                System.out.println("Stock_Quantity: " + rs.getInt("Stock_Quantity"));
                System.out.println("Supply_Date: " + rs.getString("Supply_Date"));
            } else {
                System.out.println("No record found with Inventory_ID: " + inventoryID);
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void update_inventory() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            view_inventory();
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Inventory_ID to update: ");
            int inventoryID = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Stock Quantity: ");
            int newStockQuantity = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Supply Date (YYYY-MM-DD): ");
            String newSupplyDate = sc.nextLine();

            PreparedStatement pstmt = db.conn.prepareStatement(
                    "UPDATE Inventory SET Stock_Quantity = ?, Supply_Date = ? WHERE Inventory_ID = ?");
            pstmt.setInt(1, newStockQuantity);
            pstmt.setString(2, newSupplyDate);
            pstmt.setInt(3, inventoryID);

            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Inventory record updated successfully.");
            } else {
                System.out.println("No record found with Inventory_ID: " + inventoryID);
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete_inventory() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            view_inventory();
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Inventory_ID to delete: ");
            int inventoryID = sc.nextInt();
            sc.nextLine();

            PreparedStatement pstmt = db.conn.prepareStatement(
                    "DELETE FROM Inventory WHERE Inventory_ID = ?");
            pstmt.setInt(1, inventoryID);

            int deleted = pstmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("Inventory record deleted successfully.");
            } else {
                System.out.println("No record found with Inventory_ID: " + inventoryID);
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public int function() {
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------------------------------------------");
        System.out.println("What would you like to do with the Inventory Records?");
        System.out.println("[1] - Add Inventory Record");
        System.out.println("[2] - View Inventory Record");
        System.out.println("[3] - Update Inventory Record");
        System.out.println("[4] - Delete Inventory Record");
        System.out.println("[5] - Exit");
        System.out.print("Enter number to perform: ");
        int selection = sc.nextInt();
        sc.nextLine();

        if (selection == 1) {
            System.out.print("Enter Product ID: ");
            Product_ID = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Stock Quantity: ");
            Stock_Quantity = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Supply Date (YYYY-MM-DD): ");
            Supply_Date = sc.nextLine();

            add_inventory();
        } 
        else if(selection==2){
            view_inventory();
        }
        else if(selection==3){
            update_inventory();
        }
        else if(selection==4){
            delete_inventory();
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

    public static void main(String[] args) {
        Inventory i = new Inventory();
        while (i.function() != 5) {
        }
    }
}