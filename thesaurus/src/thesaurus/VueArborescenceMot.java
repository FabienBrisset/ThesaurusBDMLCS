package thesaurus;

import javax.swing.BoxLayout;
import javax.swing.JTree;

public class VueArborescenceMot {

	private TreeNode<Mot> arborescence;
	JTree graphicArbo;
	
	public VueArborescenceMot(TreeNode<Mot> arborescence){
		this.arborescence = arborescence;
		graphicArbo = arborescence.toJTree();
	}
	
	public void afficher() {
		Controller.fenetre.getVueCourante().panArbo.removeAll();
		
		Controller.fenetre.getVueCourante().panArbo.add(graphicArbo);
		
		Controller.fenetre.getVueCourante().panArbo.revalidate();
	}
}
