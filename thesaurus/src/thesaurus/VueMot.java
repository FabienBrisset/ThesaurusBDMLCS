package thesaurus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

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
	private Object[][] donneesTableauAssos;
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
	private JLabel labelMotParent;
	
	private JPanel panSynonyme;
	private JPanel panLabelSynonyme; // Pas r�ussi � faire sans en gardant le centrage du label
	private JLabel labelSynonyme;
	private JScrollPane scrollPanSynonyme; // Conteneur scrollable du tableau contenant les synonymes
	private JTable tableauSynonyme;
	
	private JPanel panFils;
	private JPanel panLabelFils; // Pas r�ussi � faire sans en gardant le centrage du label
	private JLabel labelFils;
	private JScrollPane scrollPanFils;
	private JTable tableauFils;
	
	private JPanel panAssos;
	private JPanel panLabelAssos; // Pas r�ussi � faire sans en gardant le centrage du label
	private JLabel labelAssos;
	private JScrollPane scrollPanAssos;
	private JTable tableauAssos;
	
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
		if(mot.getPere() != null)
		{
			labelMotParent = new JLabel(mot.getPere().getLibelleMot().toUpperCase());
			labelMotParent.setForeground(Color.BLUE);
			Font font = labelMotParent.getFont();
			Map attributes = font.getAttributes();
			attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			labelMotParent.setFont(font.deriveFont(attributes));
		}
		else
		{
			labelMotParent = new JLabel("Aucun");
		}
		
		panSynonyme = new JPanel();
		panSynonyme.setLayout(new BoxLayout(panSynonyme, BoxLayout.Y_AXIS));
		panLabelSynonyme = new JPanel();
		labelSynonyme = new JLabel("Synonyme(s) :");
		scrollPanSynonyme = new JScrollPane();
		//ArrayList<Mot> listeSynonymes = mot.getSynonymes(); PIERRE
		/* Fab */
		ArrayList<Mot> listeSynonymes = new ArrayList<Mot>(mot.getSynonymesMot().size());
		for (int i = 0; i < mot.getSynonymesMot().size(); i++) {
			if (mot.getMotByRef(mot.getSynonymesMot().get(i)) != null)
				listeSynonymes.add(mot.getMotByRef(mot.getSynonymesMot().get(i)));
		}
		/* */
		if(listeSynonymes.size() == 0)
		{
			Object[][] donneesTableauSynonymes= {{"Aucun synonyme", "Aucun synonyme"},};
			tableauSynonyme = new JTable(donneesTableauSynonymes,nomColonnes);
		}
		else
		{
			donneesTableauSynonymes = new Object[listeSynonymes.size()][2];
			
			for (int i = 0; i < listeSynonymes.size(); i++)
			{	
				donneesTableauSynonymes[i][0] = listeSynonymes.get(i).libelleMot.toUpperCase();
				donneesTableauSynonymes[i][1] = listeSynonymes.get(i).definitionMot;
				tableauSynonyme = new JTable(donneesTableauSynonymes,nomColonnes);
			}
		}
		tableauSynonyme.setDefaultRenderer(Object.class, new RenduCellule());
		tableauSynonyme.setShowVerticalLines(false);
		
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
		
		panAssos = new JPanel();
		panAssos.setLayout(new BoxLayout(panAssos, BoxLayout.Y_AXIS));
		panLabelAssos = new JPanel();
		labelAssos = new JLabel("Association(s) :");
		scrollPanAssos = new JScrollPane();
		ArrayList<Mot> listeAssos = mot.getAssociations();
		if(listeAssos.size() == 0)
		{
			Object[][] donneesTableauAssos= {{"Aucune association", "Aucune association"},};
			tableauAssos = new JTable(donneesTableauAssos, nomColonnes);
		}
		else
		{
			donneesTableauAssos = new Object[listeAssos.size()][2];
			for (int i = 0; i < listeAssos.size(); i++)
			{
				donneesTableauAssos[i][0] = listeAssos.get(i).libelleMot.toUpperCase();
				donneesTableauAssos[i][1] = listeAssos.get(i).definitionMot;
				tableauAssos = new JTable(donneesTableauAssos,nomColonnes);
			}
		}
		tableauAssos.setDefaultRenderer(Object.class, new RenduCellule());
		tableauAssos.setShowVerticalLines(false);
		
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
		if (Controller.fenetre.getVueCourante().panConsulter != null) {
			Controller.fenetre.getVueCourante().panConsulter.removeAll();
			Controller.fenetre.getVueCourante().panConsulter.setLayout(new BoxLayout(Controller.fenetre.getVueCourante().panConsulter, BoxLayout.Y_AXIS));
			
			panChampRecherche.add(labelChampRecherche);
			panChampRecherche.add(textFieldChampRecherche);
			panChampRecherche.add(buttonChampRecherche);
			Controller.fenetre.getVueCourante().panConsulter.add(panChampRecherche);
			
			panEntreeRecherchee.add(labelEntreeRecherchee);
			panEntreeRecherchee.add(labelMotEntreeRecherchee);
			Controller.fenetre.getVueCourante().panConsulter.add(panEntreeRecherchee);
			
			panParent.add(labelParent);
			panParent.add(labelMotParent);
			Controller.fenetre.getVueCourante().panConsulter.add(panParent);
			
			panSynonyme.add(panLabelSynonyme);
			panLabelSynonyme.add(labelSynonyme);
			panSynonyme.add(scrollPanSynonyme);
			scrollPanSynonyme.setViewportView(tableauSynonyme);
			Controller.fenetre.getVueCourante().panConsulter.add(panSynonyme);
			
			Controller.fenetre.getVueCourante().panConsulter.add(panFils);
			panFils.add(panLabelFils);
			panLabelFils.add(labelFils);
			panFils.add(scrollPanFils);
			scrollPanFils.setViewportView(tableauFils);
			
			Controller.fenetre.getVueCourante().panConsulter.add(panAssos);
			panAssos.add(panLabelAssos);
			panLabelAssos.add(labelAssos);
			panAssos.add(scrollPanAssos);
			scrollPanAssos.setViewportView(tableauAssos);
			
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
}