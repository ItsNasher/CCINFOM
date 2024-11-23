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
            System.out.println("Connection Succesful");
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
			pstmt = db.conn.prepareStatement("UPDATE Owner SET First_Name=?, Last_Name=?, Contact_Info=?, City=? WHERE Owner_ID=?");
			
			pstmt.setString(1, Owner_FirstName);
			pstmt.setString(2, Owner_LastName);
			pstmt.setString(3, Contact_Info);
            pstmt.setString(4, City);
            pstmt.setInt(5, Owner_ID);
			
			pstmt.executeUpdate();
			pstmt.close();
            db.DBDisconnect();
			System.out.println("Student record updated in the Database");			
		} catch (Exception e) {
	        System.out.println("Error occured while updating a Student Record");
	        System.out.println(e.getMessage());	
		}
    }

    public void removeOwner(){

    }

    public int ownerMenu() {
		Scanner sc 	= new Scanner(System.in);		
		System.out.println ("------------------------------------------------------");
		System.out.println ("What would you like to do with the Owner Records?");
		System.out.println ("[1] - Register a new Owner Record");
		System.out.println ("[2] - View an Owner Record");
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
		}  else if (selection == 3) {
            
            viewOwner();
			System.out.print("Enter owner ID to update :");
			Owner_ID = sc.nextInt();
			sc.nextLine();

            String url = "jdbc:mysql://localhost:3310/db_app_services";
            String username = "root";
            String password = "ethan";

            String newLastname  = null;
            String newFirstname = null;
            String newContactInfo = null;
            String newCity = null;
            try {
                Connection conn = DriverManager.getConnection(url, username, password);
                
                // Check if the ID exists
                PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM Owner WHERE Owner_ID = ?");
                checkStmt.setInt(1, Owner_ID);
                ResultSet resultSet = checkStmt.executeQuery();
                
                if (!resultSet.next()) {
                    System.out.println("No record found with Owner ID: " + Owner_ID);
                } else {
                    System.out.print("Enter updated Last Name (Enter if no changes): ");
                    newLastname = sc.nextLine();
                    System.out.print("Enter updated First Name (Enter if no changes): ");
                    newFirstname = sc.nextLine();
                    System.out.print("Enter updated Contact Info (Enter if no changes): ");
                    newContactInfo = sc.nextLine();
                    System.out.print("Enter updated City (Enter if no changes): ");
                    newCity = sc.nextLine();

                    if (newLastname != null && !newLastname.isEmpty()){
                        Owner_LastName = newLastname;
                    }

                    if (newFirstname != null && !newFirstname.isEmpty()){
                        Owner_FirstName = newFirstname;
                    }

                    if (newContactInfo != null && !newContactInfo.isEmpty()){
                        Contact_Info = newContactInfo;
                    } 
                    if (newCity != null && !newCity.isEmpty()){
                        City = newCity;
                    }

                    modifyOwner();
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }   
        }
         
            /*else if (selection == 4) {
			// Mechanics for Deleting a Record
			// 1. Ask for the identifier of the record
			// 2. Retrieve the old data of the record
			// 3. Call the delete function
			 			
			
			System.out.println("Enter student ID to delete :");
			studentid = sc.nextInt();
			sc.nextLine();
			viewStudent();
			
			if (norecord) {
				System.out.println("No record to delete.");
			} else {
				deleteStudent();
			}
		} */else if (selection == 5) {
			System.out.println("Exiting Owner Function selected");
			System.out.println("Function terminated");
			return selection;
		} else {
			System.out.println("Selection not valid");
		}
		
		System.out.println ("Press any key to return to Owner Functions");
		sc.nextLine();
		sc.nextLine();
		
		return selection;
	}
    public static void main(String[] args) {
        Owner o = new Owner();
        while (o.ownerMenu() != 5){
        };
    }
}
