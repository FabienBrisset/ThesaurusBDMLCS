package thesaurus;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;

public class VueAjoutMot {
	
	JTextField textFieldNouvelleEntree;
	
	JPanel panNouvelleEntree;
	JPanel panParent;
	JPanel panSynonyme;
	JPanel panLabelDescription;
	JPanel panDescription;
	JPanel panButton;
	JPanel panLabelSynonyme;

	JLabel labelNouvelleEntree;
	JLabel labelParentExiste;
	JLabel labelSynonyme;
	JLabel labelDescription;
	
	String[] donneesComboBox = {"Exemple", "Exemple"};
	JComboBox comboBoxParent;
	JScrollPane scrollPanDescription;
	JScrollPane scrollPanSynonyme;
	JTextArea textAreaDescription;
	JTextArea textAreaSynonyme;
	JButton buttonAjouterEntree;
	
	public VueAjoutMot() 
	{	
		panNouvelleEntree = new JPanel();
		panNouvelleEntree.setMaximumSize(new Dimension(32767, 200));
		labelNouvelleEntree = new JLabel("Intitul\u00E9 de la nouvelle entr\u00E9e :");
		textFieldNouvelleEntree = new JTextField();
		textFieldNouvelleEntree.setColumns(30);
		
		panParent = new JPanel();
		panParent.setMaximumSize(new Dimension(32767, 200));
		labelParentExiste = new JLabel("Parent (doit exister) :");
		comboBoxParent = new JComboBox(donneesComboBox);
		
		panSynonyme = new JPanel();
		panSynonyme.setLayout(new BoxLayout(panSynonyme, BoxLayout.Y_AXIS));	
		panLabelSynonyme = new JPanel();
		panLabelSynonyme.setMaximumSize(new Dimension(32767, 100));
		labelSynonyme = new JLabel("Synonyme(s) :");
		scrollPanSynonyme = new JScrollPane();	
		textAreaSynonyme = new JTextArea();
	
		panDescription = new JPanel();
		panDescription.setLayout(new BoxLayout(panDescription, BoxLayout.Y_AXIS));
		panLabelDescription = new JPanel();
		panLabelDescription.setMaximumSize(new Dimension(32767, 100));
		labelDescription = new JLabel("Description :");
		scrollPanDescription = new JScrollPane();
		textAreaDescription = new JTextArea();
		scrollPanDescription.setViewportView(textAreaDescription);
		
		panButton = new JPanel();
		panButton.setMaximumSize(new Dimension(32767, 200));
		panButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		buttonAjouterEntree = new JButton("Ajouter l'Entr\u00E9e");
		buttonAjouterEntree.setForeground(Color.BLACK);
	}

	public void afficher() 
	{
		Controller.fenetre.getVueCourante().panAjouter.removeAll();
		Controller.fenetre.getVueCourante().panAjouter.setLayout(new BoxLayout(Controller.fenetre.getVueCourante().panAjouter, BoxLayout.Y_AXIS));
		
		panNouvelleEntree.add(labelNouvelleEntree);
		panNouvelleEntree.add(textFieldNouvelleEntree);
		Controller.fenetre.getVueCourante().panAjouter.add(panNouvelleEntree);
		
		panParent.add(labelParentExiste);
		panParent.add(comboBoxParent);
		Controller.fenetre.getVueCourante().panAjouter.add(panParent);
		
		panLabelSynonyme.add(labelSynonyme);
		panSynonyme.add(panLabelSynonyme);
		Controller.fenetre.getVueCourante().panAjouter.add(panSynonyme);
		scrollPanSynonyme.setViewportView(textAreaSynonyme);
		panSynonyme.add(scrollPanSynonyme);
		
		panLabelDescription.add(labelDescription);
		panDescription.add(panLabelDescription);
		panDescription.add(scrollPanDescription);
		Controller.fenetre.getVueCourante().panAjouter.add(panDescription);
		
		panButton.add(buttonAjouterEntree);
		Controller.fenetre.getVueCourante().panAjouter.add(panButton);
	}
}
