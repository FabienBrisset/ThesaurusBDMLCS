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
	private Object[][] donneesTableauSynonymesGauche = {{"Aucun mot", "Aucun mot"},};
	private Object[][] donneesTableauSynonymesDroite = {{"Aucun mot", "Aucun mot"},};
	private Object[][] donneesTableauAssosGauche = {{"Aucun mot", "Aucun mot"},};
	private Object[][] donneesTableauAssosDroite = {{"Aucun mot", "Aucun mot"},};
	private String[] nomColonnesAssos = {"Entrée (Associations)", "Description"};
	private String[] nomColonnesSynonyme = {"Entrée (Synonymes)", "Description"};
	
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
	
	VueTableauxMots vueTableauxMots;
	
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
		
		vueTableauxMots = new VueTableauxMots(listeMots, comboBoxParent.getSelectedItem().toString().toLowerCase());
	
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
		
		Controller.fenetre.getVueCourante().panAjouter.add(vueTableauxMots.getPanLabelSynonyme());
		Controller.fenetre.getVueCourante().panAjouter.add(vueTableauxMots.getPanSplit());
		
		panLabelDescription.add(labelDescription);
		panDescription.add(panLabelDescription);
		panDescription.add(scrollPanDescription);
		Controller.fenetre.getVueCourante().panAjouter.add(panDescription);
		
		panButton.add(buttonAjouterEntree);
		Controller.fenetre.getVueCourante().panAjouter.add(panButton);
	}
	
	public VueTableauxMots getVueTableauxMots() {
		return vueTableauxMots;
	}

	public void setVueTableauxMots(VueTableauxMots vueTableauxMots) {
		this.vueTableauxMots = vueTableauxMots;
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
		return vueTableauxMots.getDonneesTableauSynonymesGauche();
	}

	public void setDonneesTableauSynonymesGauche(
			Object[][] donneesTableauSynonymesGauche) {
		this.vueTableauxMots.setDonneesTableauSynonymesGauche(donneesTableauSynonymesGauche);
	}

	public Object[][] getDonneesTableauSynonymesDroite() {
		return vueTableauxMots.getDonneesTableauSynonymesDroite();
	}

	public void setDonneesTableauSynonymesDroite(
			Object[][] donneesTableauSynonymesDroite) {
		this.vueTableauxMots.setDonneesTableauSynonymesDroite(donneesTableauSynonymesDroite);
	}

	public Object[][] getDonneesTableauAssosGauche() {
		return vueTableauxMots.getDonneesTableauAssosGauche();
	}

	public void setDonneesTableauAssosGauche(Object[][] donneesTableauAssosGauche) {
		this.vueTableauxMots.setDonneesTableauAssosGauche(donneesTableauAssosGauche);
	}

	public Object[][] getDonneesTableauAssosDroite() {
		return vueTableauxMots.getDonneesTableauAssosDroite();
	}

	public void setDonneesTableauAssosDroite(Object[][] donneesTableauAssosDroite) {
		this.vueTableauxMots.setDonneesTableauAssosDroite(donneesTableauAssosDroite);
	}

	public JTable getTableauSynonymeGauche() {
		return vueTableauxMots.getTableauSynonymeGauche();
	}

	public void setTableauSynonymeGauche(JTable tableauSynonymeGauche) {
		this.vueTableauxMots.setTableauSynonymeGauche(tableauSynonymeGauche);
	}

	public JTable getTableauSynonymeDroite() {
		return vueTableauxMots.getTableauSynonymeDroite();
	}

	public void setTableauSynonymeDroite(JTable tableauSynonymeDroite) {
		this.vueTableauxMots.setTableauSynonymeDroite(tableauSynonymeDroite);
	}

	public JTable getTableauAssosGauche() {
		return vueTableauxMots.getTableauAssosGauche();
	}

	public void setTableauAssosGauche(JTable tableauAssosGauche) {
		this.vueTableauxMots.setTableauAssosGauche(tableauAssosGauche);
	}

	public JTable getTableauAssosDroite() {
		return vueTableauxMots.getTableauAssosDroite();
	}

	public void setTableauAssosDroite(JTable tableauAssosDroite) {

		this.vueTableauxMots.setTableauAssosDroite(tableauAssosDroite);
	}
	
	public DefaultTableModel getModelSynonymeGauche() {
		return vueTableauxMots.getModelSynonymeGauche();
	}

	public void setModelSynonymeGauche(DefaultTableModel modelSynonymeGauche) {
		this.vueTableauxMots.setModelSynonymeGauche(modelSynonymeGauche);
	}

	public DefaultTableModel getModelAssosGauche() {
		return vueTableauxMots.getModelAssosGauche();
	}

	public void setModelAssosGauche(DefaultTableModel modelAssosGauche) {
		this.vueTableauxMots.setModelAssosGauche(modelAssosGauche);
	}

	public DefaultTableModel getModelSynonymeDroite() {
		return vueTableauxMots.getModelSynonymeDroite();
	}

	public void setModelSynonymeDroite(DefaultTableModel modelSynonymeDroite) {
		this.vueTableauxMots.setModelSynonymeDroite(modelSynonymeDroite);
	}

	public DefaultTableModel getModelAssosDroite() {
		return vueTableauxMots.getModelAssosDroite();
	}

	public void setModelAssosDroite(DefaultTableModel modelAssosDroite) {
		this.vueTableauxMots.setModelAssosDroite(modelAssosDroite);
	}

}
