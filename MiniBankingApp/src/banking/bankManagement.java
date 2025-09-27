package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.*;

public class bankManagement {
	private static final int NULL = 0;
	static Connection con = myConnection.getconnection();
	//create account
	public static boolean createAcoount(String name, int passCode) {
		
		if(name.isEmpty() || passCode== NULL) {
			System.out.println("All field are Required!");
			return false;
		}
		try {
			String sql ="insert into customer(cname,balance, pass_code) values(?,1000,?)";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, name);
			pst.setInt(2, passCode);
			
			int rows = pst.executeUpdate();
			if(rows ==1) {
				System.out.println("Account created successfully! you can now login.");
				return true;
			}
		}catch(SQLIntegrityConstraintViolationException e) {
			System.out.println("Username already exits! try another");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	//login account
	
	public static boolean loginAccount(String name,int passCode) {
		
		if(name.isEmpty() || passCode == NULL) {
			System.out.println("All fields are required!");
			return false;
		}
		try {
			
			String sql = "select  ac_no,cname, balance, pass_code from customer where cname=? and pass_code=?";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, name);
			pst.setInt(2, passCode);
			
			ResultSet rs = pst.executeQuery();
			
			BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
			if(rs.next()) {
				int senderAc = rs.getInt("ac_no");
				int choice;
				
				while(true) {
					System.out.println("\n Hello, "+rs.getString("cname")+"!What would you like to do?");
					System.out.println("(1) Transfer money");
					System.out.println("(2) View Balance");
					System.out.println("(3) Logout");
					System.out.println("Enter your choice");
					choice = Integer.parseInt(sc.readLine());
					
					if(choice==1) {
						System.out.println("Enter receiver A/C NO: ");
						int receiverAc = Integer.parseInt(sc.readLine());
						
						System.out.println("Enter amount: ");
						int amt = Integer.parseInt(sc.readLine());
						
						if(transferMoney(senderAc,receiverAc,amt)) {
							System.out.println("Transaction Successfully");
						}else {
							System.out.println("Transaction failed! please try again");
						}
					}else if(choice == 2) {
						getBalnace(senderAc);
					}else if(choice ==3) {
						System.out.println("Logout successfully. returning to main menu");
						break;
					}else {
						System.out.println("Invalid input");
					}
				}
				return true;
			}else {
				System.out.println("Invalid username or password");
				return false;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
	}

	// get balance
	private static void getBalnace(int acNo) {
		try {
			
			String sql = "select*from customer where ac_no=?";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, acNo);
			ResultSet rs = pst.executeQuery();
			
			System.out.println("\n------------------------------------");
			System.out.printf("%12s %15s %10s\n", "Account No", "Customer Name", "Balance");
			
			while(rs.next()) {
				 System.out.printf("%12d %15s %10d.00\n",
	                        rs.getInt("ac_no"),
	                        rs.getString("cname"),
	                        rs.getInt("balance"));
			}
			System.out.println("\n------------------------------------");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//tranfer money
	
	private static boolean transferMoney(int sender_ac, int receiver_ac, int amount) {
		if(receiver_ac == NULL || amount==NULL) {
			System.out.println("All feild are required");
			return false;
		}
		try {
			con.setAutoCommit(false);
			
			String checkBalance = "select balance from customer where ac_no=?";
			PreparedStatement pst = con.prepareStatement(checkBalance);
			pst.setInt(1, sender_ac);
			
			ResultSet rs = pst.executeQuery();
			if(rs.next() && rs.getInt("balance")< amount) {
				System.out.println("insufficient balance!");
				return false;
			}
			
			String debit = "update customer set balance = balance -? where ac_no=?";
			PreparedStatement pstDebit = con.prepareStatement(debit);
			pstDebit.setInt(1, amount);
			pstDebit.setInt(2, sender_ac);
			pstDebit.executeUpdate();
			
			String credit = "update customer set balance= balance + ? where ac_no=?";
			PreparedStatement pstCredit = con.prepareStatement(credit);
			pstCredit.setInt(1, amount);
			pstCredit.setInt(2, receiver_ac);
			pstCredit.executeUpdate();
			
			con.commit();
			return true;
			
			
			
		}catch(Exception e) {
			try {
				con.rollback();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
		return false;
	}	
}
