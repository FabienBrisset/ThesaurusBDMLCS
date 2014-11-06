package thesaurus;

import java.awt.Color;

public class VueAccueil extends View {

	public VueAccueil() {
		this.setBackground(Color.ORANGE);
	}
	
	public void afficher() {
		Controller c = new Controller();
		Controller.fenetre.setContentPane(this);
		Controller.fenetre.revalidate();
	}
}
