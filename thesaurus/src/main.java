import java.sql.*;

import thesaurus.Controller;
import thesaurus.ControllerMain;
import thesaurus.ControllerMots;
import thesaurus.Mot;

public class main {

	public static void main(String[] args) {
		ControllerMain c = new ControllerMain();
		c.afficherFenetrePrincipale();
		// Requ�te : SELECT table_name FROM all_tables;
		/*Statement req;
		String nom;
		String prenom;
		int age;
		String nationalite;
		ResultSet rs;
		try {
			
			  Statement req; int nb; req = connection.createStatement(); nb =
			  req.executeUpdate(
			  "INSERT into Joueur values ('Delpino', 'Dorian', 22, 'France')");
			  System.out.println("Nombre de lignes modifi�es : " + nb);
			  req.close();
			 

			
			  int nb; req = connection.createStatement(); nb =
			  req.executeUpdate
			  ("INSERT into Joueur values ('Malmassari', 'Pierre', 22, 'France')"
			  ); System.out.println("Nombre de lignes modifi�es : " + nb);
			  req.close();
			 

			req = connection.createStatement();
			rs = req.executeQuery("SELECT * FROM Joueur");

			while (rs.next()) {
				nom = rs.getString(1);
				prenom = rs.getString(2);
				age = rs.getInt(3);
				nationalite = rs.getString(4);
				System.out.println("Nom : " + nom + ", pr�nom : " + prenom
						+ ", age : " + age + ", nationalit� : " + nationalite);
			}
			req.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}