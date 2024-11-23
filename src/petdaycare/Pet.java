package petdaycare;

import java.sql.*;
import java.util.*;

public class Pet {

    // fields
    public int Pet_ID;
    public String Pet_Name;
    public String Breed;
    public int Age;
    public String HealthInformation;
    public int Owner_ID; 
    
    public ArrayList<Integer> Pet_IDList = new ArrayList<>();
    public ArrayList<String> Pet_NameList = new ArrayList<>();
    public ArrayList<String> BreedList = new ArrayList<>();
    public ArrayList<Integer> AgeList = new ArrayList<>();
    public ArrayList<String> HealthInfoList = new ArrayList<>();
    public ArrayList<Integer> Owner_IDList = new ArrayList<>();

    public Pet() {}

    // method for registering a new pet
    public void registerPet() {
        try {
            DBConnect db = new DBConnect();
            PreparedStatement pstmt = db.conn.prepareStatement(
                "INSERT INTO Pet (Pet_Name, Breed, Age, HealthInformation, Owner_ID) " +
                "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, Pet_Name);
            pstmt.setString(2, Breed);
            pstmt.setInt(3, Age);
            pstmt.setString(4, HealthInformation);
            pstmt.setInt(5, Owner_ID);

            int added = pstmt.executeUpdate();
            if (added > 0) {
                System.out.println("New pet added successfully.");
            }

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Pet_ID = generatedKeys.getInt(1);
                System.out.println("Generated Pet_ID: " + Pet_ID);
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // method for viewing pet records
    public void viewPets() {
        try {
            DBConnect db = new DBConnect();
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Pet");

            System.out.printf("%-10s %-20s %-30s %-10s %-50s %-10s\n",
                "Pet_ID", "Pet_Name", "Breed", "Age", "Health Info", "Owner_ID");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int petID = rs.getInt("Pet_ID");
                String name = rs.getString("Pet_Name");
                String breed = rs.getString("Breed");
                int age = rs.getInt("Age");
                String healthInfo = rs.getString("HealthInformation");
                int ownerID = rs.getInt("Owner_ID");

                System.out.printf("%-10d %-20s %-30s %-10d %-50s %-10d\n",
                    petID, name, breed, age, healthInfo, ownerID);
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // modifying pet record
    public void modifyPet() {
        try {
            DBConnect db = new DBConnect();
            PreparedStatement pstmt = db.conn.prepareStatement(
                "UPDATE Pet SET Pet_Name=?, Breed=?, Age=?, HealthInformation=?, Owner_ID=? WHERE Pet_ID=?");

            pstmt.setString(1, Pet_Name);
            pstmt.setString(2, Breed);
            pstmt.setInt(3, Age);
            pstmt.setString(4, HealthInformation);
            pstmt.setInt(5, Owner_ID);
            pstmt.setInt(6, Pet_ID);

            pstmt.executeUpdate();
            System.out.println("Pet record updated successfully.");
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Remove Pet
    public void removePet() {
        try {
            DBConnect db = new DBConnect();
            PreparedStatement pstmt = db.conn.prepareStatement("DELETE FROM Pet WHERE Pet_ID=?");
            pstmt.setInt(1, Pet_ID);

            int deleted = pstmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("Pet record deleted successfully.");
            }
            db.DBDisconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // method to check if a specific owner exists
    public boolean checkOwnerExists(int ownerID) {
        try {
            DBConnect db = new DBConnect();
            PreparedStatement pstmt = db.conn.prepareStatement("SELECT Owner_ID FROM Owner WHERE Owner_ID=?");
            pstmt.setInt(1, ownerID);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            db.DBDisconnect();
            return exists;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // method to check if the pet_id exists
    public boolean checkPetExists(int petID) {
        try {
            DBConnect db = new DBConnect();
            PreparedStatement pstmt = db.conn.prepareStatement("SELECT Pet_ID FROM Pet WHERE Pet_ID=?");
            pstmt.setInt(1, petID);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            db.DBDisconnect();
            return exists;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Pet Menu
    public int petMenu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("------------------------------------------------------");
        System.out.println("What would you like to do with the Pet Records?");
        System.out.println("[1] - Register a new Pet Record");
        System.out.println("[2] - View Pet Records");
        System.out.println("[3] - Modify a Pet Record");
        System.out.println("[4] - Delete a Pet Record");
        System.out.println("[5] - Exit");
        System.out.print("Enter number to perform: ");
        int selection = sc.nextInt();
        sc.nextLine();

        switch (selection) {
            case 1: {
                System.out.println("Creating a new Pet Record.");
                System.out.print("Enter Pet Name: ");
                Pet_Name = sc.nextLine();

                System.out.print("Enter Breed: ");
                Breed = sc.nextLine();

                System.out.print("Enter Age: ");
                Age = sc.nextInt();
                sc.nextLine(); 

                System.out.print("Enter Health Information: ");
                HealthInformation = sc.nextLine();

                System.out.print("Enter Owner ID: ");
                Owner_ID = Integer.parseInt(sc.nextLine());
                if (checkOwnerExists(Owner_ID)) {
                    registerPet(); // Proceed with registration if owner exists
                } else {
                    System.out.println("Error: Owner with ID " + Owner_ID + " does not exist. Pet cannot be registered.");
                }
                break;
            }

            case 2: 
                viewPets();
                break;

            case 3: {
                viewPets(); // Display existing pets

                System.out.print("Enter Pet ID to modify: ");
                Pet_ID = sc.nextInt(); // Read Pet_ID
                sc.nextLine(); // Consume leftover newline character

                if (!checkPetExists(Pet_ID)) {
                    System.out.println("No such Pet ID in the database!");
                    break;
                }

                System.out.print("Enter new Pet Name (Leave blank if no change): ");
                String newPetName = sc.nextLine(); // Read Pet Name
                if (!newPetName.isBlank()) Pet_Name = newPetName;

                System.out.print("Enter new Breed (Leave blank if no change): ");
                String newBreed = sc.nextLine(); // Read Breed
                if (!newBreed.isBlank()) Breed = newBreed;

                System.out.print("Enter new Age (Leave blank if no change): ");
                String newAge = sc.nextLine(); // Read Age
                if (!newAge.isBlank()) Age = Integer.parseInt(newAge);

                System.out.print("Enter new Health Info (Leave blank if no change): ");
                String newHealthInfo = sc.nextLine(); // Read Health Info
                if (!newHealthInfo.isBlank()) HealthInformation = newHealthInfo;

                System.out.print("Enter new Owner ID (Leave blank if no change): ");
                int newOwnerID = Integer.parseInt(sc.nextLine()); 
                if (checkOwnerExists(newOwnerID)) {
                    modifyPet(); 
                } else {
                    System.out.println("Error: Owner with ID " + Owner_ID + " does not exist. Owner ID couldn't be modified.");
                }   
                break; 
            }

            case 4: {
                viewPets();
                System.out.print("Enter Pet ID to delete: ");
                Pet_ID = sc.nextInt();
                if(!checkPetExists(Pet_ID)) {
                    System.out.println("No such Pet ID in the database!");
                    break;
                }
                sc.nextLine();
                removePet();
                break;
            }
            case 5: {
                System.out.println("Exiting Pet Function selected");
                System.out.println("Function terminated");
                return selection;
            }
            default: System.out.println("Invalid selection. Please try again.");
        }

        System.out.println("Returning...");
        sc.nextLine();

        return selection;
    }

    public static void main(String[] args) {
        Pet pet = new Pet();
        while (pet.petMenu() != 5) {}
    }
}
