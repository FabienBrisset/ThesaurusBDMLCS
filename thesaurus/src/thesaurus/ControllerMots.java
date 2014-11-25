package thesaurus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ControllerMots extends Controller implements ActionListener, MouseListener {

	private Mot motCourant;

	public ControllerMots() {
		this.motCourant = new Mot("table");
	}

	public ControllerMots(Mot m) {
		this.motCourant = m;
	}

	public void afficherMot(Mot m) {
		this.motCourant = m;
		VueMot vue = new VueMot(m);
		vue.afficher();
		Controller.fenetre.setVueMot(vue);
		Controller.fenetre.getVueCourante().panConsulter = null;
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


	public void ajouterMot(String mot, String pere, String synonyme, String description) {
		try {
			Mot m = new Mot(null,mot,"",null,null,null,null);
			if (m.existe()) {
				this.afficherAjout();
			}
			else {
				String[] lesSynonymesEnString = recupererSousChaineEtMot(synonyme);
				String[] lesSynonymesEnStringEnMinuscules = new String[lesSynonymesEnString.length];
				Mot[] lesSynonymesEnMot = new Mot[lesSynonymesEnString.length];
				Ref[] lesSynonymesEnRef = new Ref[lesSynonymesEnMot.length];
				ArrayList<Ref> lesSynonymesEnRefArrayList = new ArrayList<Ref>(lesSynonymesEnRef.length);
				ArrayList<Mot> lesSynonymesMotArrayList = new ArrayList<Mot>(lesSynonymesEnMot.length);


				for (int i = 0; i < lesSynonymesEnString.length; i++) {
					lesSynonymesEnStringEnMinuscules[i] = lesSynonymesEnString[i].toLowerCase();
					Mot motTeste = null;
					//try {
					motTeste = new Mot(lesSynonymesEnStringEnMinuscules[i]);
					if (motTeste != null)
						lesSynonymesEnMot[i] = motTeste;
					//					} catch (SQLException e) {
					//						// TODO Auto-generated catch block
					//						e.printStackTrace();
					//					}
					if (lesSynonymesEnMot[i] != null)
						lesSynonymesEnRef[i] = lesSynonymesEnMot[i].getRef();
				}

				Mot motPere = null;
				//try {
				//motPere = this.motCourant.getMotByLibelle(pere.toLowerCase());
				motPere = new Mot(pere.toLowerCase());
				//				} catch (SQLException e) {
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				}

				for (int i = 0; i < lesSynonymesEnRef.length; i++) {
					if (lesSynonymesEnRef[i] != null)
						lesSynonymesEnRefArrayList.add(lesSynonymesEnRef[i]);
				}

				for (int i = 0; i < lesSynonymesEnMot.length; i++) {
					if (lesSynonymesEnMot[i] != null)
						lesSynonymesMotArrayList.add(lesSynonymesEnMot[i]);
				}

				Mot m2 = new Mot(null,mot.toLowerCase(), description, motPere, new ArrayList<Mot>(), lesSynonymesMotArrayList, null);

				m2.insert();
				this.afficherMot(m2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ajouterMot(String mot, String pere, Object[][] assos, Object[][] synonymes, String description)
	{
		ArrayList<Mot> listeSynonymes = new ArrayList<Mot>();
		ArrayList<Mot> listeAssos = new ArrayList<Mot>();
		Mot motPere = null;
		try {
			Mot m = new Mot(null,mot,"",null,null,null,null);
			if (m.existe()) {
				this.afficherAjout();
			}
			else 
			{
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
				m2.insert();
				this.afficherMot(m2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void modifierMot(Mot m) { 
		//Ref refAncienMot = m.getRef();
		Mot ancienMot = new Mot(m.getLibelleMot());
		m.update(ancienMot);
	}

	public void supprimerMot(Mot m) {
		Mot pere = new Mot(m.getPereMot().getLibelleMot());
		m.delete();
		this.afficherMot(pere);
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
			this.modifierMot(motCourant);
			this.afficherMot(motCourant);
			JOptionPane.showMessageDialog(fenetre, "Modification Effectuée");
		}
		if(arg0.getActionCommand().equals("Supprimer l'entrée")){
			this.supprimerMot(motCourant);
			this.afficherMot(motCourant);
			JOptionPane.showMessageDialog(fenetre, "Mot Supprimé");

		}

		if(arg0.getSource().getClass().equals(Controller.fenetre.getVueAjout().getJComboBox().getClass()))
		{
			String motChoisi = Controller.fenetre.getVueAjout().getJComboBox().getSelectedItem().toString();
			//			try 
			//			{
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
			}
			//			} 
			//			catch (SQLException e) 
			//			{
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
		}
		else if(arg0.getActionCommand().equals("Ajouter l'Entrée"))
		{
			String nouveauMot = Controller.fenetre.getVueAjout().getTextFieldNouvelleEntree().getText();
			String pere = Controller.fenetre.getVueAjout().getJComboBox().getSelectedItem().toString();
			Object[][] synonymes = Controller.fenetre.getVueAjout().getDonneesTableauSynonymesDroite();
			Object[][] assos = Controller.fenetre.getVueAjout().getDonneesTableauAssosDroite();
			String description = Controller.fenetre.getVueAjout().getTextAreaDescription().getText();
			ajouterMot(nouveauMot, pere, assos, synonymes, description);
		}

		//this.ajouterMot(nouveauMot, pere, synonymeEnString, description);
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
			if(Controller.fenetre.getVueAjout().getTableauAssosGauche().getSelectedRow() > -1
					&& Controller.fenetre.getVueAjout().getTableauAssosGauche().getSelectedColumn() == 0)
			{
				int x = Controller.fenetre.getVueAjout().getTableauAssosGauche().getSelectedRow();
				int y = Controller.fenetre.getVueAjout().getTableauAssosGauche().getSelectedColumn();
				if (Controller.fenetre.getVueAjout().getTableauAssosGauche().getValueAt(x, y) != null)
				{
					ActionTableauAssosGauche(x, y);
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
					ActionTableauAssosDroite(x, y);

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
						ActionTableauSynonymeGauche(x, y);
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
							ActionTableauSynonymeDroite(x, y);
						}
						Controller.fenetre.getVueAjout().getTableauSynonymeDroite().clearSelection();
						Controller.fenetre.getVueCourante().revalidate();
					}
				}
			}
		}
		if(Controller.fenetre.getVueArborescence() != null)
		{
			if(arg0.getSource().equals(Controller.fenetre.getVueArborescence().getTableauArbo()))
			{
				System.out.println("dedans");
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
			}
		}
	}

	public void ActionTableauAssosGauche(int x, int y)
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

	public void ActionTableauAssosDroite(int x, int y)
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

	public void ActionTableauSynonymeGauche(int x, int y)
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

	public void ActionTableauSynonymeDroite(int x, int y)
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
		//((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauSynonymeGauche().getModel()).removeRow(0);
		Controller.fenetre.getVueCourante().revalidate();
		Controller.fenetre.getVueAjout().setDonneesTableauSynonymesDroite(donneesTableauSynonymeDroiteFUTUR);
		Controller.fenetre.getVueAjout().getModelSynonymeDroite().setDataVector(donneesTableauSynonymeDroiteFUTUR, nomColonnesSynonyme);
		((DefaultTableModel) Controller.fenetre.getVueAjout().getTableauSynonymeDroite().getModel()).removeRow(0);
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
}
