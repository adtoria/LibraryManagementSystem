import java.sql.*;

public class ConnectDB {
	
	public static Connection connectDB() {
		
		try {
		    final String url = "jdbc:mysql://localhost:3306/libraryDatabase";
		    final String user = "root";
		    final String pw = "8pothippo";
		    
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connect = DriverManager.getConnection(url,user,pw);
			
			System.out.println("Connection Established.");
			return connect;
		}
		
		catch(Exception e) {
			System.out.println("Connection Failed.");
            System.out.println(e);
		}
        
		return null;
	}
}