package thesaurus;

import java.sql.*;

/**
 * Classe modelDB permettant la connexion à la BD et l'execution de requetes.
 * Cette classe est un Pattern Singleton ce qui permet de n'avoir qu'un seul instance de connection.
 * 
 * @author Axel
 *
 */

public class ModelDB {
	
	/**
	 * Liste des paramètres permettant l'accès à la BD.
	 * 
	 * db : variable de type Connection assurant la connexion à la BD.
	 * instance : variable de type ModelBD permettant d'utiliser les methodes de la classe 
	 */
	
	protected Connection db;
	protected static ModelDB instance;
	
	/**
	 * Constructeur privée de la classe 
	 */
	private ModelDB()
	{
		// On verifie d'abord si le driver jdbc existe 
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;
		}
		
		// On effectue ensuite la connexion
		try 
		{
			db = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:database","system","Toto123");
		} 
		catch (SQLException e) 
		{
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Permet d'instancier une seule fois la Classe
	 * 
	 * @return Une instance de la classe ModelDB
	 */
	public static ModelDB getDB()
	{
		if (instance==null)
		{
			instance = new ModelDB();
			System.out.println("First instance of connexion SQL");
		}
		else
		{
			//System.out.println("Connexion SQL already exists");
		}
		
		return instance;
	}
	
}
