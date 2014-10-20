import java.sql.*;

public class main {

	public static void main(String[] args) {
		System.out.println("-------- Oracle JDBC Connection Testing ------");

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;

		}

		System.out.println("Oracle JDBC Driver Registered!");

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:nom_de_ma_base_de_donnees", "system",
					"mon_mot_de_passe");
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

		// Requête : SELECT table_name FROM all_tables;
		Statement req;
		String nom;
		String prenom;
		int age;
		String nationalite;
		ResultSet rs;
		try {
			/*
			 * Statement req; int nb; req = connection.createStatement(); nb =
			 * req.executeUpdate(
			 * "INSERT into Joueur values ('Delpino', 'Dorian', 22, 'France')");
			 * System.out.println("Nombre de lignes modifiées : " + nb);
			 * req.close();
			 */

			/*
			 * int nb; req = connection.createStatement(); nb =
			 * req.executeUpdate
			 * ("INSERT into Joueur values ('Malmassari', 'Pierre', 22, 'France')"
			 * ); System.out.println("Nombre de lignes modifiées : " + nb);
			 * req.close();
			 */

			req = connection.createStatement();
			rs = req.executeQuery("SELECT * FROM Joueur");

			while (rs.next()) {
				nom = rs.getString(1);
				prenom = rs.getString(2);
				age = rs.getInt(3);
				nationalite = rs.getString(4);
				System.out.println("Nom : " + nom + ", prénom : " + prenom
						+ ", age : " + age + ", nationalité : " + nationalite);
			}
			req.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}