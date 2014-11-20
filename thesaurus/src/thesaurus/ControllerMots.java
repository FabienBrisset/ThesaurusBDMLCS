package thesaurus;

import java.awt.event.ActionEvent;
import java.sql.Ref;
import java.util.TreeSet;

import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ControllerMots extends Controller {
	
	private Mot motCourant;
	
	public ControllerMots() {
		this.motCourant = null;
	}
	
	public ControllerMots(Mot m) {
		this.motCourant = m;
	}
	
	public void afficherMot(Mot m) {
		this.motCourant = m;
		VueMot vue = new VueMot(m);
		vue.afficher();
	}
	
	public void afficherArborescenceMot(Mot m) {
		this.motCourant = m;
		TreeNode<Mot> racine = new TreeNode<Mot>(m);
		TreeNode<Mot> arbre = m.getArborescence();
		System.out.println(arbre.children.get(3).children.get(0).data);
		VueArborescenceMot vue = new VueArborescenceMot(arbre);
		vue.afficher();
	}
	
	public void afficherAjout() {
		VueAjoutMot vue = new VueAjoutMot();
		vue.afficher();
		//fenetre.setView(vue);
	}
	
	
	public void ajouterMot(Mot nouveauMot) {
		nouveauMot.insert();
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
	
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0);
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
			this.afficherMot(new Mot("table"));
			break;
		case 2 :
			this.afficherAjout();
			break;
		case 3 :
			this.afficherArborescenceMot(new Mot("table"));
			break;
		};

	}
}
