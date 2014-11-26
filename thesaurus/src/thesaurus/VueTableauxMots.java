package thesaurus;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VueTableauxMots extends JPanel 
{
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
	
	private JPanel panSplit;
	private JPanel panLabelSynonyme;
	private JLabel labelSynonyme;
	
	private JSplitPane splitPanSynonyme;
	private JScrollPane scrollPanSynonymeGauche;
	private JScrollPane scrollPanSynonymeDroite;
	private JTable tableauSynonymeGauche;
	private JTable tableauSynonymeDroite;
	
	private JSplitPane splitPanAssos;
	private JScrollPane scrollPanAssosGauche;
	private JScrollPane scrollPanAssosDroite;
	private JTable tableauAssosGauche;
	private JTable tableauAssosDroite;
	
	public VueTableauxMots(ArrayList<Mot> listeMots, String parent)
	{
		listeMots = Mot.ArrayListEnOrdreAlphabetique(listeMots);
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
			int i = 0;
			int curseur = 0;
			while (i < listeMots.size())
			{
				if(!parent.toLowerCase().equals(listeMots.get(i).libelleMot.toLowerCase()))
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
			tableauSynonymeGauche.addMouseListener(Controller.getControllerMots());
			tableauAssosGauche.addMouseListener(Controller.getControllerMots());
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
		tableauSynonymeDroite.addMouseListener(Controller.getControllerMots());
		tableauAssosDroite.addMouseListener(Controller.getControllerMots());
		
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
		
		panSplit.add(splitPanAssos);
		panSplit.add(splitPanSynonyme);
		panLabelSynonyme.add(labelSynonyme);
	}

	public JPanel getPanSplit() {
		return panSplit;
	}

	public void setPanSplit(JPanel panSplit) {
		this.panSplit = panSplit;
	}

	public JPanel getPanLabelSynonyme() {
		return panLabelSynonyme;
	}

	public void setPanLabelSynonyme(JPanel panLabelSynonyme) {
		this.panLabelSynonyme = panLabelSynonyme;
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

	public String[] getNomColonnesAssos() {
		return nomColonnesAssos;
	}

	public void setNomColonnesAssos(String[] nomColonnesAssos) {
		this.nomColonnesAssos = nomColonnesAssos;
	}

	public String[] getNomColonnesSynonyme() {
		return nomColonnesSynonyme;
	}

	public void setNomColonnesSynonyme(String[] nomColonnesSynonyme) {
		this.nomColonnesSynonyme = nomColonnesSynonyme;
	}

	public JLabel getLabelSynonyme() {
		return labelSynonyme;
	}

	public void setLabelSynonyme(JLabel labelSynonyme) {
		this.labelSynonyme = labelSynonyme;
	}

	public JSplitPane getSplitPanSynonyme() {
		return splitPanSynonyme;
	}

	public void setSplitPanSynonyme(JSplitPane splitPanSynonyme) {
		this.splitPanSynonyme = splitPanSynonyme;
	}

	public JScrollPane getScrollPanSynonymeGauche() {
		return scrollPanSynonymeGauche;
	}

	public void setScrollPanSynonymeGauche(JScrollPane scrollPanSynonymeGauche) {
		this.scrollPanSynonymeGauche = scrollPanSynonymeGauche;
	}

	public JScrollPane getScrollPanSynonymeDroite() {
		return scrollPanSynonymeDroite;
	}

	public void setScrollPanSynonymeDroite(JScrollPane scrollPanSynonymeDroite) {
		this.scrollPanSynonymeDroite = scrollPanSynonymeDroite;
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

	public JSplitPane getSplitPanAssos() {
		return splitPanAssos;
	}

	public void setSplitPanAssos(JSplitPane splitPanAssos) {
		this.splitPanAssos = splitPanAssos;
	}

	public JScrollPane getScrollPanAssosGauche() {
		return scrollPanAssosGauche;
	}

	public void setScrollPanAssosGauche(JScrollPane scrollPanAssosGauche) {
		this.scrollPanAssosGauche = scrollPanAssosGauche;
	}

	public JScrollPane getScrollPanAssosDroite() {
		return scrollPanAssosDroite;
	}

	public void setScrollPanAssosDroite(JScrollPane scrollPanAssosDroite) {
		this.scrollPanAssosDroite = scrollPanAssosDroite;
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

}
