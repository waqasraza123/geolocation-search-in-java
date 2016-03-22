import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SearchNearByCities {

	
	Scanner in = new Scanner(System.in);
	double num1, num2, num3, num4;
	Connection connection;
	ConnectDatabase connectDatabase;
	PreparedStatement stmt;
	public ResultSet rs;
	public float L1;
	public float L2;
	int choice;
	String cityName;
	Scanner sc;
	boolean bool = true;
	
	
	/*
	 * call connect method from ConnectDatabase class to get connection instance
	 * then call distance method which will return result set for nearby cities
	 * */
	public SearchNearByCities() throws ClassNotFoundException, SQLException {
		
		//get connection instance
		connectDatabase = new ConnectDatabase();
		connection = connectDatabase.connectDatabaseMethod();
		
		//make scanner object for inputs
		sc = new Scanner(System.in);
		
		System.out.println("Search by city name = 1, by lat and long = 2");
		choice = in.nextInt();
		
		
		if(choice == 1){
			//get lat, long of city
			cityName = sc.nextLine();
			System.out.println(cityName);
			searchByCityName(cityName);
			
			//call distance method
			if(bool){
				rs = distance(L1, L2);
			}
			else{
				System.out.println("No Records found for city name");
				return; //will exit
			}
		}
		
		
		else if(choice == 2){
			System.out.println("Enter Latitude");
			L1 = sc.nextFloat();
			System.out.println("Enter Longitude");
			L2 = sc.nextFloat();
			
			rs = distance(L1, L2);
		}
		
		
		else{
			System.out.println("Please input 1 or 2");
			return;
		}
	}

	/*
	 * @param cityName
	 * @throws SQLException=========================================================
	 * @return lat and long
	 * */
	private void searchByCityName(String cityName2) throws SQLException {
		
		stmt  = connection.prepareStatement("SELECT latitude, longitude from record WHERE city =?");
		System.out.println("city name here "+cityName2);
		stmt.setString(1, cityName2);
		rs = stmt.executeQuery();
		
		if (rs.isBeforeFirst() ) {
			
			while(rs.next())
			{
				L1 = rs.getFloat("latitude");
				L2 = rs.getFloat("longitude");
			}
		} 
		else{
			bool = false;
			System.out.println("No data");
			return;//will return control from here, no further lines will be exec
		}
	}


	//find nearest points ====================================================================
	public ResultSet distance(float L1, float L2) throws SQLException {
		
		System.out.println("L1 : " + L1 + " L2 : " + L2);
		
		String sql = "SELECT *, (6371 * acos( cos( radians(?) ) * cos( radians( ? ) ) "
				+ "* cos( radians( ? ) - radians(?) ) + sin( radians(?) ) "
				+ "* sin( radians(?) ) )) AS distanta FROM record WHERE latitude<>'' "
				+ "AND longitude<>'' having distanta<50 ORDER BY distanta desc";
		
		stmt = connection.prepareStatement(sql);
		stmt.setFloat(1, L1);
		stmt.setFloat(2, L1);
		stmt.setFloat(3, L2);
		stmt.setFloat(4, L2);
		stmt.setFloat(5, L1);
		stmt.setFloat(6, L1);
		
		rs = stmt.executeQuery();	
		return rs;
	}
	
	
	//print results from rs=====================================================
	public void printResults() throws SQLException {
				
		try {
			System.out.println(rs);
			while(rs.next())
			{
				String first = rs.getString("city");
				if ( first.length() == 0 ){
					first = "N/A";
				}


				System.out.println("City :  " + first);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
