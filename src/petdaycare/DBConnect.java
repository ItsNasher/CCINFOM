package petdaycare;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

	public Connection conn;
	String url = "jdbc:mysql://localhost:3306/db_app_services";
    String username = "root";
    String password = "K!@n2004";
	
	public DBConnect() {
		try {
        conn = DriverManager.getConnection(url, username, password);
        System.out.println("Connection Succesful.");
		} catch (Exception e) {
	        System.out.println("An error happened connecting to the DB");
			System.out.println(e.getMessage());
		}
	}
	
	public void DBDisconnect() {
		try {
			conn.close();
			System.out.println("Closing...");
		} catch (Exception e) {
	        System.out.println("An error happened closing the connection to the DB");
			System.out.println(e.getMessage());			
		}
	}
	
	public static void main(String[] args) {
		DBConnect db = new DBConnect();

	}

}