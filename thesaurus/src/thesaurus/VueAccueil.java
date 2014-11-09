package thesaurus;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class VueAccueil extends View 
{
	
	private JTextPane textAccueil;

	public VueAccueil() 
	{
		textAccueil = new JTextPane();
		textAccueil.setEditable(false);
		textAccueil.setContentType("text/html"); 
		textAccueil.setText("<center><br /><br /><h1>BIENVENUE SUR L'APPLICATION THESAURUS 'CUISINE'</h1><center>  <center> <br/><br/><br /><br />Réalisée par :  </center> <center>LAFON Lucas - MALMASSARI Pierre - DEPRET Axel</center> <center>CHAMBON Théo - BRISSET Fabien</center><br /><br/><br/><br/><br/><br/><br/><br/><br/><br/><center> M1 INFORMATIQUE 2014-2015</center> <center>UNIVERSITE MONTPELLIER II</center> ");
	}
	
	public void afficher() 
	{
		super.afficher();
		panAccueil.add(textAccueil);
		panAccueil.setLayout(new BoxLayout(panAccueil, BoxLayout.X_AXIS));
		panAccueil.revalidate();
	}
}
