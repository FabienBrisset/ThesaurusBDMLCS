package thesaurus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
	
	protected static Fenetre fenetre;
	
	public Controller() {
		fenetre = null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public Fenetre getFenetre() {
		if (fenetre == null) {
			fenetre = new Fenetre();
		}
		
		return fenetre;
	}
}
