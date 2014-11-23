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
	JScrollPane scrollPanSynonyme;
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
	
	JPanel panButtonTableaux;
	JButton buttonAssosVersGauche;
	JButton buttonAssosVersDroite;
	JButton buttonSynonymeversGauche;
	JButton buttonSynonymeVersDroite;
	
	
	public VueAjoutMot(ArrayList<Mot> listeMots) throws SQLException 
	{	
		mots = listeMots;
		
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
		comboBoxParent.addActionListener(new ControllerMots());
		
		panSplit = new JPanel();
		panSplit.setLayout(new BoxLayout(panSplit, BoxLayout.X_AXIS));
		panSplit.setMaximumSize(new Dimension(32767, 50));
		panLabelSynonyme = new JPanel();
		panLabelSynonyme.setMaximumSize(new Dimension(32767, 100));
		labelSynonyme = new JLabel("Association(s) et Synonyme(s) :");
		scrollPanSynonymeGauche = new JScrollPane();
		scrollPanSynonymeDroite = new JScrollPane();
		
		modelSynonymeGauche = new DefaultTableModel(donneesTableauSynonymesGauche,nomColonnesSynonyme);
		tableauSynonymeGauche = new JTable(modelSynonymeGauche);
		if(listeMots.size() == 0)
		{
			modelAssosGauche = new DefaultTableModel(donneesTableauAssosGauche,nomColonnesAssos);
			tableauAssosGauche = new JTable(modelAssosGauche);
		}
		else
		{
			donneesTableauAssosGauche = new Object[listeMots.size()-1][2];
			//for (int i = 0; i < listeMots.size()-1; i++)
			int i = 0;
			int curseur = 0;
			while (i < listeMots.size())
			{
				if(!comboBoxParent.getSelectedItem().toString().toLowerCase().equals(listeMots.get(i).libelleMot.toLowerCase()))
				{
					System.out.println(i);
					donneesTableauAssosGauche[curseur][0] = listeMots.get(i).libelleMot.toUpperCase();
					donneesTableauAssosGauche[curseur][1] = listeMots.get(i).definitionMot;
					curseur++;
				}
				i++;
			}
			modelAssosGauche = new DefaultTableModel(donneesTableauAssosGauche,nomColonnesAssos);
			tableauAssosGauche = new JTable(modelAssosGauche);
			tableauSynonymeGauche.addMouseListener(new ControllerMots());
			tableauAssosGauche.addMouseListener(new ControllerMots());
		}
		tableauSynonymeGauche.setDefaultRenderer(Object.class, new RenduCellule());
		((DefaultTableModel) tableauSynonymeGauche.getModel()).removeRow(0);
		tableauAssosGauche.setDefaultRenderer(Object.class, new RenduCellule());
		modelSynonymeDroite = new DefaultTableModel(donneesTableauSynonymesDroite,nomColonnesSynonyme);
		modelAssosDroite = new DefaultTableModel(donneesTableauAssosDroite,nomColonnesAssos);
		tableauSynonymeDroite = new JTable(modelSynonymeDroite);
		((DefaultTableModel) tableauSynonymeDroite.getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
		tableauAssosDroite = new JTable(modelAssosDroite);
		((DefaultTableModel) tableauAssosDroite.getModel()).removeRow(0);
		tableauSynonymeDroite.setDefaultRenderer(Object.class, new RenduCellule());
		tableauAssosDroite.setDefaultRenderer(Object.class, new RenduCellule());
		tableauSynonymeDroite.addMouseListener(new ControllerMots());
		tableauAssosDroite.addMouseListener(new ControllerMots());
		
		scrollPanSynonymeGauche.setViewportView(tableauSynonymeGauche);
		scrollPanSynonymeDroite.setViewportView(tableauSynonymeDroite);
		splitPanSynonyme = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollPanSynonymeGauche,scrollPanSynonymeDroite);
		splitPanSynonyme.setDividerLocation(300);
		
		scrollPanAssosGauche = new JScrollPane();
		scrollPanAssosDroite = new JScrollPane();
		scrollPanAssosGauche.setViewportView(tableauAssosGauche);
		scrollPanAssosDroite.setViewportView(tableauAssosDroite);
		splitPanAssos = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollPanAssosGauche,scrollPanAssosDroite);
		splitPanAssos.setDividerLocation(300);
		
		panButtonTableaux = new JPanel();
		panButtonTableaux.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonAssosVersGauche = new JButton("Enlever Association");
		buttonAssosVersDroite = new JButton("Ajouter Association");
		buttonSynonymeversGauche = new JButton("Enlever Synonyme");
		buttonSynonymeVersDroite = new JButton("Ajouter Synonyme");
	
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
		buttonAjouterEntree.addActionListener(new ControllerMots());
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
		
		panSplit.add(splitPanAssos);
		panSplit.add(splitPanSynonyme);
		panLabelSynonyme.add(labelSynonyme);
		Controller.fenetre.getVueCourante().panAjouter.add(panLabelSynonyme);
		Controller.fenetre.getVueCourante().panAjouter.add(panSplit);
		
		panLabelDescription.add(labelDescription);
		panDescription.add(panLabelDescription);
		panDescription.add(scrollPanDescription);
		Controller.fenetre.getVueCourante().panAjouter.add(panDescription);
		
		panButton.add(buttonAjouterEntree);
		Controller.fenetre.getVueCourante().panAjouter.add(panButton);
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
		return donneesTableauSynonymesGauche;
	}

	public void setDonneesTableauSynonymesGauche(
			Object[][] donneesTableauSynonymesGauche) {
		this.donneesTableauSynonymesGauche = donneesTableauSynonymesGauche;
	}

	public Object[][] getDonneesTableauSynonymesDroite() {
		return donneesTableauSynonymesDroite;
	}

	public void setDonneesTableauSynonymesDroite(
			Object[][] donneesTableauSynonymesDroite) {
		this.donneesTableauSynonymesDroite = donneesTableauSynonymesDroite;
	}

	public Object[][] getDonneesTableauAssosGauche() {
		return donneesTableauAssosGauche;
	}

	public void setDonneesTableauAssosGauche(Object[][] donneesTableauAssosGauche) {
		this.donneesTableauAssosGauche = donneesTableauAssosGauche;
	}

	public Object[][] getDonneesTableauAssosDroite() {
		return donneesTableauAssosDroite;
	}

	public void setDonneesTableauAssosDroite(Object[][] donneesTableauAssosDroite) {
		this.donneesTableauAssosDroite = donneesTableauAssosDroite;
	}

	public JTable getTableauSynonymeGauche() {
		return tableauSynonymeGauche;
	}

	public void setTableauSynonymeGauche(JTable tableauSynonymeGauche) {
		this.tableauSynonymeGauche = tableauSynonymeGauche;
	}

	public JTable getTableauSynonymeDroite() {
		return tableauSynonymeDroite;
	}

	public void setTableauSynonymeDroite(JTable tableauSynonymeDroite) {
		this.tableauSynonymeDroite = tableauSynonymeDroite;
	}

	public JTable getTableauAssosGauche() {
		return tableauAssosGauche;
	}

	public void setTableauAssosGauche(JTable tableauAssosGauche) {
		this.tableauAssosGauche = tableauAssosGauche;
	}

	public JTable getTableauAssosDroite() {
		return tableauAssosDroite;
	}

	public void setTableauAssosDroite(JTable tableauAssosDroite) {

		this.tableauAssosDroite = tableauAssosDroite;
	}
	
	public DefaultTableModel getModelSynonymeGauche() {
		return modelSynonymeGauche;
	}

	public void setModelSynonymeGauche(DefaultTableModel modelSynonymeGauche) {
		this.modelSynonymeGauche = modelSynonymeGauche;
	}

	public DefaultTableModel getModelAssosGauche() {
		return modelAssosGauche;
	}

	public void setModelAssosGauche(DefaultTableModel modelAssosGauche) {
		this.modelAssosGauche = modelAssosGauche;
	}

	public DefaultTableModel getModelSynonymeDroite() {
		return modelSynonymeDroite;
	}

	public void setModelSynonymeDroite(DefaultTableModel modelSynonymeDroite) {
		this.modelSynonymeDroite = modelSynonymeDroite;
	}

	public DefaultTableModel getModelAssosDroite() {
		return modelAssosDroite;
	}

	public void setModelAssosDroite(DefaultTableModel modelAssosDroite) {
		this.modelAssosDroite = modelAssosDroite;
	}

}
