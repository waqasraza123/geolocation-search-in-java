import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {

	public String userName;
	public String password;
	public String host;
	public Connection connection;
	
	public ConnectDatabase() {
		userName = "root";
		password = "";
		host = "jdbc:mysql://localhost:3306/geolocation";
	}
	
	public Connection connectDatabaseMethod() throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(host, userName, password);
		return connection;
	}
	
}
