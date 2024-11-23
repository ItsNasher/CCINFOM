package petdaycare;
import java.util.Scanner;

public class Driver {
    public Driver(){}

    public static void main(String[] args) {
        int selection = 0;
        Scanner sc = new Scanner(System.in);

        while (selection != 9) {
			System.out.println("=====================================================");
			System.out.println("Welcome to our Pet Daycare!");
			System.out.println ("[1] Manage Owner Records");
            System.out.println ("[2] Manage Pet Records");
			System.out.println ("[3] Manage Service Records");
			System.out.println ("[4] Manage Product Records");
			System.out.println ("[5] Manage Appointment Records");
			System.out.println ("[6] Manage Inventory Records");
			System.out.println ("[7] Manage Sale Transaction Records");
			System.out.println ("[8] Manage Services Transaction Records");
			System.out.println ("[9] Quit Application");
			System.out.println("=====================================================");
			System.out.print ("Enter function to perform: ");
			selection = sc.nextInt();
            sc.nextLine();
	
			if (selection==1) {
				Owner o = new Owner();
				while (o.ownerMenu() != 5) {};
			} /*else if (selection==2) {
				enroll e = new enroll();
				while (e.function() != 1) {}; */
			else if (selection==3) {
				Services s = new Services();
				while (s.serviceMenu() != 5) {}
			}
			else if (selection==4) {
				Product p = new Product();
				while (p.function() !=5) {};
			} 
			else if (selection==5){
				Appointment a = new Appointment();
				while (a.appointmentMenu() !=5) {};
			}
			else if (selection==6){
				Inventory i = new Inventory();
				while (i.function() !=5) {};
			}
			else if (selection==7){
				Sales_Transaction st = new Sales_Transaction();
				while (st.function() !=5) {};
			}
			else if (selection==8){
				Services_Transaction set = new Services_Transaction();
				while (set.stMenu() !=4) {};
			}
			else if (selection==9){
				System.out.println("Thank you for visiting our Pet Daycare!");
			}
		}
    }
}