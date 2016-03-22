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
	Connection connection;
	PreparedStatement preStatement;
	String[] b;
	
	public InsertDataInDatabase() {
		userName = "root";
		password = "";
		host = "jdbc:mysql://localhost:3306/geolocation";
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(host, userName, password);
			
			//drop table if already exists
			String dropTable = "DROP TABLE IF EXISTS `record`";
			preStatement =connection.prepareStatement(dropTable);
			preStatement.executeUpdate();
			
			sql = "CREATE TABLE record " +
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
			
			preStatement = connection.prepareStatement(sql);
			preStatement.executeUpdate(sql);
			
		} catch (SQLException e) {
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		}
	}//default constructor ends here
	
	
	//method to read data from csv file====================================================
	public void readDataFromCSV() {
		try {
			BufferedReader bReader = new BufferedReader(new FileReader("GeoLiteCity-Location.csv"));
			String line;
			while((line = bReader.readLine()) != null){
	             b = line.split(",");
	             
	             try {
	            	 /*System.out.println(b[0]);
		             System.out.println(b[1]);
		             System.out.println(b[2]);
		             System.out.println(b[3]);
		             System.out.println(b[4]);
		             System.out.println(b[5]);
		             System.out.println(b[6]);
		             System.out.println(b[7]);
		             System.out.println(b[8]);*/
		             insertDataInDatabase(b);
				} catch (Exception e) {
					//this code will produce array out of bound exception since some values are empty
					//e.printStackTrace();
					System.out.println("one col val was empty");
				}
	        }
			bReader.close();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}//read from csv file ends here
	
	
	//insert data in database=================================================================
	public void insertDataInDatabase(String[] b){
		String querString = "Insert into record values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			preStatement = connection.prepareStatement(querString);			
			preStatement.setInt(1, Integer.parseInt(b[0]));//set id
			preStatement.setString(2, (b[1]));//set country
			preStatement.setString(3, (b[2]));//set region
			preStatement.setString(4, (b[3]));//set city
			preStatement.setString(5, (b[4]));//set postalCode
			preStatement.setFloat(6, Float.parseFloat(b[5]));//set latitude
			preStatement.setFloat(7, Float.parseFloat(b[6]));//set longitude
			try {
				preStatement.setInt(8, Integer.parseInt(b[7]));//metroCode
			} catch (Exception e) {
				System.out.println("metro code was null");
			}
			try {
				preStatement.setInt(9, Integer.parseInt(b[8]));//set areCode
			} catch (Exception e) {
				System.out.println("area code was null");
			}
			
			
			//execute the statement
			preStatement.executeUpdate();
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	}
	
	//closing the conenction===========================================================
	public void closeConnection() throws SQLException {
		connection.close();
	}
	
}