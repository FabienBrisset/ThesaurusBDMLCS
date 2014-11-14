package thesaurus;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VueMot {

	private Mot mot;
	
	private Object[][] donneesTableauSynonymes;
	private Object[][] donneesTableauFils;
	private String[] nomColonnes = {"Entrée", "Description"};
	
	private JPanel panChampRecherche;
	private JLabel labelChampRecherche;
	private JTextField textFieldChampRecherche;
	private JButton buttonChampRecherche;
	
	private JLabel labelEntreeRecherchee;
	private JLabel labelMotEntreeRecherchee;
	private JPanel panEntreeRecherchee;
	
	private JPanel panParent;
	private JLabel labelParent;
	private JButton buttonMotParent;
	
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
	private JPanel panLabelDescription;
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
		labelMotEntreeRecherchee = new JLabel(mot.getLibelleMot().toUpperCase());
		
		panParent = new JPanel();
		panParent.setMaximumSize(new Dimension(32767, 200));
		panParent.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		labelParent = new JLabel("Parent : ");
		buttonMotParent = new JButton(mot.getPere().getLibelleMot().toUpperCase());
		
		panSynonymes = new JPanel();
		panSynonymes.setLayout(new BoxLayout(panSynonymes, BoxLayout.Y_AXIS));
		panLabelSynonymes = new JPanel();
		labelSynonymes = new JLabel("Synonyme(s) :");
		scrollPanSynonymes = new JScrollPane();
		ArrayList<Mot> listeSynonymes = mot.getSynonymes();
		if(listeSynonymes.size() == 0)
		{
			Object[][] donneesTableauSynonymes= {{"Aucun synonyme", "Aucun synonyme"},};
			tableauSynonymes = new JTable(donneesTableauSynonymes,nomColonnes);
		}
		else
		{
			donneesTableauFils = new Object[listeSynonymes.size()][2];
			for (int i = 0; i < listeSynonymes.size(); i++)
			{
				donneesTableauSynonymes[i][0] = listeSynonymes.get(i).libelleMot.toUpperCase();
				donneesTableauSynonymes[i][1] = listeSynonymes.get(i).definitionMot;
				tableauSynonymes = new JTable(donneesTableauSynonymes,nomColonnes);
			}
		}
		tableauSynonymes.setDefaultRenderer(Object.class, new RenduCellule());
		tableauSynonymes.setShowVerticalLines(false);
		
		panFils = new JPanel();
		panFils.setLayout(new BoxLayout(panFils, BoxLayout.Y_AXIS));
		panLabelFils = new JPanel();
		labelFils = new JLabel("Fils :");
		scrollPanFils = new JScrollPane();
		ArrayList<Mot> listeFils = mot.getFils();
		if(listeFils.size() == 0)
		{
			Object[][] donneesTableauFils= {{"Aucun fils", "Aucun fils"},};
			tableauFils = new JTable(donneesTableauFils, nomColonnes);
		}
		else
		{
			donneesTableauFils = new Object[listeFils.size()][2];
			for (int i = 0; i < listeFils.size(); i++)
			{
				donneesTableauFils[i][0] = listeFils.get(i).libelleMot.toUpperCase();
				donneesTableauFils[i][1] = listeFils.get(i).definitionMot;
				tableauFils = new JTable(donneesTableauFils,nomColonnes);
			}
		}
		tableauFils.setDefaultRenderer(Object.class, new RenduCellule());
		tableauFils.setShowVerticalLines(false);
		
		panDescription = new JPanel();
		panDescription.setMinimumSize(new Dimension(10, 150));
		panDescription.setLayout(new BoxLayout(panDescription, BoxLayout.Y_AXIS));
		panLabelDescription = new JPanel();
		panLabelDescription.setMaximumSize(new Dimension(200, 200));
		panLabelDescription.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		labelDescription = new JLabel(" Description :");
		scrollPanDescription = new JScrollPane();
		textAreaDescription = new JTextArea(mot.definitionMot);
		
		panButtonsConsulter = new JPanel();
		buttonEnregistrer = new JButton("Enregistrer les modifications");
		buttonSupprimer = new JButton("Supprimer l'entrée");
		
	}
	
	public void afficher() 
	{
		Controller.fenetre.getVueCourante().panConsulter.removeAll();
		System.out.println(Controller.fenetre.getVueCourante().panConsulter);
		Controller.fenetre.getVueCourante().panConsulter.setLayout(new BoxLayout(Controller.fenetre.getVueCourante().panConsulter, BoxLayout.Y_AXIS));
		
		panChampRecherche.add(labelChampRecherche);
		panChampRecherche.add(textFieldChampRecherche);
		panChampRecherche.add(buttonChampRecherche);
		Controller.fenetre.getVueCourante().panConsulter.add(panChampRecherche);
		
		panEntreeRecherchee.add(labelEntreeRecherchee);
		panEntreeRecherchee.add(labelMotEntreeRecherchee);
		Controller.fenetre.getVueCourante().panConsulter.add(panEntreeRecherchee);
		
		panParent.add(labelParent);
		panParent.add(buttonMotParent);
		Controller.fenetre.getVueCourante().panConsulter.add(panParent);
		
		panSynonymes.add(panLabelSynonymes);
		panLabelSynonymes.add(labelSynonymes);
		panSynonymes.add(scrollPanSynonymes);
		scrollPanSynonymes.setViewportView(tableauSynonymes);
		Controller.fenetre.getVueCourante().panConsulter.add(panSynonymes);
		
		Controller.fenetre.getVueCourante().panConsulter.add(panFils);
		panFils.add(panLabelFils);
		panLabelFils.add(labelFils);
		panFils.add(scrollPanFils);
		scrollPanFils.setViewportView(tableauFils);
		
		panDescription.add(panLabelDescription);
		panLabelDescription.add(labelDescription);
		Controller.fenetre.getVueCourante().panConsulter.add(panDescription);
		panDescription.add(scrollPanDescription);
		scrollPanDescription.setViewportView(textAreaDescription);
		
		panButtonsConsulter.add(buttonEnregistrer);
		panButtonsConsulter.add(buttonSupprimer);
		buttonEnregistrer.addActionListener(new ControllerMots());
		buttonSupprimer.addActionListener(new ControllerMots());
		Controller.fenetre.getVueCourante().panConsulter.add(panButtonsConsulter);
		
		Controller.fenetre.getVueCourante().panConsulter.revalidate();
	}
}
