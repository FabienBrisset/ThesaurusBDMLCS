package thesaurus;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public class VueArborescenceMot {

	private TreeNode<Mot> arborescence;
	JTree graphicArbo;
	JSplitPane splitPanArbo;
	JScrollPane scrollPanArbo;
	JScrollPane scrollPanTableau;
	DefaultTableModel modelTableauArbo;
	private Object[][] donneesTableauArbo = {{"Aucun mot", "Aucun mot"},};
	private String[] nomColonnesArbo = {"Entrée", "Description"};
	JTable tableauArbo;
	
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
		
		ArrayList<Mot> listeMots = Mot.ArrayListEnOrdreAlphabetique(Mot.listeDesMots());
		scrollPanArbo = new JScrollPane();
		scrollPanTableau = new JScrollPane();
		splitPanArbo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollPanTableau,scrollPanArbo);
		splitPanArbo.setDividerLocation(300);
		if(listeMots.size() == 0)
		{
			modelTableauArbo = new DefaultTableModel(donneesTableauArbo,nomColonnesArbo);
			tableauArbo = new JTable(modelTableauArbo);
		}
		else
		{
			donneesTableauArbo = new Object[listeMots.size()][2];
			for (int i = 0; i < listeMots.size(); i++)
			{
				donneesTableauArbo[i][0] = listeMots.get(i).libelleMot.toUpperCase();
				donneesTableauArbo[i][1] = listeMots.get(i).definitionMot;
			}
			for(int j=0; j<donneesTableauArbo.length;j++)
			 {
			     for (int i=j+1 ; i<donneesTableauArbo.length; i++)
			     {
			         if(donneesTableauArbo[i][0].toString().compareTo(donneesTableauArbo[j][0].toString())<0)
			         {
			             String temp= donneesTableauArbo[j][0].toString();
			             donneesTableauArbo[j][0]= donneesTableauArbo[i][0]; 
			             donneesTableauArbo[i][0]=temp;
			         }
			     }
			 }
			modelTableauArbo = new DefaultTableModel(donneesTableauArbo,nomColonnesArbo);
			tableauArbo = new JTable(modelTableauArbo);
			tableauArbo.setDefaultRenderer(Object.class, new RenduCellule());
			tableauArbo.addMouseListener(Controller.getControllerMots());
		}
		
		graphicArbo.addMouseListener(Controller.getControllerMots());
	}
	
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String imageName) {
        return new ImageIcon(System.getProperty("user.dir") + "/images/" + imageName);
    }
	
	public void afficher() {
		Controller.fenetre.getVueCourante().panArbo.removeAll();
		
		scrollPanArbo.setViewportView(graphicArbo);
		scrollPanTableau.setViewportView(tableauArbo);
		Controller.fenetre.getVueCourante().panArbo.setLayout(new BoxLayout(Controller.fenetre.getVueCourante().panArbo, BoxLayout.X_AXIS));
		Controller.fenetre.getVueCourante().panArbo.add(splitPanArbo);
		
		ImageIcon imageIcon = new ImageIcon("fouet.png");
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(imageIcon);
		
		Controller.fenetre.getVueCourante().panArbo.revalidate();
	}

	public JTable getTableauArbo() {
		return tableauArbo;
	}

	public void setTableauArbo(JTable tableauArbo) {
		this.tableauArbo = tableauArbo;
	}

	public JTree getGraphicArbo() {
		return graphicArbo;
	}
	
	
}
