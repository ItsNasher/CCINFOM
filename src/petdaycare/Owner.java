package petdaycare;
import java.util.*;
import java.sql.*;

public class Owner {
    // fields of owner
    public int Owner_ID;
    public String Owner_FirstName;
    public String Owner_LastName;
    public String Contact_Info;
    public String City;

    // list of owner
    public ArrayList<Integer> Owner_IDlist = new ArrayList<>();
    public ArrayList<String> Owner_FirstNamelist = new ArrayList<>();
    public ArrayList<String> Owner_LastNamelist = new ArrayList<>();
    public ArrayList<String> Contact_Infolist = new ArrayList<>();
    public ArrayList<String> Citylist = new ArrayList<>();

    public Owner() {}

    public void register_owner(){
        
        try { // jdbc:mysql://localhost:3310/db_app_services?useTimezone=true&serverTimezone=UTC&user=root
            DBConnect db = new DBConnect();
            PreparedStatement pstmt = db.conn.prepareStatement(
            "INSERT INTO Owner (First_Name, Last_Name, Contact_Info, City) " +
            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, Owner_FirstName);
            pstmt.setString(2, Owner_LastName);
            pstmt.setString(3, Contact_Info);
            pstmt.setString(4, City);
   

            int added = pstmt.executeUpdate();
            if (added > 0){
                System.out.println("New owner added succesfully.");
            }

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Owner_ID = generatedKeys.getInt(1);
                System.out.println("Generated Owner_ID: " + Owner_ID);
            }
            db.DBDisconnect();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void viewOwner(){
    
        try { // jdbc:mysql://localhost:3310/db_app_services?useTimezone=true&serverTimezone=UTC&user=root
            DBConnect db = new DBConnect();
            Statement statement = db.conn.createStatement();
            
            ResultSet resultSet =  statement.executeQuery("SELECT * FROM Owner");

            System.out.printf("%-10s %-15s %-15s %-15s %-15s\n",
            "ID", "First Name", "Last Name", "Phone", "City");
            System.out.println("----------------------------------------------------------------------------");

            // Data Rows
            while (resultSet.next()) {
                int id = resultSet.getInt("Owner_ID");
                String firstName = resultSet.getString("First_Name");
                String lastName = resultSet.getString("Last_Name");
                String phone = resultSet.getString("Contact_Info");
                String city = resultSet.getString("City");

                // Ensure consistent spacing for output
                System.out.printf("%-10d %-15s %-15s %-15s %-15s\n",
                        id, firstName, lastName, phone, city);
            } 
            db.DBDisconnect();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void modifyOwner(){
        Scanner sc = new Scanner(System.in);
        PreparedStatement pstmt;

        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful!");

            System.out.print("Enter the Owner_ID to update: ");
            int ownerID = sc.nextInt();
            sc.nextLine(); 

            String currentFirstName = "", currentLastName = "", currentContactInfo = "", currentCity = "";
            PreparedStatement selectStmt = db.conn.prepareStatement(
                    "SELECT First_Name, Last_Name, Contact_Info, City FROM Owner WHERE Owner_ID = ?");
            selectStmt.setInt(1, ownerID);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                currentFirstName = rs.getString("First_Name");
                currentLastName = rs.getString("Last_Name");
                currentContactInfo = rs.getString("Contact_Info");
                currentCity = rs.getString("City");
            } else {
                System.out.println("No record found with Owner_ID: " + ownerID);
                db.DBDisconnect();
                return; 
            }
            rs.close();
            selectStmt.close();

            System.out.println("\nEnter new values or 1 to keep the current value:");

            System.out.print("First Name [" + currentFirstName + "]: ");
            String firstName = sc.nextLine();
            if (firstName.equals("1")) firstName = currentFirstName;

            System.out.print("Last Name [" + currentLastName + "]: ");
            String lastName = sc.nextLine();
            if (lastName.equals("1")) lastName = currentLastName;

            System.out.print("Contact Info [" + currentContactInfo + "]: ");
            String contactInfo = sc.nextLine();
            if (contactInfo.equals("1")) contactInfo = currentContactInfo;

            System.out.print("City [" + currentCity + "]: ");
            String city = sc.nextLine();
            if (city.equals("1")) city = currentCity;

            pstmt = db.conn.prepareStatement(
                    "UPDATE Owner SET First_Name = ?, Last_Name = ?, Contact_Info = ?, City = ? WHERE Owner_ID = ?");
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, contactInfo);
            pstmt.setString(4, city);
            pstmt.setInt(5, ownerID);

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            db.DBDisconnect();

            if (rowsAffected > 0) {
                System.out.println("Owner record updated successfully!");
            } else {
                System.out.println("No record found with Owner_ID: " + ownerID);
            }

        } catch (Exception e) {
            System.out.println("An error occurred while updating the owner record.");
            System.out.println(e.getMessage());
        }
    }

    public void removeOwner(){
        Scanner sc = new Scanner(System.in);

        try {
            DBConnect db = new DBConnect();
            System.out.println("Connection Successful!");

            System.out.print("Enter the Owner_ID to delete: ");
            int ownerID = sc.nextInt();
            sc.nextLine(); 

            System.out.print("Are you sure you want to delete the owner with ID " + ownerID + "? (yes/no): ");
            String confirmation = sc.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                PreparedStatement pstmt = db.conn.prepareStatement("DELETE FROM Owner WHERE Owner_ID = ?");

                pstmt.setInt(1, ownerID);

                int rowsAffected = pstmt.executeUpdate();

                pstmt.close();
                db.DBDisconnect();

                if (rowsAffected > 0) {
                    System.out.println("Owner record with ID " + ownerID + " deleted successfully!");
                } else {
                    System.out.println("No record found with Owner_ID: " + ownerID);
                }
            } else {
                System.out.println("Deletion canceled.");
            }

        } catch (Exception e) {
            System.out.println("An error occurred while deleting the owner record.");
            System.out.println(e.getMessage());
        }
    }

    public int ownerMenu() {
		Scanner sc 	= new Scanner(System.in);		
		System.out.println ("------------------------------------------------------");
		System.out.println ("What would you like to do with the Owner Records?");
		System.out.println ("[1] - Register a new Owner Record");
		System.out.println ("[2] - View Owner Records");
		System.out.println ("[3] - Modify an Owner Record");
		System.out.println ("[4] - Delete an Owner Record");
		System.out.println ("[5] - Exit");
		System.out.print ("Enter number to perform: ");
		int selection = sc.nextInt();
        sc.nextLine();
		
		if (selection == 1) {
			System.out.println("Creating new record of an Owner");
            System.out.print("Enter First Name: ");
            Owner_FirstName = sc.nextLine();

            System.out.print("Enter Last Name: ");
            Owner_LastName = sc.nextLine();

            System.out.print("Enter Contact Info: ");
            Contact_Info = sc.nextLine();

            System.out.print("Enter City: ");
            City = sc.nextLine();

			register_owner(); 
		}  
        else if (selection == 2) {
			viewOwner();		
		}  
        else if (selection == 3) {
            viewOwner();
            modifyOwner();
        }
        else if (selection == 4) {
			viewOwner();
            removeOwner();
		} else if (selection == 5) {
			System.out.println("Returning...");
            return selection;
		} else {
			System.out.println("Selection not valid");
		}
		
		System.out.println ("Press any key to return to Owner Functions");
		sc.nextLine();
		
		return selection;
	}
    public static void main(String[] args) {
        Owner o = new Owner();
        while (o.ownerMenu() != 5){
        };
    }
}
