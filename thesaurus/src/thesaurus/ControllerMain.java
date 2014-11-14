package thesaurus;

import java.awt.Event;
import java.awt.event.ActionEvent;

public class ControllerMain extends Controller {
	
	public void afficherFenetrePrincipale() {
		fenetre = new Fenetre();
		fenetre.setView(new View());
		VueAccueil vue = new VueAccueil();
		vue.afficher();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}
}
