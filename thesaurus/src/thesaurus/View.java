package thesaurus;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class View extends JPanel {
	protected static JTabbedPane listeOnglets;
	protected static JPanel panAccueil;
	protected static JPanel panAjouter;
	protected static JPanel panConsulter;
	protected static JPanel panArbo;
	
	public View()
	{
		if(listeOnglets == null)
		{
			listeOnglets = new JTabbedPane(JTabbedPane.TOP);
			listeOnglets.setAlignmentX(Component.LEFT_ALIGNMENT);
			listeOnglets.setBackground(Color.WHITE);
			panAccueil = new JPanel();
			panAjouter = new JPanel();
			panConsulter = new JPanel();
			panArbo = new JPanel();
			listeOnglets.addChangeListener(new ControllerMots());
		}
	}
	
	public static JTabbedPane getListeOnglets() 
	{
		return listeOnglets;
	}

	public void afficher() 
	{
		if(listeOnglets.getComponentCount()==0)
		{	
			Controller.fenetre.getContentPane().add(listeOnglets);
			listeOnglets.addTab("Accueil", null, panAccueil, null);
			listeOnglets.addTab("Consulter/Modifier", null, panConsulter, null);
			listeOnglets.addTab("Ajouter", null, panAjouter, null);
			listeOnglets.addTab("Arborescence", null, panArbo, null);
		}
	}
}
