package banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class bank {
	public static void main(String[] args) {
		
		BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
		String name;
		int pass_code;
		int choice;
		
		while(true) {
			System.out.println("=======================");
			System.out.println("Welcome to bank");
			System.out.println("========================");
			System.out.println("1) Create account");
			System.out.println("2) Login acccount");
			System.out.println("3) Exit");
			
			try {
				
				System.out.println("\nEnter your choice: ");
				choice = Integer.parseInt(sc.readLine());
				
				if(choice==1) {
					System.out.println("Enter unique name");
					name = sc.readLine();
					
					System.out.println("Enter password: ");
					pass_code = Integer.parseInt(sc.readLine());
					
					if(bankManagement.createAcoount(name, pass_code)) {
						System.out.println("you can now login from the main menu");
					}
				}
				
				if(choice ==2) {
					System.out.println("Enter username:");
					name=sc.readLine();
					
					System.out.println("Enter password:");
					pass_code = Integer.parseInt(sc.readLine());
				if(!bankManagement.loginAccount(name, pass_code)) {
					System.out.println("Login failed try agin");
				}		
				}
				
				
				
			}catch(Exception e) {
				System.out.println("please enter valid input");
			}
		}
		
		
	}
}
