package thesaurus;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VueMot {

	private Mot mot;
	
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
	private JLabel labelMotParent;
	
	private JPanel panFils;
	private JPanel panLabelFils; // Pas réussi à faire sans en gardant le centrage du label
	private JLabel labelFils;
	private JScrollPane scrollPanFils;
	private RenduJTable tableauFils;
	
	private JPanel panDescription;
	private JPanel panLabelDescription;
	private JLabel labelDescription;
	private JScrollPane scrollPanDescription;
	private JTextArea textAreaDescription;

	public JTextArea getTextAreaDescription() {
		return textAreaDescription;
	}

	public void setTextAreaDescription(JTextArea textAreaDescription) {
		this.textAreaDescription = textAreaDescription;
	}

	private JPanel panButtonsConsulter;
	private JButton buttonEnregistrer;
	private JButton buttonSupprimer;
	
	VueTableauxConsult vueTableauxConsult;
	
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
		if(mot.getPereMot() != null)
		{
			labelMotParent = new JLabel(mot.getPereMot().getLibelleMot().toUpperCase());
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
		
		panFils = new JPanel();
		panFils.setLayout(new BoxLayout(panFils, BoxLayout.Y_AXIS));
		panLabelFils = new JPanel();
		labelFils = new JLabel("Fils :");
		scrollPanFils = new JScrollPane();
		ArrayList<Mot> listeFils = mot.getFilsMot();
		if(listeFils.size() == 0)
		{
			Object[][] donneesTableauFils= {{"Aucun fils", "Aucun fils"},};
			tableauFils = new RenduJTable(donneesTableauFils, nomColonnes);
		}
		else
		{
			listeFils = Mot.ArrayListEnOrdreAlphabetique(listeFils);
			donneesTableauFils = new Object[listeFils.size()][2];
			for (int i = 0; i < listeFils.size(); i++)
			{
				donneesTableauFils[i][0] = listeFils.get(i).libelleMot.toUpperCase();
				donneesTableauFils[i][1] = listeFils.get(i).definitionMot;
				tableauFils = new RenduJTable(donneesTableauFils,nomColonnes);
			}
		}
		tableauFils.setDefaultRenderer(Object.class, new RenduCellule());
		tableauFils.setShowVerticalLines(false);
		
		ArrayList<Mot> listeMots = Mot.listeDesMots();
		vueTableauxConsult = new VueTableauxConsult(listeMots, mot);		
		
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
	
	public JLabel getLabelChampRecherche() {
		return labelChampRecherche;
	}

	public void setLabelChampRecherche(JLabel labelChampRecherche) {
		this.labelChampRecherche = labelChampRecherche;
	}

	public JLabel getLabelMotEntreeRecherchee() {
		return labelMotEntreeRecherchee;
	}

	public void setLabelMotEntreeRecherchee(JLabel labelMotEntreeRecherchee) {
		this.labelMotEntreeRecherchee = labelMotEntreeRecherchee;
	}

	public void afficher() 
	{
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
		labelMotParent.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelMotParent.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(!labelMotParent.getText().equals("Aucun"))
					Controller.getControllerMots().afficherMot(new Mot(labelMotParent.getText().toLowerCase()));
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		Controller.fenetre.getVueCourante().panConsulter.add(panParent);

		Controller.fenetre.getVueCourante().panConsulter.add(panFils);
		panFils.add(panLabelFils);
		panLabelFils.add(labelFils);
		panFils.add(scrollPanFils);
		scrollPanFils.setViewportView(tableauFils);
		
		Controller.fenetre.getVueCourante().panConsulter.add(vueTableauxConsult.getPanLabelSynonyme());
		Controller.fenetre.getVueCourante().panConsulter.add(vueTableauxConsult.getPanSplit());
		
		panDescription.add(panLabelDescription);
		panLabelDescription.add(labelDescription);
		Controller.fenetre.getVueCourante().panConsulter.add(panDescription);
		panDescription.add(scrollPanDescription);
		scrollPanDescription.setViewportView(textAreaDescription);
		
		panButtonsConsulter.add(buttonEnregistrer);
		panButtonsConsulter.add(buttonSupprimer);
		buttonEnregistrer.addActionListener(Controller.getControllerMots());
		buttonSupprimer.addActionListener(Controller.getControllerMots());
		buttonChampRecherche.addActionListener(Controller.getControllerMots());
		Controller.fenetre.getVueCourante().panConsulter.add(panButtonsConsulter);	
		Controller.fenetre.getVueCourante().panConsulter.revalidate();
	}

	public JTextField getTextFieldChampRecherche() {
		return textFieldChampRecherche;
	}

	public void setTextFieldChampRecherche(JTextField textFieldChampRecherche) {
		this.textFieldChampRecherche = textFieldChampRecherche;
	}

	public JButton getButtonChampRecherche() {
		return buttonChampRecherche;
	}

	public void setButtonChampRecherche(JButton buttonChampRecherche) {
		this.buttonChampRecherche = buttonChampRecherche;
	}

	public JLabel getLabelFils() {
		return labelFils;
	}

	public void setLabelFils(JLabel labelFils) {
		this.labelFils = labelFils;
	}

	public JLabel getLabelEntreeRecherchee() {
		return labelEntreeRecherchee;
	}

	public void setLabelEntreeRecherchee(JLabel labelEntreeRecherchee) {
		this.labelEntreeRecherchee = labelEntreeRecherchee;
	}

	public JButton getButtonSupprimer() {
		return buttonSupprimer;
	}

	public void setButtonSupprimer(JButton buttonSupprimer) {
		this.buttonSupprimer = buttonSupprimer;
	}

	public VueTableauxConsult getVueTableauxConsult() {
		return vueTableauxConsult;
	}

	public void setVueTableauxConsult(VueTableauxConsult vueTableauxConsult) {
		this.vueTableauxConsult = vueTableauxConsult;
	}
}