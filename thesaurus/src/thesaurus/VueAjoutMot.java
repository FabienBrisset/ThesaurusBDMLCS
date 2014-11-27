package thesaurus;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders.SplitPaneBorder;
import javax.swing.table.DefaultTableModel;

public class VueAjoutMot 
{
	ArrayList<Mot> mots;
	
	JTextField textFieldNouvelleEntree;
	DefaultTableModel modelSynonymeGauche;
	DefaultTableModel modelAssosGauche;
	DefaultTableModel modelSynonymeDroite;
	DefaultTableModel modelAssosDroite;
	
	JPanel panNouvelleEntree;
	JPanel panParent;
	JPanel panLabelDescription;
	JPanel panDescription;
	JPanel panButton;
	JPanel panSplit;

	JLabel labelNouvelleEntree;
	JLabel labelParentExiste;

	JLabel labelDescription;
	
	String[] donneesComboBox;
	JComboBox comboBoxParent;
	JScrollPane scrollPanDescription;
	JTextArea textAreaDescription;
	JTextArea textAreaSynonyme;
	JButton buttonAjouterEntree;
	
	JPanel panLabelSynonyme;
	JLabel labelSynonyme;
	JSplitPane splitPanSynonyme;
	JScrollPane scrollPanSynonymeGauche;
	JScrollPane scrollPanSynonymeDroite;
	JTable tableauSynonymeGauche;
	JTable tableauSynonymeDroite;
	
	JPanel panAssos;
	JPanel panLabelAssos;
	JLabel labelAssos;
	JSplitPane splitPanAssos;
	JScrollPane scrollPanAssosGauche;
	JScrollPane scrollPanAssosDroite;
	JTable tableauAssosGauche;
	JTable tableauAssosDroite;
	
	VueTableauxAjout vueTableauxAjout;
	
	public VueAjoutMot(ArrayList<Mot> listeMots) throws SQLException 
	{	
		mots = listeMots;
		mots = Mot.ArrayListEnOrdreAlphabetique(mots);
		listeMots = Mot.ArrayListEnOrdreAlphabetique(listeMots);
		
		panNouvelleEntree = new JPanel();
		panNouvelleEntree.setMaximumSize(new Dimension(32767, 200));
		labelNouvelleEntree = new JLabel("Intitul\u00E9 de la nouvelle entr\u00E9e :");
		textFieldNouvelleEntree = new JTextField();
		textFieldNouvelleEntree.setColumns(30);
		
		panParent = new JPanel();
		panParent.setMaximumSize(new Dimension(32767, 200));
		labelParentExiste = new JLabel("Parent :");
		donneesComboBox = new String[listeMots.size()];
		for (int i = 0; i < listeMots.size(); i++) 
		{
			donneesComboBox[i] = new String(listeMots.get(i).getLibelleMot().toUpperCase());
		}
		comboBoxParent = new JComboBox(donneesComboBox);
		comboBoxParent.addActionListener(Controller.getControllerMots());
		
		vueTableauxAjout = new VueTableauxAjout(listeMots, comboBoxParent.getSelectedItem().toString().toLowerCase());
	
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
		buttonAjouterEntree.addActionListener(Controller.getControllerMots());
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
		
		Controller.fenetre.getVueCourante().panAjouter.add(vueTableauxAjout.getPanLabelSynonyme());
		Controller.fenetre.getVueCourante().panAjouter.add(vueTableauxAjout.getPanSplit());
		
		panLabelDescription.add(labelDescription);
		panDescription.add(panLabelDescription);
		panDescription.add(scrollPanDescription);
		Controller.fenetre.getVueCourante().panAjouter.add(panDescription);
		
		panButton.add(buttonAjouterEntree);
		Controller.fenetre.getVueCourante().panAjouter.add(panButton);
	}
	
	public VueTableauxAjout getVueTableauxMots() {
		return vueTableauxAjout;
	}

	public void setVueTableauxMots(VueTableauxAjout vueTableauxAjout) {
		this.vueTableauxAjout = vueTableauxAjout;
	}

	public JTextField getTextFieldNouvelleEntree() {
		return this.textFieldNouvelleEntree;
	}
	
