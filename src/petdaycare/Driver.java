package petdaycare;
import java.util.Scanner;

public class Driver {
    public Driver(){}

    public static void main(String[] args) {
        int selection = 0;
        Scanner sc = new Scanner(System.in);

        while (selection != 4) {
			System.out.println("=====================================================");
			System.out.println("Welcome to our Pet Daycare!");
			System.out.println ("[1] Manage Owner Records");
            		System.out.println ("[2] Manage Pet Records");
			System.out.println ("[3] Enroll Student in a Course");
			System.out.println ("[4] Manage Product Records");
			System.out.println ("[5] Quit Application");
			System.out.println("=====================================================");
			System.out.print ("Enter function to perform: ");
			selection = sc.nextInt();
            sc.nextLine();
	
			if (selection==1) {
				Owner o = new Owner();
				while (o.function() != 5) {};
			} /*else if (selection==2) {
				enroll e = new enroll();
				while (e.function() != 1) {}; 
			} else if (selection==3) {
				report_01 r = new report_01();
				while (r.function() != 1) {};
				*/
			} else if (selection==4) {
				Product p = new Product();
				while (p.function() !=5) {};
			} 
		}
    }
}
