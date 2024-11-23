package petdaycare;
import java.util.*;
import java.sql.*;

public class Services {
    public int Service_ID;
    public String Service_Name;
    public String Start_Date;
    public String End_Date;
    public double Price;

    public Services() {
    }

    public void addServices(){
        try { // jdbc:mysql://localhost:3310/db_app_services?useTimezone=true&serverTimezone=UTC&user=root
            DBConnect db = new DBConnect();
            PreparedStatement pstmt = db.conn.prepareStatement(
            "INSERT INTO Services (Service_Name, Start_Date, End_Date, Price) " +
            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, Service_Name);
            pstmt.setString(2, Start_Date);
            pstmt.setString(3, End_Date);
            pstmt.setDouble(4, Price);
   

            int added = pstmt.executeUpdate();
            if (added > 0){
                System.out.println("New Service added succesfully.");
            }

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Service_ID = generatedKeys.getInt(1);
                System.out.println("Generated Service_ID: " + Service_ID);
            }
            db.DBDisconnect();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void viewServices(){
        try { 
            DBConnect db = new DBConnect();
            Statement statement = db.conn.createStatement();
            
            ResultSet resultSet =  statement.executeQuery("SELECT * FROM Services");

            System.out.printf("%-10s %-15s %-15s %-15s %-15s\n",
            "ID", "Service Name", "Available From", "To", "Price");
            System.out.println("----------------------------------------------------------------------------");

            while (resultSet.next()) {
                int serviceid = resultSet.getInt("Service_ID");
                String servicename = resultSet.getString("Service_Name");
                String startdate = resultSet.getString("Start_Date");
                String enddate = resultSet.getString("End_Date");
                double priceE = resultSet.getDouble("Price");

                // Ensure consistent spacing for output
                System.out.printf("%-10d %-15s %-15s %-15s %-15s\n",
                        serviceid, servicename, startdate, enddate, priceE);
            } 
            db.DBDisconnect();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void modifyServices(){
        Scanner sc = new Scanner(System.in);
        PreparedStatement pstmt;

        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful!");

            System.out.print("Enter the Service ID to update: ");
            int serviceID = sc.nextInt();
            sc.nextLine(); 

            String currentServiceName = "", currentStartDate = "", currentEndDate = "";
            double currentPrice = 0.0;
            PreparedStatement selectStmt = db.conn.prepareStatement(
                    "SELECT Service_Name, Start_Date, End_Date, Price FROM Services  WHERE Service_ID = ?");
            selectStmt.setInt(1, serviceID);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                currentServiceName = rs.getString("Service_Name");
                currentStartDate = rs.getString("Start_Date");
                currentEndDate = rs.getString("End_Date");
                currentPrice = rs.getDouble("Price");
            } else {
                System.out.println("No record found with Service ID: " + serviceID);
                db.DBDisconnect();
                return; 
            }
            rs.close();
            selectStmt.close();

            System.out.println("\nEnter new values or 1 to keep the current value:");

            System.out.print("Service Name [" + currentServiceName + "]: ");
            String serviceName = sc.nextLine();
            if (serviceName.equals("1")) serviceName = currentServiceName;

            System.out.print("Start Date [" + currentStartDate + "]: ");
            String startdate = sc.nextLine();
            if (startdate.equals("1")) startdate = currentStartDate;

            System.out.print("End Date [" + currentEndDate + "]: ");
            String enddate = sc.nextLine();
            if (enddate.equals("1")) enddate = currentEndDate;

            double price;
            do {
                System.out.print("Price [" + currentPrice + "]: ");
                String priceInput = sc.nextLine();
                if (priceInput.equals("1")) {
                    price = currentPrice;
                    break;
                }
                price = Double.parseDouble(priceInput);
                if (price <= 50) {
                    System.out.println("Error: Price must be higher than 50. Please try again.");
                }
            } while (price <= 50);

            pstmt = db.conn.prepareStatement(
                    "UPDATE Services SET Service_Name = ?, Start_Date = ?, End_Date = ?, Price = ? WHERE Service_ID = ?");
            pstmt.setString(1, serviceName);
            pstmt.setString(2, startdate);
            pstmt.setString(3, enddate);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, serviceID);

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            db.DBDisconnect();

            if (rowsAffected > 0) {
                System.out.println("Owner record updated successfully!");
            } else {
                System.out.println("No record found with Owner_ID: " + serviceID);
            }

        } catch (Exception e) {
            System.out.println("An error occurred while updating the owner record.");
            System.out.println(e.getMessage());
        }
    }

    public void deleteServices(){
        Scanner sc = new Scanner(System.in);

        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful!");

            System.out.print("Enter the Service ID to delete: ");
            int serviceID = sc.nextInt();
            sc.nextLine(); 

            if (serviceID <= 0) {
                System.out.println("Error: Service_ID must be a positive integer.");
                return;
            }
            PreparedStatement selectStmt = db.conn.prepareStatement("SELECT * FROM Services WHERE Service_ID = ?");
            selectStmt.setInt(1, serviceID);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No record found with Service_ID: " + serviceID);
                rs.close();
                db.DBDisconnect();
                return;
            }

            rs.close();
            System.out.print("Are you sure you want to delete the service with ID " + serviceID + "? (yes/no): ");
            String confirmation = sc.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                PreparedStatement pstmt = db.conn.prepareStatement("DELETE FROM Services WHERE Service_ID = ?");
                pstmt.setInt(1, serviceID);
                int rowsAffected = pstmt.executeUpdate();
                pstmt.close();
                db.DBDisconnect();

                if (rowsAffected > 0) {
                    System.out.println("Service record with ID " + serviceID + " deleted successfully!");
                } else {
                    System.out.println("Failed to delete the service record.");
                }
            } else {
                System.out.println("Deletion canceled.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the service record.");
            System.out.println(e.getMessage());
        }
    }

    public int serviceMenu() {
		Scanner sc 	= new Scanner(System.in);		
		System.out.println ("------------------------------------------------------");
		System.out.println ("What would you like to do with the Service Records?");
		System.out.println ("[1] - Register a new Service Record");
		System.out.println ("[2] - View Service Records");
		System.out.println ("[3] - Modify a Service Record");
		System.out.println ("[4] - Delete a Service Record");
		System.out.println ("[5] - Return to main menu");
		System.out.print ("Enter number to perform: ");
		int selection = sc.nextInt();
        sc.nextLine();
		
		if (selection == 1) {
			System.out.println("Creating new record of a Service");
            System.out.print("Enter Service Name: ");
            Service_Name = sc.nextLine();

            System.out.print("Enter Start Date (YYYY-MM-dd): ");
            Start_Date = sc.nextLine();

            System.out.print("Enter End Date (YYYY-MM-dd): ");
            End_Date = sc.nextLine();

            do {
                System.out.print("Enter Price (must be higher than 50): ");
                Price = sc.nextDouble();
                sc.nextLine();
                if (Price <= 50) {
                    System.out.println("Price must be higher than 50.");
                }
            } while (Price <= 50);

			addServices();
		}  
        else if (selection == 2) {
			viewServices();
		}  
        else if (selection == 3) {
            viewServices();
            modifyServices();
        }
        else if (selection == 4) {
			viewServices();
            deleteServices();
		} else if (selection == 5) {
			System.out.println("Returning...");
            return selection;
		} else {
			System.out.println("Selection not valid");
		}
		
		System.out.println ("Press any key to return to Services Functions");
		sc.nextLine();
		
		return selection;
	}
    public static void main(String[] args) {
        Services s = new Services();
        while (s.serviceMenu() != 5){
        };
    }
}
