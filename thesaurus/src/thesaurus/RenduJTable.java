package thesaurus;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RenduJTable extends JTable 
{
	public RenduJTable(Object[][] donneesTableau, String[] nomColonnes) 
	{
		super(donneesTableau,nomColonnes);
		// TODO Auto-generated constructor stub
	}
	
    public RenduJTable(DefaultTableModel modelAssosGauche) {
		// TODO Auto-generated constructor stub
    	super(modelAssosGauche);
	}

	@Override
    public boolean isCellEditable(int row, int column) {
       //all cells false
       return false;
    }

}
