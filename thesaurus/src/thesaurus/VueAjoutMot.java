package thesaurus;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
	RenduJTable tableauSynonymeGauche;
	RenduJTable tableauSynonymeDroite;
	
	JPanel panAssos;
	JPanel panLabelAssos;
	JLabel labelAssos;
	JSplitPane splitPanAssos;
	JScrollPane scrollPanAssosGauche;
	JScrollPane scrollPanAssosDroite;
	RenduJTable tableauAssosGauche;
	RenduJTable tableauAssosDroite;
	
	VueTableauxAjout vueTableauxAjout;
	
	public VueAjoutMot(ArrayList<Mot> listeMots) throws SQLException 
	{	
		mots = listeMots;
		mots = Mot.ArrayListEnOrdreAlphabetique(mots);
		listeMots = Mot.ArrayListEnOrdreAlphabetique(listeMots);
		ArrayList<VueAjoutMot> avam = new ArrayList<VueAjoutMot>(1);
		avam.add(this);
		
		panNouvelleEntree = new JPanel();
		panNouvelleEntree.setMaximumSize(new Dimension(32767, 200));
		labelNouvelleEntree = new JLabel("Intitul\u00E9 de la nouvelle entr\u00E9e :");
		textFieldNouvelleEntree = new JTextField();
		textFieldNouvelleEntree.setColumns(30);
		textFieldNouvelleEntree.setInputVerifier(new JTextFieldVerifier(1, avam));
		textFieldNouvelleEntree.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				new ControllerMots().insertUpdateNouvelleEntree(e, textFieldNouvelleEntree);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				new ControllerMots().insertUpdateNouvelleEntree(e, textFieldNouvelleEntree);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				new ControllerMots().insertUpdateNouvelleEntree(e, textFieldNouvelleEntree);
			}
			});
		
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

	public RenduJTable getTableauSynonymeGauche() {
		return vueTableauxAjout.getTableauSynonymeGauche();
	}

	public void setTableauSynonymeGauche(RenduJTable tableauSynonymeGauche) {
		this.vueTableauxAjout.setTableauSynonymeGauche(tableauSynonymeGauche);
	}

	public JTable getTableauSynonymeDroite() {
		return vueTableauxAjout.getTableauSynonymeDroite();
	}

	public void setTableauSynonymeDroite(RenduJTable tableauSynonymeDroite) {
		this.vueTableauxAjout.setTableauSynonymeDroite(tableauSynonymeDroite);
	}

	public RenduJTable getTableauAssosGauche() {
		return vueTableauxAjout.getTableauAssosGauche();
	}

	public void setTableauAssosGauche(RenduJTable tableauAssosGauche) {
		this.vueTableauxAjout.setTableauAssosGauche(tableauAssosGauche);
	}

	public RenduJTable getTableauAssosDroite() {
		return vueTableauxAjout.getTableauAssosDroite();
	}

	public void setTableauAssosDroite(RenduJTable tableauAssosDroite) {

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
