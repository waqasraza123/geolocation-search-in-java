import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;


public class Main 
{
	static boolean tab_Create=false;;
	static boolean rec_Insert=false;

	public static boolean CreateTab()
	{
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		String DB_URL = "jdbc:mysql://localhost/cities";
		int TB_Check =0;
		try
		{
			Connection conn = null;
			Statement stmt = null;
			String DB_Name ="cities";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,"root","");
			PreparedStatement pst=null;

			pst  = conn.prepareStatement("CREATE TABLE IF NOT EXISTS record (id INTEGER not null, country VARCHAR(30),region VARCHAR(30),city VARCHAR(30), postalCode VARCHAR(30), latitude DECIMAL(5), longitude DECIMAL(5),metroCode INT,areaCode INT,PRIMARY KEY(id) );");

			//stmt = conn.createStatement();
			TB_Check = pst.executeUpdate();
			//			System.out.println("TB: "+pst.executeUpdate());
		}
		catch(Exception sq)
		{

			sq.printStackTrace(System.out);
			System.out.println("Exiting...");
			return false;
		}
		if ( TB_Check == 0){
			return false;
		}
		else{
			return true;
		}
	}

	public static boolean SetRecord()
	{
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		String DB_URL = "jdbc:mysql://localhost/cities";
		try
		{
			Connection conn = null;
			Statement stmt = null;
			PreparedStatement pst = null;
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,"root","");

			stmt = conn.createStatement();


			String csvFile = "GeoLiteCity-Location.csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
			String sql;
			br = new BufferedReader(new FileReader(csvFile));
			int count = 0;


			while ((line = br.readLine()) != null &&  count < 10000) 
			{
				count++;
				if(count   <3)
					continue;

				String [] cols = line.split(",");
				String id = cols[0];
				String country = cols[1].replace("\"", "");
				String region = cols[2].replace("\"", "");
				String city = cols[3].replace("\"", "");
				String postalCode = cols[4].replace("\"", "");
				String lat = cols[5];
				String lon = cols[6];


				sql = "INSERT into record(id, country, region, city, postalCode, latitude, longitude) VALUES (" + Integer.parseInt(id) + ",'" + country + "','" + region + "','" + city + "','" + postalCode + "'," + Float.parseFloat(lat) + "," + Float.parseFloat(lon) + ");";
				stmt.executeUpdate(sql);


			}


		}
		catch(Exception sq)
		{
			sq.printStackTrace(System.out);
			System.out.println("Exiting...");
			System.exit(1);
		}
		return true;  
	}


	public static void main(String[] args) 
	{
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		String DB_URL = "jdbc:mysql://localhost/cities";
		try
		{
			Connection conn = null;
			PreparedStatement stmt = null;
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,"root","");
			PreparedStatement pst = null;
			int myCheck = 0;

			pst = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS cities");
			myCheck = pst.executeUpdate();

			System.out.println("DB: "+myCheck);
			
			if(myCheck == 1){
				tab_Create = CreateTab();

				System.out.println("Table Created");

				rec_Insert = SetRecord();

				System.out.println("Records Done");
			}

				float L1;
				float L2;

				Scanner sc = new Scanner(System.in);
				System.out.println("Enter Latitude");
				L1 = sc.nextFloat();
				System.out.println("Enter Longitude");
				L2 = sc.nextFloat();

				//	stmt = conn.prepareStatement("SELECT city from record WHERE latitude = ? and  longitude = ?;");
				stmt = conn.prepareStatement("SELECT city, ( 3959 * acos( cos( radians(?) ) * cos( radians( ? ) ) * cos( radians( ? ) - radians(?) ) + sin( radians(?) ) * sin(radians(?)) ) ) AS distance FROM record HAVING distance < 25");
				stmt.setFloat(1, L1);
				stmt.setFloat(2, L1);
				stmt.setFloat(3, L2);
				stmt.setFloat(4, L2);
				stmt.setFloat(5, L1);
				stmt.setFloat(6, L1);

				ResultSet rs = stmt.executeQuery();


				while(rs.next())
				{

					String first = rs.getString("city");
					if ( first.length() == 0 ){
						first = "N/A";
					}


					System.out.println("City :  " + first);
				}

				rs.close();


				System.out.println("Enter city");
				Scanner sc2 = new Scanner(System.in);
				String city = sc2.nextLine(); 
				stmt  = conn.prepareStatement("SELECT latitude, longitude from record WHERE city =?;");;
				stmt.setString(1, city);
				ResultSet rs1 = stmt.executeQuery();



				System.out.println("Latitude and Longitude of "+ city);
				while(rs1.next())
				{

					float first = rs1.getFloat("latitude");
					float second = rs1.getFloat("longitude");
					System.out.println("Latitude :  " + first + " Longitude: "+ second) ;
				}

			}
			catch(Exception sq)
			{
				sq.printStackTrace(System.out);
			}

		}

	}