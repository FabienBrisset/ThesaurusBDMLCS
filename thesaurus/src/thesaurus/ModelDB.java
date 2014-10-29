package thesaurus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelDB {
	
	protected static Connection db;
	protected static ModelDB instance;
	
	public ModelDB()
	{
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			//return null;

		}
		try {
			this.db = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:orcl", "system",
					"Fabien34090");
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			//return null;

		}
	}

	public static ModelDB getInstance() {
		
		if(instance==null)
		{
			instance=new ModelDB();
		}
		return instance;
	}

	public ResultSet query(String sql, Object[] parametres) {
		// A changer par la suite
		return null;
	}
}
