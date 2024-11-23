package petdaycare;

import java.util.*;
import java.sql.*;

public class Product {
    public int Product_ID;
    public String Product_Name;
    public double Price;
    public String Description;

    public Product() {
    }

    public int add_product() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

                    // Check if a product with the same name already exists
                PreparedStatement checkStmt = db.conn.prepareStatement(
                    "SELECT COUNT(*) AS count FROM Product WHERE Product_Name = ?");
            checkStmt.setString(1, Product_Name);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt("count") > 0) {
                System.out.println("Error: A product with the name '" + Product_Name + "' already exists.");
                return 0;
            }

            // Validate inputs
            if (Product_Name == null || Product_Name.trim().isEmpty()) {
                System.out.println("Error: Product name cannot be empty.");
                return 0;
            }
            if (Price <= 0) {
                System.out.println("Error: Price must be greater than 0.");
                return 0;
            }

            // Insert A New Product

            PreparedStatement pstmt = db.conn.prepareStatement(
                    "INSERT INTO Product (Product_Name, Price, Description) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, Product_Name);
            pstmt.setDouble(2, Price);
            pstmt.setString(3, Description);

            int added = pstmt.executeUpdate();
            if (added > 0) {
                System.out.println("New product added successfully.");
            }

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Product_ID = generatedKeys.getInt(1);
                System.out.println("Generated Product_ID: " + Product_ID);
            }
            db.DBDisconnect();
            return 1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void view_product() {
        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");

            System.out.println("Product Records:");
            System.out.println("-------------------------------------------------");
            while (rs.next()) {
                System.out.println("Product_ID: " + rs.getInt("Product_ID"));
                System.out.println("Product_Name: " + rs.getString("Product_Name"));
                System.out.println("Price: " + rs.getDouble("Price"));
                System.out.println("Description: " + rs.getString("Description"));
                System.out.println("-------------------------------------------------");
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void update_product() {
        Scanner sc = new Scanner(System.in);

        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            view_product();
            System.out.print("Enter the Product_ID to update: ");
            int productIdToUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Product Name: ");
            String newName = sc.nextLine();

            System.out.print("Enter new Price: ");
            double newPrice = sc.nextDouble();
            sc.nextLine();

            System.out.print("Enter new Description: ");
            String newDescription = sc.nextLine();

            PreparedStatement pstmt = db.conn.prepareStatement(
                    "UPDATE Product SET Product_Name = ?, Price = ?, Description = ? WHERE Product_ID = ?");
            pstmt.setString(1, newName);
            pstmt.setDouble(2, newPrice);
            pstmt.setString(3, newDescription);
            pstmt.setInt(4, productIdToUpdate);

            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product_ID not found.");
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete_product() {
        Scanner sc = new Scanner(System.in);

        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful");

            view_product();
            System.out.print("Enter the Product_ID to delete: ");
            int productIdToDelete = sc.nextInt();
            sc.nextLine();

            PreparedStatement pstmt = db.conn.prepareStatement("DELETE FROM Product WHERE Product_ID = ?");
            pstmt.setInt(1, productIdToDelete);

            int deleted = pstmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product_ID not found.");
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int function() {
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------------------------------------------");
        System.out.println("What would you like to do with the Product Records?");
        System.out.println("[1] - Add a Product");
        System.out.println("[2] - View a Product");
        System.out.println("[3] - Update a Product");
        System.out.println("[4] - Delete a Product");
        System.out.println("[5] - Exit");
        System.out.print("Enter number to perform: ");
        int selection = sc.nextInt();
        sc.nextLine();

        if (selection == 1) {
            while (true){
            System.out.println("Enter Product Name: ");
            Product_Name = sc.nextLine();
            if (!Product_Name.trim().isEmpty()) {
                break;
            }
            System.out.println("Error: Product name cannot be empty.");
        }

            while (true){
            System.out.println("Enter Price: ");
            try {
                Price = sc.nextDouble();
                sc.nextLine();
                if (Price > 0) {
                    break;
                }
                System.out.println("Error: Price must be greater than 0.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input for price. Please enter a valid number.");
                sc.nextLine(); // Clear the invalid input
            }
        }

            System.out.println("Enter Description: ");
            Description = sc.nextLine();
            add_product();
        } else if (selection == 2) {
            view_product();
        } else if (selection == 3) {
            update_product();
        } else if (selection == 4) {
            delete_product();
        } else if (selection == 5) {
            System.out.println("Exiting Product Function selected");
            System.out.println("Function terminated");
            return selection;
        } else {
            System.out.println("Selection not valid");
        }

        System.out.println("Press any key to return to Product Functions");
        sc.nextLine();
        sc.nextLine();
        return selection;
    }

    public static void main(String[] args) {
        Product p = new Product();
        while (p.function() != 5) {
        }
    }
}