	public JComboBox getJComboBox() {
		return this.comboBoxParent;
	}
	
	public JTextArea getTextAreaSynonyme() {
		return this.textAreaSynonyme;
	}
	
	public JTextArea getTextAreaDescription() {
		return this.textAreaDescription;
	}

	public Object[][] getDonneesTableauSynonymesGauche() {
		return vueTableauxAjout.getDonneesTableauSynonymesGauche();
	}

	public void setDonneesTableauSynonymesGauche(
			Object[][] donneesTableauSynonymesGauche) {
		this.vueTableauxAjout.setDonneesTableauSynonymesGauche(donneesTableauSynonymesGauche);
	}

	public Object[][] getDonneesTableauSynonymesDroite() {
		return vueTableauxAjout.getDonneesTableauSynonymesDroite();
	}

	public void setDonneesTableauSynonymesDroite(
			Object[][] donneesTableauSynonymesDroite) {
		this.vueTableauxAjout.setDonneesTableauSynonymesDroite(donneesTableauSynonymesDroite);
	}

	public Object[][] getDonneesTableauAssosGauche() {
		return vueTableauxAjout.getDonneesTableauAssosGauche();
	}

	public void setDonneesTableauAssosGauche(Object[][] donneesTableauAssosGauche) {
		this.vueTableauxAjout.setDonneesTableauAssosGauche(donneesTableauAssosGauche);
	}

	public Object[][] getDonneesTableauAssosDroite() {
		return vueTableauxAjout.getDonneesTableauAssosDroite();
	}

	public void setDonneesTableauAssosDroite(Object[][] donneesTableauAssosDroite) {
		this.vueTableauxAjout.setDonneesTableauAssosDroite(donneesTableauAssosDroite);
	}

	public JTable getTableauSynonymeGauche() {
		return vueTableauxAjout.getTableauSynonymeGauche();
	}

	public void setTableauSynonymeGauche(JTable tableauSynonymeGauche) {
		this.vueTableauxAjout.setTableauSynonymeGauche(tableauSynonymeGauche);
	}

	public JTable getTableauSynonymeDroite() {
		return vueTableauxAjout.getTableauSynonymeDroite();
	}

	public void setTableauSynonymeDroite(JTable tableauSynonymeDroite) {
		this.vueTableauxAjout.setTableauSynonymeDroite(tableauSynonymeDroite);
	}

	public JTable getTableauAssosGauche() {
		return vueTableauxAjout.getTableauAssosGauche();
	}

	public void setTableauAssosGauche(JTable tableauAssosGauche) {
		this.vueTableauxAjout.setTableauAssosGauche(tableauAssosGauche);
	}

	public JTable getTableauAssosDroite() {
		return vueTableauxAjout.getTableauAssosDroite();
	}

	public void setTableauAssosDroite(JTable tableauAssosDroite) {

		this.vueTableauxAjout.setTableauAssosDroite(tableauAssosDroite);
	}
	
	public DefaultTableModel getModelSynonymeGauche() {
		return vueTableauxAjout.getModelSynonymeGauche();
	}

	public void setModelSynonymeGauche(DefaultTableModel modelSynonymeGauche) {
		this.vueTableauxAjout.setModelSynonymeGauche(modelSynonymeGauche);
	}

	public DefaultTableModel getModelAssosGauche() {
		return vueTableauxAjout.getModelAssosGauche();
	}

	public void setModelAssosGauche(DefaultTableModel modelAssosGauche) {
		this.vueTableauxAjout.setModelAssosGauche(modelAssosGauche);
	}

	public DefaultTableModel getModelSynonymeDroite() {
		return vueTableauxAjout.getModelSynonymeDroite();
	}

	public void setModelSynonymeDroite(DefaultTableModel modelSynonymeDroite) {
		this.vueTableauxAjout.setModelSynonymeDroite(modelSynonymeDroite);
	}

	public DefaultTableModel getModelAssosDroite() {
		return vueTableauxAjout.getModelAssosDroite();
	}

	public void setModelAssosDroite(DefaultTableModel modelAssosDroite) {
		this.vueTableauxAjout.setModelAssosDroite(modelAssosDroite);
	}

}
