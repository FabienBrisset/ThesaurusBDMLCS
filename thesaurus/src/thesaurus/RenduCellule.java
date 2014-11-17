package thesaurus;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenduCellule extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) 
	{
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if(column == 0 && (row % 2 == 0))
		{
			cell.setBackground(Color.LIGHT_GRAY);
		}
		else if (column == 0 && (row % 2 == 1))
		{
			cell.setBackground(Color.GRAY);

		}
		else
		{
			cell.setBackground(Color.WHITE);

		}
		return cell;
	}
}
