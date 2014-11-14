package thesaurus;

import javax.swing.JTree;

public class VueArborescenceMot extends View {

	private TreeNode<Mot> arborescence;
	JTree graphicArbo;
	
	public VueArborescenceMot(TreeNode<Mot> arborescence){
		graphicArbo = arborescence.toJTree();
	}
	
	public void afficher() {
		
	}
}
