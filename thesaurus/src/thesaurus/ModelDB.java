package thesaurus;

import java.sql.Connection;
import java.sql.ResultSet;

public class ModelDB {
	
	protected Connection db;

	public Connection getDB() {
		return this.db;
	}

	public ResultSet query(String sql, Object[] parametres) {
		// A changer par la suite
		return null;
	}
}
