package thesaurus;

import java.awt.event.ActionEvent;
import java.util.TreeSet;

import javax.swing.JTree;
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
		
	}
	
	public void afficherArborescenceMot(Mot m) {
		this.motCourant = m;
		TreeNode<Mot> racine = new TreeNode<Mot>(m);
		TreeNode<Mot> arbre = m.getArborescence(racine, racine);
		VueArborescenceMot vue = new VueArborescenceMot(arbre);
	}
	
	public void afficherAjout() {
		
	}
	
	public void afficherModificationMot() {
		
	}
	
	public void ajouterMot(Mot nouveauMot) {
		
	}
	public void modifierMot(Mot m) {
		
	}
	
	public void supprimerMot() {
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}
	
	public Mot getMotCourant() {
		return this.motCourant;
	}
}
