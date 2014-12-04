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

public class VueTableauxConsult extends JPanel {
	private	DefaultTableModel modelSynonymeGauche;
	private	DefaultTableModel modelAssosGauche;
	private	DefaultTableModel modelSynonymeDroite;
	private	DefaultTableModel modelAssosDroite;
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
	private RenduJTable tableauSynonymeGauche;
	private RenduJTable tableauSynonymeDroite;
	
	private JSplitPane splitPanAssos;
	private JScrollPane scrollPanAssosGauche;
	private JScrollPane scrollPanAssosDroite;
	private RenduJTable tableauAssosGauche;
	private RenduJTable tableauAssosDroite;

	public VueTableauxConsult(ArrayList<Mot> listeMots, Mot motCourant) {
		listeMots = Mot.ArrayListEnOrdreAlphabetique(listeMots);
		panSplit = new JPanel();
		panSplit.setLayout(new BoxLayout(panSplit, BoxLayout.X_AXIS));
		panSplit.setMaximumSize(new Dimension(32767, 50));
		panLabelSynonyme = new JPanel();
		panLabelSynonyme.setMaximumSize(new Dimension(32767, 100));
		labelSynonyme = new JLabel("Association(s) et Synonyme(s) :");
		scrollPanSynonymeGauche = new JScrollPane();
		scrollPanSynonymeDroite = new JScrollPane();
			
		if(listeMots.size() == 0)
		{
			modelAssosGauche = new DefaultTableModel(donneesTableauAssosGauche,nomColonnesAssos);
			tableauAssosGauche = new RenduJTable(modelAssosGauche);
			((DefaultTableModel) tableauAssosGauche.getModel()).removeRow(0);
			tableauAssosGauche.setDefaultRenderer(Object.class, new RenduCellule());
			tableauAssosGauche.addMouseListener(Controller.getControllerMots());
		}
		else
		{
			donneesTableauAssosGauche = new Object[listeMots.size()-(motCourant.getAssociationsMot().size())-1][2];
			int i = 0;
			int curseur = 0;
			while (i < listeMots.size())
			{
				if(!motCourant.estDansAssociations(listeMots.get(i)) && !listeMots.get(i).equals(motCourant))
				{
					donneesTableauAssosGauche[curseur][0] = listeMots.get(i).libelleMot.toUpperCase();
					donneesTableauAssosGauche[curseur][1] = listeMots.get(i).definitionMot;
					curseur++;
				}
				i++;
			}
			modelAssosGauche = new DefaultTableModel(donneesTableauAssosGauche,nomColonnesAssos);
			tableauAssosGauche = new RenduJTable(modelAssosGauche);
			tableauAssosGauche.setDefaultRenderer(Object.class, new RenduCellule());
			tableauAssosGauche.addMouseListener(Controller.getControllerMots());
		}
		
		ArrayList<Mot> listeAssos = motCourant.getAssociationsMot();
		if(listeAssos.size() == 0)
		{
			modelAssosDroite = new DefaultTableModel(donneesTableauAssosDroite,nomColonnesAssos);
			tableauAssosDroite = new RenduJTable(modelAssosDroite);
			((DefaultTableModel) tableauAssosDroite.getModel()).removeRow(0);
			tableauAssosDroite.setDefaultRenderer(Object.class, new RenduCellule());
			tableauAssosDroite.addMouseListener(Controller.getControllerMots());
		}
		else
		{
			listeAssos = Mot.ArrayListEnOrdreAlphabetique(listeAssos);
			donneesTableauAssosDroite = new Object[listeAssos.size()][2];
			for (int i = 0; i < listeAssos.size(); i++)
			{
				donneesTableauAssosDroite[i][0] = listeAssos.get(i).libelleMot.toUpperCase();
				donneesTableauAssosDroite[i][1] = listeAssos.get(i).definitionMot;
				modelAssosDroite = new DefaultTableModel(donneesTableauAssosDroite,nomColonnesAssos);
				tableauAssosDroite = new RenduJTable(modelAssosDroite);
			}
		}
		tableauAssosDroite.setDefaultRenderer(Object.class, new RenduCellule());
		tableauAssosDroite.setShowVerticalLines(false);
		tableauAssosDroite.addMouseListener(Controller.getControllerMots());
		
		if(motCourant.getPereMot() != null)
		{
			Mot motParent = new Mot(motCourant.getPereMot().getLibelleMot());
			ArrayList<Mot> listeFils = motParent.getFilsMot();
			listeFils = Mot.ArrayListEnOrdreAlphabetique(listeFils);
			if(listeFils.size() == 0)
			{
				modelSynonymeGauche = new DefaultTableModel(donneesTableauSynonymesGauche,nomColonnesSynonyme);
				tableauSynonymeGauche = new RenduJTable(modelSynonymeGauche);
				((DefaultTableModel) tableauSynonymeGauche.getModel()).removeRow(0);
				tableauSynonymeGauche.setDefaultRenderer(Object.class, new RenduCellule());
				tableauSynonymeGauche.addMouseListener(Controller.getControllerMots());
			}
			else
			{
				donneesTableauSynonymesGauche = new Object[listeFils.size()-(motCourant.getSynonymesMot().size())-1][2];
				int i = 0;
				int curseur = 0;
				while (i < listeFils.size())
				{
					if(!motCourant.estDansSynonymes(listeFils.get(i)) && !listeFils.get(i).equals(motCourant))
					{
						donneesTableauSynonymesGauche[curseur][0] = listeFils.get(i).libelleMot.toUpperCase();
						donneesTableauSynonymesGauche[curseur][1] = listeFils.get(i).definitionMot;
						curseur++;
					}
					i++;
				}
				modelSynonymeGauche = new DefaultTableModel(donneesTableauSynonymesGauche,nomColonnesSynonyme);
				tableauSynonymeGauche = new RenduJTable(modelSynonymeGauche);
				tableauSynonymeGauche.setDefaultRenderer(Object.class, new RenduCellule());
				tableauSynonymeGauche.addMouseListener(Controller.getControllerMots());
			}
		}
		else
		{
			modelSynonymeGauche = new DefaultTableModel(donneesTableauSynonymesGauche,nomColonnesSynonyme);
			tableauSynonymeGauche = new RenduJTable(modelSynonymeGauche);
			((DefaultTableModel) tableauSynonymeGauche.getModel()).removeRow(0);
			tableauSynonymeGauche.setDefaultRenderer(Object.class, new RenduCellule());
			tableauSynonymeGauche.addMouseListener(Controller.getControllerMots());
		}
		
		ArrayList<Mot> listeSynonymes = new ArrayList<Mot>();
		for (int i = 0; i < motCourant.getSynonymesMot().size(); i++) 
		{
			listeSynonymes.add(motCourant.getSynonymesMot().get(i));
		}
		if(listeSynonymes.size() == 0)
		{
			modelSynonymeDroite = new DefaultTableModel(donneesTableauSynonymesDroite,nomColonnesSynonyme);
			tableauSynonymeDroite = new RenduJTable(modelSynonymeDroite);
			((DefaultTableModel) tableauSynonymeDroite.getModel()).removeRow(0);
			tableauSynonymeDroite.setDefaultRenderer(Object.class, new RenduCellule());
			tableauSynonymeDroite.addMouseListener(Controller.getControllerMots());
		}
		else
		{
			listeSynonymes = Mot.ArrayListEnOrdreAlphabetique(listeSynonymes);
			donneesTableauSynonymesDroite = new Object[listeSynonymes.size()][2];
			
			for (int i = 0; i < listeSynonymes.size(); i++)
			{	
				donneesTableauSynonymesDroite[i][0] = listeSynonymes.get(i).getLibelleMot().toUpperCase();
				donneesTableauSynonymesDroite[i][1] = listeSynonymes.get(i).getDefinitionMot();
				modelSynonymeDroite = new DefaultTableModel(donneesTableauSynonymesDroite,nomColonnesSynonyme);
				tableauSynonymeDroite = new RenduJTable(modelSynonymeDroite);
			}
		}
		tableauSynonymeDroite.setDefaultRenderer(Object.class, new RenduCellule());
		tableauSynonymeDroite.setShowVerticalLines(false);
		tableauSynonymeDroite.addMouseListener(Controller.getControllerMots());
		
		Controller.fenetre.getVueCourante().revalidate();
		
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

	public RenduJTable getTableauSynonymeGauche() {
		return tableauSynonymeGauche;
	}

	public void setTableauSynonymeGauche(RenduJTable tableauSynonymeGauche) {
		this.tableauSynonymeGauche = tableauSynonymeGauche;
	}

	public RenduJTable getTableauSynonymeDroite() {
		return tableauSynonymeDroite;
	}

	public void setTableauSynonymeDroite(RenduJTable tableauSynonymeDroite) {
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

	public RenduJTable getTableauAssosGauche() {
		return tableauAssosGauche;
	}

	public void setTableauAssosGauche(RenduJTable tableauAssosGauche) {
		this.tableauAssosGauche = tableauAssosGauche;
	}

	public JTable getTableauAssosDroite() {
		return tableauAssosDroite;
	}

	public void setTableauAssosDroite(RenduJTable tableauAssosDroite) {
		this.tableauAssosDroite = tableauAssosDroite;
	}
}
