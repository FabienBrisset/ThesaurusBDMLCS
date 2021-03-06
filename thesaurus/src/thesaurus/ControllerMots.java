package thesaurus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;

public class ControllerMots extends Controller implements ActionListener, MouseListener {

	private Mot motCourant;

	public ControllerMots() {
		this.motCourant = new Mot("cuisine");
	}

	public ControllerMots(Mot m) {
		this.motCourant = m;
	}

	public void afficherMot(Mot m) {
		this.motCourant = m;
		VueMot vue = new VueMot(m);
		vue.afficher();
		Controller.fenetre.setVueMot(vue);
		if(motCourant.getPereMot() == null)
		{
			Controller.fenetre.getVueMot().getButtonSupprimer().setEnabled(false);
		}
		Controller.fenetre.getVueCourante().panConsulter.revalidate();

		Controller.fenetre.getVueCourante().listeOnglets.setSelectedIndex(1);
	}

	public void afficherArborescenceMot(Mot m) {
		this.motCourant = m;
		TreeNode<Mot> racine = new TreeNode<Mot>(m);
		TreeNode<Mot> arbre = m.getArborescence();
		VueArborescenceMot vue = new VueArborescenceMot(arbre);
		vue.afficher();
		Controller.fenetre.setVueArborescence(vue);
	}

	public void afficherAjout() throws SQLException {
		ArrayList<Mot> listeMots = Mot.listeDesMots();
		VueAjoutMot vue = new VueAjoutMot(listeMots);
		vue.afficher();
		Controller.fenetre.setVueAjout(vue);
	}

	public void ajouterMot(String mot, String pere, Object[][] assos, Object[][] synonymes, String description)
	{
		boolean verifyClassName = Controller.fenetre.getVueAjout().getTextFieldNouvelleEntree().getInputVerifier().verify(Controller.fenetre.getVueAjout().getTextFieldNouvelleEntree());
		if(!verifyClassName){
			return;
		}
		
		ArrayList<Mot> listeSynonymes = new ArrayList<Mot>();
		ArrayList<Mot> listeAssos = new ArrayList<Mot>();
		Mot motPere = null;
		Mot m = null;
		for (int i = 0; i < synonymes.length; i++) 
		{
			if (!synonymes[i][0].toString().toLowerCase().equals("aucun mot"))
			{
				m = new Mot(synonymes[i][0].toString().toLowerCase());
				if(m != null)
				{
					listeSynonymes.add(m);
				}
			}

		}
		for (int i = 0; i < assos.length; i++) 
		{
			if (!assos[i][0].toString().toLowerCase().equals("aucun mot"))
			{
				m = new Mot(assos[i][0].toString().toLowerCase());
				if(m != null)
				{
					listeAssos.add(m);
				}
			}

		}

		motPere = new Mot(pere.toLowerCase());

		Mot m2 = new Mot(null,mot.toLowerCase(), description, motPere, new ArrayList<Mot>(), listeSynonymes, listeAssos);
		m2.beginTransaction();
		boolean res = m2.insert();
		
		if (res)
		{
			m2.commit();
			this.afficherMot(m2);
		}
		else
		{
			m2.rollback();
			JOptionPane.showMessageDialog(fenetre, "Echec de l'insertion");
			System.out.println("Erreur : ajout du mot " + m2.getLibelleMot());
		}
		
	}

	public void modifierMot(Mot m, Object[][] assos, Object[][] synonymes, String description) { 
		
		Mot nouveauMot = new Mot(m.getOid(),m.getLibelleMot(),description,m.getPereMot(),m.getFilsMot(),null,null);
		
		ArrayList<Mot> listeSynonymes = new ArrayList<Mot>();
		ArrayList<Mot> listeAssos = new ArrayList<Mot>();
		
		Mot m1 = null;
		
		for (int i = 0; i < synonymes.length; i++) 
		{
			if (!synonymes[i][0].toString().toLowerCase().equals("aucun mot"))
			{
				m1 = new Mot(synonymes[i][0].toString().toLowerCase());
				if(m1 != null)
				{
					listeSynonymes.add(m1);
				}
			}
		}
		
		for (int i = 0; i < assos.length; i++) 
		{
			if (!assos[i][0].toString().toLowerCase().equals("aucun mot"))
			{
				m1 = new Mot(assos[i][0].toString().toLowerCase());
				if(m1 != null)
				{
					listeAssos.add(m1);
				}
			}
		}
		
		nouveauMot.setSynonymesMot(listeSynonymes);
		nouveauMot.setAssociationsMot(listeAssos);
		
		nouveauMot.beginTransaction();
		boolean res = nouveauMot.update(m);
		this.motCourant = nouveauMot;

		if (res) {
			nouveauMot.commit();
			JOptionPane.showMessageDialog(fenetre, "Mot modifié");
		}
		else {
			nouveauMot.rollback();
			JOptionPane.showMessageDialog(fenetre, "Echec de la modification");
		}
	}

