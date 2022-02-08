import java.util.Scanner;

public class LibraryManagementSystem {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);  
	
		while (true) { 
    	  
			System.out.println("Welcome to Library Management System.");
			System.out.println("");
			System.out.println("Enter a number to proceed further:");
			System.out.println("1 : Login as Admin");
			System.out.println("2 : Login as Student");
			System.out.println("3 : Exit");

			int option = input.nextInt();

			if(option == 1) {
				Administration.adminServices();
			} else if (option == 2) {
				Student.studentServices();
			} else if (option == 3) {
				System.out.println("Exiting.");
				System.exit(0);
			} else {
				System.out.println("Wrong input.");
			}
			input.close();
		}
	}
}