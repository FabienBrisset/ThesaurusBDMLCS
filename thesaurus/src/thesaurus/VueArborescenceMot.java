package thesaurus;

import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class VueArborescenceMot {

	private TreeNode<Mot> arborescence;
	JTree graphicArbo;
	
	public VueArborescenceMot(TreeNode<Mot> arborescence){
		this.arborescence = arborescence;
		graphicArbo = arborescence.toJTree();
		
		ImageIcon leafIcon = createImageIcon("fouet.png");
		ImageIcon openIcon = createImageIcon("spatule.png");
		if (leafIcon != null) {
		    DefaultTreeCellRenderer renderer = 
		        new DefaultTreeCellRenderer();
		    renderer.setLeafIcon(leafIcon);
		    renderer.setOpenIcon(openIcon);
		    renderer.setClosedIcon(openIcon);
		    graphicArbo.setCellRenderer(renderer);
		}
		
		for (int i = 0; i < graphicArbo.getRowCount(); i++) {
			graphicArbo.expandRow(i);
		}
	}
	
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String imageName) {
        return new ImageIcon(System.getProperty("user.dir") + "/images/" + imageName);
    }
	
	public void afficher() {
		Controller.fenetre.getVueCourante().panArbo.removeAll();
		
		Controller.fenetre.getVueCourante().panArbo.add(graphicArbo);
		
		ImageIcon imageIcon = new ImageIcon("fouet.png");
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(imageIcon);
		
		Controller.fenetre.getVueCourante().panArbo.revalidate();
	}
}
