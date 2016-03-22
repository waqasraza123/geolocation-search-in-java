import java.sql.SQLException;

public class MainDB {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		/*InsertDataInDatabase inDatabase = new InsertDataInDatabase();
		inDatabase.readDataFromCSV();
		
		
		inDatabase.closeConnection();//call in the end
*/		
		SearchNearByCities searchNearByCities = new SearchNearByCities();
		searchNearByCities.printResults();
		
		
	}

}
