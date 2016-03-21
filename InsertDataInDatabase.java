import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertDataInDatabase{
	
	String userName;
	String password;
	String host;
	String sql;
	
	public InsertDataInDatabase() {
		userName = "root";
		password = "";
		host = "jdbc:mysql://localhost:3306/geolocation";
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(host, userName, password);
			sql = "CREATE TABLE cities " +
	                   "(id INTEGER not NULL, " +
	                   " country VARCHAR(255), " + 
	                   "region VARCHAR(255)," +
	                   " city VARCHAR(255), " + 
	                   " postal_code FLOAT, " + 
	                   " latitude FLOAT, " + 
	                   " longitude FLOAT, " + 
	                   " metroCode VARCHAR(255), " + 
	                   " areaCode VARCHAR(255), " + 
	                   " PRIMARY KEY ( id ))"; 
			
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.executeUpdate(sql);
			
		} catch (SQLException e) {
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		}
	}//default constructor ends here
	
	//method to read data from csv file
	public void readDataFromCSV() {
		try {
			BufferedReader bReader = new BufferedReader(new FileReader("GeoLiteCity-Location.csv"));
			String line = bReader.readLine();
			while((line = bReader.readLine()) !=null){
	             String[] b = line.split(",");
	             System.out.println(b[0]);
	        }
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}