package thesaurus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ControllerMots extends Controller implements ActionListener {
	
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
		ArrayList<Mot> listeMots = Mot.getLibelleDeTousLesMots();
		VueAjoutMot vue = new VueAjoutMot(listeMots);
		vue.afficher();
		Controller.fenetre.setVueAjout(vue);
	}
	
	
	public void ajouterMot(String mot, String pere, String synonyme, String description) {
		try {
			if (Mot.libelleMotExisteDeja(mot)) {
				this.afficherAjout();
			}
			else {
				String[] lesSynonymesEnString = recupererSousChaineEtMot(synonyme);
				String[] lesSynonymesEnStringEnMinuscules = new String[lesSynonymesEnString.length];
				Mot[] lesSynonymesEnMot = new Mot[lesSynonymesEnString.length];
				Ref[] lesSynonymesEnRef = new Ref[lesSynonymesEnMot.length];
				ArrayList<Ref> lesSynonymesEnRefArrayList = new ArrayList<Ref>(lesSynonymesEnRef.length);
				
				for (int i = 0; i < lesSynonymesEnString.length; i++) {
					lesSynonymesEnStringEnMinuscules[i] = lesSynonymesEnString[i].toLowerCase();
					Mot motTeste = null;
					try {
						motTeste = Mot.getMotByLibelle(lesSynonymesEnStringEnMinuscules[i]);
						if (motTeste != null)
							lesSynonymesEnMot[i] = motTeste;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (lesSynonymesEnMot[i] != null)
						lesSynonymesEnRef[i] = lesSynonymesEnMot[i].getRef();
				}
				
				Mot motPere = null;
				try {
					motPere = this.motCourant.getMotByLibelle(pere.toLowerCase());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (int i = 0; i < lesSynonymesEnRef.length; i++) {
					if (lesSynonymesEnRef[i] != null)
						lesSynonymesEnRefArrayList.add(lesSynonymesEnRef[i]);
				}
				
				Mot m = new Mot(mot.toLowerCase(), description, motPere.getRef(), null, lesSynonymesEnRefArrayList, null);
				
				m.insert();
				this.afficherMot(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void modifierMot(Mot m) { 
		Ref refAncienMot = m.getRef();
		Mot ancienMot = Mot.getMotByRef(refAncienMot);
		m.update(ancienMot);
	}
	
	public void supprimerMot(Mot m) {
		Mot pere = m.getPere();
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
	public void actionPerformed(ActionEvent arg0) {
		String nouveauMot = Controller.fenetre.getVueAjout().getTextFieldNouvelleEntree().getText();
		String pere = Controller.fenetre.getVueAjout().getJComboBox().getSelectedItem().toString();
		String synonymeEnString = Controller.fenetre.getVueAjout().getTextAreaSynonyme().getText();
		String description = Controller.fenetre.getVueAjout().getTextAreaDescription().getText();
		this.ajouterMot(nouveauMot, pere, synonymeEnString, description);
	}
	
	public String[] recupererSousChaineEtMot(String s) {
		String stringSansEspaces = s.replaceAll("\\s", "");;
		String[] SousChaine = stringSansEspaces.split(",");

		return SousChaine;
	}
}
