package petdaycare;
import java.util.*;
import java.sql.*;

public class Owner {
    // fields of owner
    public int Owner_ID;
    public String Owner_FirstName;
    public String Owner_LastName;
    public int Contact_Info;
    public String City;
    public String Street;
    public int Postal_Code;

    // list of owner
    public ArrayList<Integer> Owner_IDlist = new ArrayList<>();
    public ArrayList<String> Owner_FirstNamelist = new ArrayList<>();
    public ArrayList<String> Owner_LastNamelist = new ArrayList<>();
    public ArrayList<Integer> Contact_Infolist = new ArrayList<>();
    public ArrayList<String> Citylist = new ArrayList<>();
    public ArrayList<String> Streetlist = new ArrayList<>();
    public ArrayList<Integer> Postal_Codelist = new ArrayList<>();

    public Owner() {

    }
    public void register_owner(){
        String url = "jdbc:mysql://localhost:3310/db_app_services";
        String username = "root";
        String password = "";
        try { // jdbc:mysql://localhost:3310/db_app_services?useTimezone=true&serverTimezone=UTC&user=root
            Connection conn;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Succesful");
            
            Statement statement = conn.createStatement();
            ResultSet resultSet =statement.executeQuery("SELECT * FROM Owner");

            while (resultSet.next()){
                System.out.println(resultSet.getString("First_Name"));
                System.out.println(resultSet.getString("Last_Name"));
            }
            //save the new asset, only 6 ? because id auto inrements
            /*PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO Owner (Owner_FirstName, Owner_LastName, Contact_Info, City, Street, Postal_Code) " +
            "VALUES (?, ?, ?, ?, ?, ?)");

            pstmt.setString(1, Owner_FirstName);
            pstmt.setString(2, Owner_LastName);
            pstmt.setInt(3, Contact_Info);
            pstmt.setString(4, City);
            pstmt.setString(5, Street);
            pstmt.setInt(6, Postal_Code);

            int added = pstmt.executeUpdate();
            if (added > 0){
                System.out.println("New owner added succesfully.");
            }

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Owner_ID = generatedKeys.getInt(1);
                System.out.println("Generated Owner_ID: " + Owner_ID);
            }
            return 1; */
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        
    }

    public int function() {
		/* this function is to provide an I/O module to facilitate the different record functions
		 * of the class. The function can be called by the MAIN MODULE of the DB Application.
		 */
		Scanner sc 	= new Scanner(System.in);		
		System.out.println ("------------------------------------------------------");
		System.out.println ("What would you like to do with the Owner Records?");
		System.out.println ("[1] - Register a new Owner Record");
		System.out.println ("[2] - View an Owner Record");
		System.out.println ("[3] - Update an Owner Record");
		System.out.println ("[4] - Delete an Owner Record");
		System.out.println ("[5] - Exit");
		System.out.print ("Enter number to perform: ");
		int selection = sc.nextInt();
        sc.nextLine();
		
		if (selection == 1) {
			/* Mechanics for Creating a Student Record 
			 * 1. Ask user for the data about the student
			 * 2. Create the new record
			 */
            register_owner();
            /* 
			System.out.println("Creating new record of a Student");
            System.out.println("Enter First Name: ");
            Owner_FirstName = sc.nextLine();

            System.out.println("Enter Last Name: ");
            Owner_LastName = sc.nextLine();

            System.out.println("Enter Contact Info: ");
            Contact_Info = sc.nextInt();
            sc.nextLine(); // Consume newline

            System.out.println("Enter City: ");
            City = sc.nextLine();

            System.out.println("Enter Street: ");
            Street = sc.nextLine();

            System.out.println("Enter Postal Code: ");
            Postal_Code = sc.nextInt();
            sc.nextLine();

			register_owner(); */
		}  
       /* else if (selection == 2) {
			/* Mechanics for Retrieving Student Record 
			 * 1. Ask user for student ID
			 * 2. Retrieve the record with the student ID provided
			 
			System.out.println("Enter student ID to retrieve :");
			studentid = sc.nextInt();
			viewStudent();			
		}  else if (selection == 3) {
			// Mechanics for Updating a Record
			// 1. Ask for the identifier of the record
			// 2. Retrieve the old data of the record
			// 3. Ask for the new set of data of the record. 
			//    if user did not put any value then old data will be used.
			// 4. Call the update function
            

			System.out.println("Enter student ID to update :");
			studentid = sc.nextInt();
			sc.nextLine();
			viewStudent();
			
			if (norecord) {
				System.out.println("No record to update.");
			} else {
				String newlastname  = new String();
				String newfirstname = new String();
				System.out.println ("Enter updated Last Name: ");
				newlastname  = sc.nextLine();
				System.out.println ("Enter updated First Name: ");
				newfirstname = sc.nextLine();
	
				// Check if the field was left not to be updated
				// If so, then use the old value of the data
				 
				if (newlastname.isEmpty()  == false) lastname = newlastname;
				if (newfirstname.isEmpty() == false) firstname = newfirstname;
				updateStudent();
			} 
		} else if (selection == 4) {
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
			System.out.println("Exiting Student Function selected");
			System.out.println("Function terminated");
			return selection;
		} else {
			System.out.println("Selection not valid");
		}
		
		System.out.println ("Press any key to return to Student Functions");
		sc.nextLine();
		sc.nextLine();
		
		return selection;
	}
    public static void main(String[] args) {
        Owner o = new Owner();
        while (o.function() != 5){
        };
    }
}
