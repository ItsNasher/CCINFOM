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
        String url = "jdbc:mysql://localhost:3310/db_app_services";
        String username = "root";
        String password = "ethan";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful");

            PreparedStatement pstmt = conn.prepareStatement(
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
            return 1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
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
            System.out.println("Enter Product Name: ");
            Product_Name = sc.nextLine();

            System.out.println("Enter Price: ");
            Price = sc.nextDouble();
            sc.nextLine();

            System.out.println("Enter Description: ");
            Description = sc.nextLine();

            add_product();
        } else if (selection == 5) {
            System.out.println("Exiting Product Function selected");
            System.out.println("Function terminated");
            return selection;
        } else {
            System.out.println("Selection not valid");
        }

        System.out.println("Press any key to return to Product Functions");
        sc.nextLine();
        return selection;
    }

    public static void main(String[] args) {
        Product p = new Product();
        while (p.function() != 5) {
        }
    }
}