	public void supprimerMot(Mot m) {
		if(m.getPereMot() == null){JOptionPane.showMessageDialog(fenetre, "Impossible de supprimer la racine");}
		else {
			m.beginTransaction();
			boolean res = m.delete();
			Mot pere = new Mot(m.getPereMot().getLibelleMot());
			
			if(res){
				m.commit();
				JOptionPane.showMessageDialog(fenetre, "Mot Supprimé");
				this.afficherMot(pere);
				}
			else{
				m.rollback();
				JOptionPane.showMessageDialog(fenetre, "Echec de la suppression");
				this.afficherMot(m);
				}
		}
	}

	public Mot getMotCourant() {
		return this.motCourant;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		switch(fenetre.getVueCourante().getListeOnglets().getSelectedIndex())
		{
		case 1 :
			this.afficherMot(this.motCourant);
			break;
		case 2 :
			try {
				this.afficherAjout();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case 3 :
			this.afficherArborescenceMot(this.motCourant);
			break;
		};
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getActionCommand().equals("Enregistrer les modifications")){
			String description = Controller.fenetre.getVueMot().getTextAreaDescription().getText();
			Object[][] synonymes = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauSynonymesDroite();
			Object[][] assos = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauAssosDroite();
			
			this.modifierMot(motCourant,assos,synonymes,description);
			this.afficherMot(motCourant);
		}

		if(arg0.getActionCommand().equals("Supprimer l'entrée")){
			this.supprimerMot(motCourant);
			this.afficherMot(motCourant);

		}

		if(arg0.getActionCommand().equals("Rechercher"))
		{
			String motaRechercher = Controller.fenetre.getVueMot().getTextFieldChampRecherche().getText().toLowerCase();
			Mot m = new Mot(motaRechercher);
			if (Mot.existe(m)) 
			{
				afficherMot(m);
			}
			else
			{
				JOptionPane.showMessageDialog(fenetre, "Ce mot n'existe pas !");
			}
		}

		if(Controller.fenetre.getVueAjout() != null)
		{
			if(arg0.getSource().getClass().equals(Controller.fenetre.getVueAjout().getJComboBox().getClass()))
			{
				String motChoisi = Controller.fenetre.getVueAjout().getJComboBox().getSelectedItem().toString();
				Mot monMotChoisi = new Mot(motChoisi.toLowerCase());
				ArrayList<Mot> listeFils = monMotChoisi.getFilsMot();
				listeFils = Mot.ArrayListEnOrdreAlphabetique(listeFils);
				if(listeFils.size() == 0)
				{
					Object[][] donneesTableauSynonymesGauche = {{"Aucun mot", "Aucun mot"},};
					String[] nomColonnesSynonyme = {"Entrée (Synonymes)", "Description"};
					Controller.fenetre.getVueAjout().setDonneesTableauSynonymesGauche(donneesTableauSynonymesGauche);
					Controller.fenetre.getVueAjout().getModelSynonymeGauche().setDataVector(donneesTableauSynonymesGauche, nomColonnesSynonyme);
					((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getModel()).removeRow(0);
					Controller.fenetre.getVueCourante().revalidate();
				}
				else
				{
					Object[][] donneesTableauSynonymesGauche = new Object[listeFils.size()][2];
					String[] nomColonnesSynonyme = {"Entrée (Synonymes)", "Description"};
					for (int i = 0; i < listeFils.size(); i++)
					{
						donneesTableauSynonymesGauche[i][0] = listeFils.get(i).libelleMot.toUpperCase();
						donneesTableauSynonymesGauche[i][1] = listeFils.get(i).definitionMot;
					}
					Controller.fenetre.getVueAjout().setDonneesTableauSynonymesGauche(donneesTableauSynonymesGauche);
					Controller.fenetre.getVueAjout().getModelSynonymeGauche().setDataVector(donneesTableauSynonymesGauche, nomColonnesSynonyme);
					Controller.fenetre.getVueCourante().revalidate();
				}

				ArrayList<Mot> listeMots = Mot.listeDesMots();
				listeMots = Mot.ArrayListEnOrdreAlphabetique(listeMots);
				if(listeMots.size() > 0)
				{
					Object[][] donneesTableauAssosGauche = new Object[listeMots.size()-1][2];
					int i = 0;
					int curseur = 0;
					while (i < listeMots.size())
					{
						if(!motChoisi.toLowerCase().equals(listeMots.get(i).libelleMot.toLowerCase()))
						{
							donneesTableauAssosGauche[curseur][0] = listeMots.get(i).libelleMot.toUpperCase();
							donneesTableauAssosGauche[curseur][1] = listeMots.get(i).definitionMot;
							curseur++;
						}
						i++;
					}
					String[] nomColonnesAssos = {"Entrée (Associations)", "Description"};
					Controller.fenetre.getVueAjout().setDonneesTableauAssosGauche(donneesTableauAssosGauche);
					Controller.fenetre.getVueAjout().getModelAssosGauche().setDataVector(donneesTableauAssosGauche, nomColonnesAssos);
					Controller.fenetre.getVueCourante().revalidate();
					Object[][] donneesTableauSynonymesDroite = {{"Aucun mot", "Aucun mot"},};
					String[] nomColonnesSynonyme = {"Entrée (Synonymes)", "Description"};
					Controller.fenetre.getVueAjout().setDonneesTableauSynonymesDroite(donneesTableauSynonymesDroite);
					Controller.fenetre.getVueAjout().getModelSynonymeDroite().setDataVector(donneesTableauSynonymesDroite, nomColonnesSynonyme);
					((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getModel()).removeRow(0);
					Controller.fenetre.getVueCourante().revalidate();
					Object[][] donneesTableauAssosDroite = {{"Aucun mot", "Aucun mot"},};
					Controller.fenetre.getVueAjout().setDonneesTableauAssosDroite(donneesTableauAssosDroite);
					Controller.fenetre.getVueAjout().getModelAssosDroite().setDataVector(donneesTableauAssosDroite, nomColonnesAssos);
					((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauAssosDroite().getModel()).removeRow(0);
					Controller.fenetre.getVueCourante().revalidate();
				}
			}
		}
		if(arg0.getActionCommand().equals("Ajouter l'Entrée"))
		{
			String nouveauMot = Controller.fenetre.getVueAjout().getTextFieldNouvelleEntree().getText();
			String pere = Controller.fenetre.getVueAjout().getJComboBox().getSelectedItem().toString();
			Object[][] synonymes = Controller.fenetre.getVueAjout().getDonneesTableauSynonymesDroite();
			Object[][] assos = Controller.fenetre.getVueAjout().getDonneesTableauAssosDroite();
			String description = Controller.fenetre.getVueAjout().getTextAreaDescription().getText();
			ajouterMot(nouveauMot, pere, assos, synonymes, description);
		}
	}

	public String[] recupererSousChaineEtMot(String s) {
		String stringSansEspaces = s.replaceAll("\\s", "");;
		String[] SousChaine = stringSansEspaces.split(",");

		return SousChaine;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		if(Controller.fenetre.getVueAjout() != null)
		{
			actionSurVueAjout();
		}
		if(Controller.fenetre.getVueArborescence() != null)
		{
			actionSurVueArbo(arg0);
		}
		if(Controller.fenetre.getVueMot() != null)
		{
			actionSurVueMot();
		}
	}
	
	public void actionSurVueMot()
	{
		if(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getSelectedRow() > -1
				&& Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getSelectedColumn() == 0)
		{
			int x = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getSelectedRow();
			int y = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getSelectedColumn();
			if (Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getValueAt(x, y) != null)
			{
				actionVueTableauAssosGauche(x, y);
			}
			Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().clearSelection();
			Controller.fenetre.getVueCourante().revalidate();
		}

		if(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getSelectedRow() > -1
				&& Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getSelectedColumn() == 0)
		{
			int x = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getSelectedRow();
			int y = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getSelectedColumn();
			if (Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getValueAt(x, y) != null)
			{
				actionVueTableauAssosDroite(x, y);

			}
			Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().clearSelection();
			Controller.fenetre.getVueCourante().revalidate();
		}
		else
		{
			if(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getSelectedRow() > -1
					&& Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getSelectedColumn() == 0)
			{
				int x = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getSelectedRow();
				int y = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getSelectedColumn();
				if (Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getValueAt(x, y) != null)
				{
					actionVueTableauSynonymeGauche(x, y);
				}
				Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().clearSelection();
				Controller.fenetre.getVueCourante().revalidate();
			}
			else
			{
				if(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getSelectedRow() > -1
						&& Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getSelectedColumn()== 0)
				{
					int x = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getSelectedRow();
					int y = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getSelectedColumn();
					if (Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getValueAt(x, y) != null)
					{
						actionVueTableauSynonymeDroite(x, y);
					}
					Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().clearSelection();
					Controller.fenetre.getVueCourante().revalidate();
				}
			}
		}
	}
	
	public void actionSurVueAjout()
	{
		if(Controller.fenetre.getVueAjout().getTableauAssosGauche().getSelectedRow() > -1
				&& Controller.fenetre.getVueAjout().getTableauAssosGauche().getSelectedColumn() == 0)
		{
			int x = Controller.fenetre.getVueAjout().getTableauAssosGauche().getSelectedRow();
			int y = Controller.fenetre.getVueAjout().getTableauAssosGauche().getSelectedColumn();
			if (Controller.fenetre.getVueAjout().getTableauAssosGauche().getValueAt(x, y) != null)
			{
				actionAjoutTableauAssosGauche(x, y);
			}
			Controller.fenetre.getVueAjout().getTableauAssosGauche().clearSelection();
			Controller.fenetre.getVueCourante().revalidate();
		}

		if(Controller.fenetre.getVueAjout().getTableauAssosDroite().getSelectedRow() > -1
				&& Controller.fenetre.getVueAjout().getTableauAssosDroite().getSelectedColumn() == 0)
		{
			int x = Controller.fenetre.getVueAjout().getTableauAssosDroite().getSelectedRow();
			int y = Controller.fenetre.getVueAjout().getTableauAssosDroite().getSelectedColumn();
			if (Controller.fenetre.getVueAjout().getTableauAssosDroite().getValueAt(x, y) != null)
			{
				actionAjoutTableauAssosDroite(x, y);

			}
			Controller.fenetre.getVueAjout().getTableauAssosGauche().clearSelection();
			Controller.fenetre.getVueCourante().revalidate();
		}
		else
		{
			if(Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getSelectedRow() > -1
					&& Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getSelectedColumn() == 0)
			{
				int x = Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getSelectedRow();
				int y = Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getSelectedColumn();
				if (Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getValueAt(x, y) != null)
				{
					actionAjoutTableauSynonymeGauche(x, y);
				}
				Controller.fenetre.getVueAjout().getTableauSynonymeGauche().clearSelection();
				Controller.fenetre.getVueCourante().revalidate();
			}
			else
			{
				if(Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getSelectedRow() > -1
						&& Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getSelectedColumn()== 0)
				{
					int x = Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getSelectedRow();
					int y = Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getSelectedColumn();
					if (Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getValueAt(x, y) != null)
					{
						actionAjoutTableauSynonymeDroite(x, y);
					}
					Controller.fenetre.getVueAjout().getTableauSynonymeDroite().clearSelection();
					Controller.fenetre.getVueCourante().revalidate();
				}
			}
		}
	}

	public void actionSurVueArbo(MouseEvent arg0)
	{
		if(arg0.getSource().equals(Controller.fenetre.getVueArborescence().getTableauArbo()))
		{
			int x = Controller.fenetre.getVueArborescence().getTableauArbo().getSelectedRow();
			int y = Controller.fenetre.getVueArborescence().getTableauArbo().getSelectedColumn();
			if (Controller.fenetre.getVueArborescence().getTableauArbo().getValueAt(x, y) != null)
			{
				Mot m = new Mot(Controller.fenetre.getVueArborescence().getTableauArbo().getValueAt(x, y).toString().toLowerCase());
				if(m != null)
				{
					afficherArborescenceMot(m);
				}
			}
			//Double click sur un noeud de l'arborescence
		} else if(arg0.getSource().equals(Controller.fenetre.getVueArborescence().getGraphicArbo())){
			int selRow = Controller.fenetre.getVueArborescence().getGraphicArbo().getRowForLocation(arg0.getX(), arg0.getY());
			TreePath selPath = Controller.fenetre.getVueArborescence().getGraphicArbo().getPathForLocation(arg0.getX(), arg0.getY());
			if(selRow != -1) {
				if(arg0.getClickCount() == 2) {
					afficherMot(new Mot(selPath.getLastPathComponent().toString()));
				}
			}
		}
	}
	
	public void actionAjoutTableauAssosGauche(int x, int y)
	{
		Object[][] donneesTableauAssosGaucheFUTUR;
		Object[][] donneesTableauAssosDroiteFUTUR;
		Object[][] listeAssosGauche = Controller.fenetre.getVueAjout().getDonneesTableauAssosGauche();
		Object[][] listeAssosDroite = Controller.fenetre.getVueAjout().getDonneesTableauAssosDroite();
		donneesTableauAssosDroiteFUTUR = new Object[listeAssosDroite.length+1][2];
		donneesTableauAssosGaucheFUTUR = new Object[listeAssosGauche.length-1][2];
		String[] nomColonnesAssos = {"Entrée (Associations)", "Description"};
		int rangMotaAjouter = 0;
		if(!Controller.fenetre.getVueAjout().getTableauAssosGauche().getValueAt(x, y).toString().equals("Aucun mot"))
		{
			int i = 0;
			int curseur = 0;
			while(i < listeAssosGauche.length)
			{
				if(!(listeAssosGauche[i][0].toString().toLowerCase().equals(Controller.fenetre.getVueAjout().getTableauAssosGauche().getValueAt(x, y).toString().toLowerCase())))
				{
					donneesTableauAssosGaucheFUTUR[curseur][0] = listeAssosGauche[i][0].toString().toUpperCase();
					donneesTableauAssosGaucheFUTUR[curseur][1] = listeAssosGauche[i][1];
					curseur ++;
				}
				else
				{
					rangMotaAjouter = i;
				}
				i++;
			}
		}
		for (int i = 0; i < listeAssosDroite.length; i++)
		{
			donneesTableauAssosDroiteFUTUR[i][0] = listeAssosDroite[i][0].toString().toUpperCase();
			donneesTableauAssosDroiteFUTUR[i][1] = listeAssosDroite[i][1];
		}
		donneesTableauAssosDroiteFUTUR[listeAssosDroite.length][0] = Controller.fenetre.getVueAjout().getTableauAssosGauche().getValueAt(x, y).toString().toUpperCase();
		donneesTableauAssosDroiteFUTUR[listeAssosDroite.length][1] = Controller.fenetre.getVueAjout().getTableauAssosGauche().getValueAt(x, 1);
		Controller.fenetre.getVueAjout().setDonneesTableauAssosDroite(donneesTableauAssosDroiteFUTUR);
		Controller.fenetre.getVueAjout().getModelAssosDroite().setDataVector(donneesTableauAssosDroiteFUTUR, nomColonnesAssos);
		((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauAssosDroite().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueAjout().setDonneesTableauAssosGauche(donneesTableauAssosGaucheFUTUR);
		Controller.fenetre.getVueAjout().getModelAssosGauche().setDataVector(donneesTableauAssosGaucheFUTUR, nomColonnesAssos);
		Controller.fenetre.getVueCourante().revalidate();
	}

	public void actionVueTableauAssosGauche(int x, int y)
	{
		Object[][] donneesTableauAssosGaucheFUTUR;
		Object[][] donneesTableauAssosDroiteFUTUR;
		Object[][] listeAssosGauche = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauAssosGauche();
		Object[][] listeAssosDroite = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauAssosDroite();
		donneesTableauAssosDroiteFUTUR = new Object[listeAssosDroite.length+1][2];
		donneesTableauAssosGaucheFUTUR = new Object[listeAssosGauche.length-1][2];
		String[] nomColonnesAssos = {"Entrée (Associations)", "Description"};
		int rangMotaAjouter = 0;
		if(!Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getValueAt(x, y).toString().equals("Aucun mot"))
		{
			int i = 0;
			int curseur = 0;
			while(i < listeAssosGauche.length)
			{
				if(!(listeAssosGauche[i][0].toString().toLowerCase().equals(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getValueAt(x, y).toString().toLowerCase())))
				{
					donneesTableauAssosGaucheFUTUR[curseur][0] = listeAssosGauche[i][0].toString().toUpperCase();
					donneesTableauAssosGaucheFUTUR[curseur][1] = listeAssosGauche[i][1];
					curseur ++;
				}
				else
				{
					rangMotaAjouter = i;
				}
				i++;
			}
		}
		for (int i = 0; i < listeAssosDroite.length; i++)
		{
			donneesTableauAssosDroiteFUTUR[i][0] = listeAssosDroite[i][0].toString().toUpperCase();
			donneesTableauAssosDroiteFUTUR[i][1] = listeAssosDroite[i][1];
		}
		donneesTableauAssosDroiteFUTUR[listeAssosDroite.length][0] = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getValueAt(x, y).toString().toUpperCase();
		donneesTableauAssosDroiteFUTUR[listeAssosDroite.length][1] = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosGauche().getValueAt(x, 1);
		Controller.fenetre.getVueMot().getVueTableauxConsult().setDonneesTableauAssosDroite(donneesTableauAssosDroiteFUTUR);
		Controller.fenetre.getVueMot().getVueTableauxConsult().getModelAssosDroite().setDataVector(donneesTableauAssosDroiteFUTUR, nomColonnesAssos);
		if(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getValueAt(0, 0).toString().equals("AUCUN MOT"))
		((DefaultTableModel) Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueMot().getVueTableauxConsult().setDonneesTableauAssosGauche(donneesTableauAssosGaucheFUTUR);
		Controller.fenetre.getVueMot().getVueTableauxConsult().getModelAssosGauche().setDataVector(donneesTableauAssosGaucheFUTUR, nomColonnesAssos);
		Controller.fenetre.getVueCourante().revalidate();
	}
	
	public void actionAjoutTableauAssosDroite(int x, int y)
	{
		Object[][] donneesTableauAssosGaucheFUTUR;
		Object[][] donneesTableauAssosDroiteFUTUR;
		Object[][] listeAssosGauche = Controller.fenetre.getVueAjout().getDonneesTableauAssosGauche();
		Object[][] listeAssosDroite = Controller.fenetre.getVueAjout().getDonneesTableauAssosDroite();
		donneesTableauAssosDroiteFUTUR = new Object[listeAssosDroite.length-1][2];
		donneesTableauAssosGaucheFUTUR = new Object[listeAssosGauche.length+1][2];
		String[] nomColonnesAssos = {"Entrée (Associations)", "Description"};
		int rangMotaAjouter = 0;
		if(!Controller.fenetre.getVueAjout().getTableauAssosDroite().getValueAt(x, y).toString().equals("Aucun mot"))
		{
			int i = 0;
			int curseur = 0;
			while(i < listeAssosDroite.length)
			{
				if(!(listeAssosDroite[i][0].toString().toLowerCase().equals(Controller.fenetre.getVueAjout().getTableauAssosDroite().getValueAt(x, y).toString().toLowerCase())))
				{
					donneesTableauAssosDroiteFUTUR[curseur][0] = listeAssosDroite[i][0].toString().toUpperCase();
					donneesTableauAssosDroiteFUTUR[curseur][1] = listeAssosDroite[i][1];
					curseur ++;
				}
				else
				{
					rangMotaAjouter = i;
				}
				i++;
			}
		}
		for (int i = 0; i < listeAssosGauche.length; i++)
		{
			donneesTableauAssosGaucheFUTUR[i][0] = listeAssosGauche[i][0].toString().toUpperCase();
			donneesTableauAssosGaucheFUTUR[i][1] = listeAssosGauche[i][1];
		}
		donneesTableauAssosGaucheFUTUR[listeAssosGauche.length][0] = Controller.fenetre.getVueAjout().getTableauAssosDroite().getValueAt(x, y).toString().toUpperCase();
		donneesTableauAssosGaucheFUTUR[listeAssosGauche.length][1] = Controller.fenetre.getVueAjout().getTableauAssosDroite().getValueAt(x, 1);
		Controller.fenetre.getVueAjout().setDonneesTableauAssosGauche(donneesTableauAssosGaucheFUTUR);
		Controller.fenetre.getVueAjout().getModelAssosGauche().setDataVector(donneesTableauAssosGaucheFUTUR, nomColonnesAssos);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueAjout().setDonneesTableauAssosDroite(donneesTableauAssosDroiteFUTUR);
		Controller.fenetre.getVueAjout().getModelAssosDroite().setDataVector(donneesTableauAssosDroiteFUTUR, nomColonnesAssos);
		((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauAssosDroite().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
	}

	public void actionVueTableauAssosDroite(int x, int y)
	{
		Object[][] donneesTableauAssosGaucheFUTUR;
		Object[][] donneesTableauAssosDroiteFUTUR;
		Object[][] listeAssosGauche = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauAssosGauche();
		Object[][] listeAssosDroite = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauAssosDroite();
		donneesTableauAssosDroiteFUTUR = new Object[listeAssosDroite.length-1][2];
		donneesTableauAssosGaucheFUTUR = new Object[listeAssosGauche.length+1][2];
		String[] nomColonnesAssos = {"Entrée (Associations)", "Description"};
		int rangMotaAjouter = 0;
		if(!Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getValueAt(x, y).toString().equals("Aucun mot"))
		{
			int i = 0;
			int curseur = 0;
			while(i < listeAssosDroite.length)
			{
				if(!(listeAssosDroite[i][0].toString().toLowerCase().equals(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getValueAt(x, y).toString().toLowerCase())))
				{
					donneesTableauAssosDroiteFUTUR[curseur][0] = listeAssosDroite[i][0].toString().toUpperCase();
					donneesTableauAssosDroiteFUTUR[curseur][1] = listeAssosDroite[i][1];
					curseur ++;
				}
				else
				{
					rangMotaAjouter = i;
				}
				i++;
			}
		}
		for (int i = 0; i < listeAssosGauche.length; i++)
		{
			donneesTableauAssosGaucheFUTUR[i][0] = listeAssosGauche[i][0].toString().toUpperCase();
			donneesTableauAssosGaucheFUTUR[i][1] = listeAssosGauche[i][1];
		}
		donneesTableauAssosGaucheFUTUR[listeAssosGauche.length][0] = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getValueAt(x, y).toString().toUpperCase();
		donneesTableauAssosGaucheFUTUR[listeAssosGauche.length][1] = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getValueAt(x, 1);
		Controller.fenetre.getVueMot().getVueTableauxConsult().setDonneesTableauAssosGauche(donneesTableauAssosGaucheFUTUR);
		Controller.fenetre.getVueMot().getVueTableauxConsult().getModelAssosGauche().setDataVector(donneesTableauAssosGaucheFUTUR, nomColonnesAssos);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueMot().getVueTableauxConsult().setDonneesTableauAssosDroite(donneesTableauAssosDroiteFUTUR);
		Controller.fenetre.getVueMot().getVueTableauxConsult().getModelAssosDroite().setDataVector(donneesTableauAssosDroiteFUTUR, nomColonnesAssos);
		if(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getValueAt(0, 0).toString().equals("AUCUN MOT"))
		((DefaultTableModel) Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauAssosDroite().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
	}
	
	public void actionAjoutTableauSynonymeGauche(int x, int y)
	{
		Object[][] donneesTableauSynonymeGaucheFUTUR;
		Object[][] donneesTableauSynonymeDroiteFUTUR;
		Object[][] listeSynonymeGauche = Controller.fenetre.getVueAjout().getDonneesTableauSynonymesGauche();
		Object[][] listeSynonymeDroite = Controller.fenetre.getVueAjout().getDonneesTableauSynonymesDroite();
		donneesTableauSynonymeDroiteFUTUR = new Object[listeSynonymeDroite.length+1][2];
		donneesTableauSynonymeGaucheFUTUR = new Object[listeSynonymeGauche.length-1][2];
		String[] nomColonnesSynonyme = {"Entrée (Synonymes)", "Description"};
		int rangMotaAjouter = 0;
		if(!Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getValueAt(x, y).toString().equals("Aucun mot"))
		{
			int i = 0;
			int curseur = 0;
			while(i < listeSynonymeGauche.length)
			{
				if(!(listeSynonymeGauche[i][0].toString().toLowerCase().equals(Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getValueAt(x, y).toString().toLowerCase())))
				{
					donneesTableauSynonymeGaucheFUTUR[curseur][0] = listeSynonymeGauche[i][0].toString().toUpperCase();
					donneesTableauSynonymeGaucheFUTUR[curseur][1] = listeSynonymeGauche[i][1];
					curseur ++;
				}
				else
				{
					rangMotaAjouter = i;
				}
				i++;
			}
		}
		for (int i = 0; i < listeSynonymeDroite.length; i++)
		{
			donneesTableauSynonymeDroiteFUTUR[i][0] = listeSynonymeDroite[i][0].toString().toUpperCase();
			donneesTableauSynonymeDroiteFUTUR[i][1] = listeSynonymeDroite[i][1];
		}
		donneesTableauSynonymeDroiteFUTUR[listeSynonymeDroite.length][0] = Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getValueAt(x, y).toString().toUpperCase();
		donneesTableauSynonymeDroiteFUTUR[listeSynonymeDroite.length][1] = Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getValueAt(x, 1);
		Controller.fenetre.getVueAjout().setDonneesTableauSynonymesDroite(donneesTableauSynonymeDroiteFUTUR);
		Controller.fenetre.getVueAjout().getModelSynonymeDroite().setDataVector(donneesTableauSynonymeDroiteFUTUR, nomColonnesSynonyme);
		((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueAjout().setDonneesTableauSynonymesGauche(donneesTableauSynonymeGaucheFUTUR);
		Controller.fenetre.getVueAjout().getModelSynonymeGauche().setDataVector(donneesTableauSynonymeGaucheFUTUR, nomColonnesSynonyme);
		Controller.fenetre.getVueCourante().revalidate();
	}

	public void actionVueTableauSynonymeGauche(int x, int y)
	{
		Object[][] donneesTableauSynonymeGaucheFUTUR;
		Object[][] donneesTableauSynonymeDroiteFUTUR;
		Object[][] listeSynonymeGauche = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauSynonymesGauche();
		Object[][] listeSynonymeDroite = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauSynonymesDroite();
		donneesTableauSynonymeDroiteFUTUR = new Object[listeSynonymeDroite.length+1][2];
		donneesTableauSynonymeGaucheFUTUR = new Object[listeSynonymeGauche.length-1][2];
		String[] nomColonnesSynonyme = {"Entrée (Synonymes)", "Description"};
		int rangMotaAjouter = 0;
		if(!Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getValueAt(x, y).toString().equals("Aucun mot"))
		{
			int i = 0;
			int curseur = 0;
			while(i < listeSynonymeGauche.length)
			{
				if(!(listeSynonymeGauche[i][0].toString().toLowerCase().equals(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getValueAt(x, y).toString().toLowerCase())))
				{
					donneesTableauSynonymeGaucheFUTUR[curseur][0] = listeSynonymeGauche[i][0].toString().toUpperCase();
					donneesTableauSynonymeGaucheFUTUR[curseur][1] = listeSynonymeGauche[i][1];
					curseur ++;
				}
				else
				{
					rangMotaAjouter = i;
				}
				i++;
			}
		}
		for (int i = 0; i < listeSynonymeDroite.length; i++)
		{
			donneesTableauSynonymeDroiteFUTUR[i][0] = listeSynonymeDroite[i][0].toString().toUpperCase();
			donneesTableauSynonymeDroiteFUTUR[i][1] = listeSynonymeDroite[i][1];
		}
		donneesTableauSynonymeDroiteFUTUR[listeSynonymeDroite.length][0] = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getValueAt(x, y).toString().toUpperCase();
		donneesTableauSynonymeDroiteFUTUR[listeSynonymeDroite.length][1] = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeGauche().getValueAt(x, 1);
		Controller.fenetre.getVueMot().getVueTableauxConsult().setDonneesTableauSynonymesDroite(donneesTableauSynonymeDroiteFUTUR);
		Controller.fenetre.getVueMot().getVueTableauxConsult().getModelSynonymeDroite().setDataVector(donneesTableauSynonymeDroiteFUTUR, nomColonnesSynonyme);
		if(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getValueAt(0, 0).toString().equals("AUCUN MOT"))
		((DefaultTableModel) Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueMot().getVueTableauxConsult().setDonneesTableauSynonymesGauche(donneesTableauSynonymeGaucheFUTUR);
		Controller.fenetre.getVueMot().getVueTableauxConsult().getModelSynonymeGauche().setDataVector(donneesTableauSynonymeGaucheFUTUR, nomColonnesSynonyme);
		Controller.fenetre.getVueCourante().revalidate();
	}
	
	public void actionAjoutTableauSynonymeDroite(int x, int y)
	{
		Object[][] donneesTableauSynonymeDroiteFUTUR;
		Object[][] donneesTableauSynonymeGaucheFUTUR;
		Object[][] listeSynonymeDroite = Controller.fenetre.getVueAjout().getDonneesTableauSynonymesDroite();
		Object[][] listeSynonymeGauche = Controller.fenetre.getVueAjout().getDonneesTableauSynonymesGauche();
		donneesTableauSynonymeGaucheFUTUR = new Object[listeSynonymeGauche.length+1][2];
		donneesTableauSynonymeDroiteFUTUR = new Object[listeSynonymeDroite.length-1][2];
		String[] nomColonnesSynonyme = {"Entrée (Synonymes)", "Description"};
		int rangMotaAjouter = 0;
		if(!Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getValueAt(x, y).toString().equals("Aucun mot"))
		{
			int i = 0;
			int curseur = 0;
			while(i < listeSynonymeDroite.length)
			{
				if(!(listeSynonymeDroite[i][0].toString().toLowerCase().equals(Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getValueAt(x, y).toString().toLowerCase())))
				{
					donneesTableauSynonymeDroiteFUTUR[curseur][0] = listeSynonymeDroite[i][0].toString().toUpperCase();
					donneesTableauSynonymeDroiteFUTUR[curseur][1] = listeSynonymeDroite[i][1];
					curseur ++;
				}
				else
				{
					rangMotaAjouter = i;
				}
				i++;
			}
		}
		for (int i = 0; i < listeSynonymeGauche.length; i++)
		{
			donneesTableauSynonymeGaucheFUTUR[i][0] = listeSynonymeGauche[i][0].toString().toUpperCase();
			donneesTableauSynonymeGaucheFUTUR[i][1] = listeSynonymeGauche[i][1];
		}
		donneesTableauSynonymeGaucheFUTUR[listeSynonymeGauche.length][0] = Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getValueAt(x, y).toString().toUpperCase();
		donneesTableauSynonymeGaucheFUTUR[listeSynonymeGauche.length][1] = Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getValueAt(x, 1);
		Controller.fenetre.getVueAjout().setDonneesTableauSynonymesGauche(donneesTableauSynonymeGaucheFUTUR);
		Controller.fenetre.getVueAjout().getModelSynonymeGauche().setDataVector(donneesTableauSynonymeGaucheFUTUR, nomColonnesSynonyme);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueAjout().setDonneesTableauSynonymesDroite(donneesTableauSynonymeDroiteFUTUR);
		Controller.fenetre.getVueAjout().getModelSynonymeDroite().setDataVector(donneesTableauSynonymeDroiteFUTUR, nomColonnesSynonyme);
		((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
	}
	
	public void actionVueTableauSynonymeDroite(int x, int y)
	{
		Object[][] donneesTableauSynonymeDroiteFUTUR;
		Object[][] donneesTableauSynonymeGaucheFUTUR;
		Object[][] listeSynonymeDroite = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauSynonymesDroite();
		Object[][] listeSynonymeGauche = Controller.fenetre.getVueMot().getVueTableauxConsult().getDonneesTableauSynonymesGauche();
		donneesTableauSynonymeGaucheFUTUR = new Object[listeSynonymeGauche.length+1][2];
		donneesTableauSynonymeDroiteFUTUR = new Object[listeSynonymeDroite.length-1][2];
		String[] nomColonnesSynonyme = {"Entrée (Synonymes)", "Description"};
		int rangMotaAjouter = 0;
		if(!Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getValueAt(x, y).toString().equals("Aucun mot"))
		{
			int i = 0;
			int curseur = 0;
			while(i < listeSynonymeDroite.length)
			{
				if(!(listeSynonymeDroite[i][0].toString().toLowerCase().equals(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getValueAt(x, y).toString().toLowerCase())))
				{
					donneesTableauSynonymeDroiteFUTUR[curseur][0] = listeSynonymeDroite[i][0].toString().toUpperCase();
					donneesTableauSynonymeDroiteFUTUR[curseur][1] = listeSynonymeDroite[i][1];
					curseur ++;
				}
				else
				{
					rangMotaAjouter = i;
				}
				i++;
			}
		}
		for (int i = 0; i < listeSynonymeGauche.length; i++)
		{
			donneesTableauSynonymeGaucheFUTUR[i][0] = listeSynonymeGauche[i][0].toString().toUpperCase();
			donneesTableauSynonymeGaucheFUTUR[i][1] = listeSynonymeGauche[i][1];
		}
		donneesTableauSynonymeGaucheFUTUR[listeSynonymeGauche.length][0] = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getValueAt(x, y).toString().toUpperCase();
		donneesTableauSynonymeGaucheFUTUR[listeSynonymeGauche.length][1] = Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getValueAt(x, 1);
		Controller.fenetre.getVueMot().getVueTableauxConsult().setDonneesTableauSynonymesGauche(donneesTableauSynonymeGaucheFUTUR);
		Controller.fenetre.getVueMot().getVueTableauxConsult().getModelSynonymeGauche().setDataVector(donneesTableauSynonymeGaucheFUTUR, nomColonnesSynonyme);
		//((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueMot().getVueTableauxConsult().setDonneesTableauSynonymesDroite(donneesTableauSynonymeDroiteFUTUR);
		Controller.fenetre.getVueMot().getVueTableauxConsult().getModelSynonymeDroite().setDataVector(donneesTableauSynonymeDroiteFUTUR, nomColonnesSynonyme);
		if(Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getValueAt(0, 0).toString().equals("AUCUN MOT"))
		((DefaultTableModel) Controller.fenetre.getVueMot().getVueTableauxConsult().getTableauSynonymeDroite().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void insertUpdateNouvelleEntree(DocumentEvent e, JTextField textFieldNouvelleEntree) {
		boolean verifyClassName = Controller.fenetre.getVueAjout().getTextFieldNouvelleEntree().getInputVerifier().verify(Controller.fenetre.getVueAjout().getTextFieldNouvelleEntree());
	}
}
