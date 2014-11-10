package thesaurus;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class VueMot extends View {

	private Mot mot;
	
	private Object[][] donneesTableauSynonymes = {
		      {"Exemple", "Exemple"},
		    };
	private Object[][] donneesTableauFils = {
		      {"Exemple", "Exemple"},
		    };
	private String[] nomColonnes = {"Entrée", "Description"};
	private DefaultTableModel modeleTableauSynonymes = new DefaultTableModel(donneesTableauSynonymes, nomColonnes);
	private DefaultTableModel modeleTableauFils = new DefaultTableModel(donneesTableauFils, nomColonnes);
	
	private JPanel panChampRecherche;
	private JLabel labelChampRecherche;
	private JTextField textFieldChampRecherche;
	private JButton buttonChampRecherche;
	
	private JLabel labelEntreeRecherchee;
	private JPanel panEntreeRecherchee;
	
	private JPanel panParent;
	private JLabel labelParent;
	
	private JPanel panSynonymes;
	private JPanel panLabelSynonymes; // Pas réussi à faire sans en gardant le centrage du label
	private JLabel labelSynonymes;
	private JScrollPane scrollPanSynonymes; // Conteneur scrollable du tableau contenant les synonymes
	private JTable tableauSynonymes;
	
	private JPanel panFils;
	private JPanel panLabelFils; // Pas réussi à faire sans en gardant le centrage du label
	private JLabel labelFils;
	private JScrollPane scrollPanFils;
	private JTable tableauFils;
	
	private JPanel panDescription;
	private JPanel panLabelDescriptions;
	private JLabel labelDescription;
	private JScrollPane scrollPanDescription;
	private JTextArea textAreaDescription;

	private JPanel panButtonsConsulter;
	private JButton buttonEnregistrer;
	private JButton buttonSupprimer;
	
	public VueMot(Mot m)
	{
		this.mot = m;
		
		panChampRecherche = new JPanel();
		panChampRecherche.setMaximumSize(new Dimension(32767, 200));
		labelChampRecherche = new JLabel("Mot à rechercher : ");
		textFieldChampRecherche = new JTextField();
		textFieldChampRecherche.setColumns(10);
		buttonChampRecherche = new JButton("Rechercher");
		
		panEntreeRecherchee = new JPanel();
		panEntreeRecherchee.setMaximumSize(new Dimension(32767, 200));
		labelEntreeRecherchee = new JLabel("Entrée recherchée : ");
		
		panParent = new JPanel();
		panParent.setMaximumSize(new Dimension(32767, 200));
		panParent.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		labelParent = new JLabel("Parent : ");
		
		panSynonymes = new JPanel();
		panSynonymes.setLayout(new BoxLayout(panSynonymes, BoxLayout.Y_AXIS));
		panLabelSynonymes = new JPanel();
		labelSynonymes = new JLabel("Synonyme(s) :");
		scrollPanSynonymes = new JScrollPane();
		tableauSynonymes = new JTable(modeleTableauSynonymes);
		tableauSynonymes.setShowVerticalLines(false);
		
		panFils = new JPanel();
		panFils.setLayout(new BoxLayout(panFils, BoxLayout.Y_AXIS));
		panLabelFils = new JPanel();
		labelFils = new JLabel("Fils :");
		scrollPanFils = new JScrollPane();
		tableauFils = new JTable(modeleTableauFils);
		tableauFils.setShowVerticalLines(false);
		
		panDescription = new JPanel();
		panDescription.setMinimumSize(new Dimension(10, 150));
		panDescription.setLayout(new BoxLayout(panDescription, BoxLayout.Y_AXIS));
		panLabelDescriptions = new JPanel();
		panLabelDescriptions.setMaximumSize(new Dimension(200, 200));
		panDescription.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		labelDescription = new JLabel(" Description :");
		textAreaDescription = new JTextArea();
		
		panButtonsConsulter = new JPanel();
		buttonEnregistrer = new JButton("Enregistrer les modifications");
		buttonSupprimer = new JButton("Supprimer l'entrée");
		
	}
	
	public void afficher() 
	{
		super.afficher();
		panConsulter.setLayout(new BoxLayout(panConsulter, BoxLayout.Y_AXIS));
		
		panChampRecherche.add(labelChampRecherche);
		panChampRecherche.add(textFieldChampRecherche);
		panChampRecherche.add(buttonChampRecherche);
		panConsulter.add(panChampRecherche);
		
		panEntreeRecherchee.add(labelEntreeRecherchee);
		panConsulter.add(panEntreeRecherchee);
		
		panParent.add(labelParent);
		panConsulter.add(panParent);
		
		panSynonymes.add(panLabelSynonymes);
		panLabelSynonymes.add(labelSynonymes);
		panSynonymes.add(scrollPanSynonymes);
		scrollPanSynonymes.setViewportView(tableauSynonymes);
		panConsulter.add(panSynonymes);
		
		panConsulter.add(panFils);
		panFils.add(panLabelFils);
		panLabelFils.add(labelFils);
		panFils.add(scrollPanFils);
		scrollPanFils.setViewportView(tableauFils);
		
		panDescription.add(panLabelDescriptions);
		panLabelDescriptions.add(labelDescription);
		panConsulter.add(panDescription);
		panDescription.add(scrollPanDescription);
		scrollPanDescription.setViewportView(textAreaDescription);
		
		panButtonsConsulter.add(buttonEnregistrer);
		panButtonsConsulter.add(buttonSupprimer);
		panConsulter.add(panButtonsConsulter);
		
		panConsulter.revalidate();
	}
}
