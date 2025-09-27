package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class myConnection {

	static Connection con;
	
	public static Connection getconnection() {
	try {
	Class.forName("com.mysql.cj.jdbc.Driver");
	con = DriverManager.getConnection("jdbc:mysql://localhost/bank", "root", "vishal");
	
	}catch(ClassNotFoundException | SQLException e)
	{
//		e.printStackTrace();
		System.out.println("Connection failed!!!!!!!!!!"+e.getMessage());
	}
	return con;
	
	
}
}
