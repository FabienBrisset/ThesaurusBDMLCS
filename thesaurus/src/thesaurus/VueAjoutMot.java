package thesaurus;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;

public class VueAjoutMot {
	
	JTextField textField_1;
	JTabbedPane tabbedPane;
	
	JPanel tab_ajouter;
	JPanel panel;
	JPanel panel_1;
	JPanel panel_2;
	JPanel panel_4;
	JPanel panel_5;
	JPanel panel_6;
	JPanel panel_7;

	JLabel lblNewLabel_8;
	JLabel lblNewLabel_9;
	JLabel lblNewLabel_10;
	JLabel desc;
	
	String[] items = {"Exemple", "Exemple"};
	JComboBox comboBox;
	JScrollPane scrollPane;
	JScrollPane scrollPane_1;
	JTextArea textArea;
	JTextArea textArea_1;
	JButton btnNewButton;
	
	public VueAjoutMot() {
		tabbedPane = new JTabbedPane();
		tab_ajouter = new JPanel();
		tabbedPane.addTab("Ajouter Entr\u00E9e", null, tab_ajouter, null);
		tab_ajouter.setLayout(new BoxLayout(tab_ajouter, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		panel.setMaximumSize(new Dimension(32767, 200));
		tab_ajouter.add(panel);
		
		lblNewLabel_8 = new JLabel("Intitul\u00E9 de la nouvelle entr\u00E9e :");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(32767, 200));
		tab_ajouter.add(panel_1);
		
		lblNewLabel_9 = new JLabel("Parent (doit exister) :");
		
		comboBox = new JComboBox(items);
		
		panel_2 = new JPanel();
		tab_ajouter.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		panel_7 = new JPanel();
		panel_7.setMaximumSize(new Dimension(32767, 100));
		
		lblNewLabel_10 = new JLabel("Synonyme(s) :");
		
		scrollPane_1 = new JScrollPane();
		tab_ajouter.add(scrollPane_1);
		
		textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		
		panel_5 = new JPanel();
		tab_ajouter.add(panel_5);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.Y_AXIS));
		
		panel_4 = new JPanel();
		panel_4.setMaximumSize(new Dimension(32767, 100));
		
		desc = new JLabel("Description :");
		
		scrollPane = new JScrollPane();

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		panel_6 = new JPanel();
		panel_6.setMaximumSize(new Dimension(32767, 200));
		tab_ajouter.add(panel_6);
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnNewButton = new JButton("Ajouter l'Entr\u00E9e");
		btnNewButton.setForeground(Color.BLACK);
	}

	public void afficher() {
		Controller.fenetre.getVueCourante().panAjouter.removeAll();
		System.out.println(Controller.fenetre.getVueCourante().panAjouter);
		Controller.fenetre.getVueCourante().panAjouter.setLayout(new BoxLayout(Controller.fenetre.getVueCourante().panAjouter, BoxLayout.Y_AXIS));
		
		Controller.fenetre.getVueCourante().panAjouter.add(tabbedPane);
		
		panel.add(lblNewLabel_8);
		panel_1.add(lblNewLabel_9);
		panel.add(textField_1);
		panel_1.add(comboBox);
		Controller.fenetre.getVueCourante().panAjouter.add(panel_1);
		
		panel_7.add(lblNewLabel_10);
		
		panel_2.add(panel_7);
		Controller.fenetre.getVueCourante().panAjouter.add(panel_7);
		
		panel_4.add(desc);
		
		panel_5.add(panel_4);
		panel_5.add(scrollPane);
		Controller.fenetre.getVueCourante().panAjouter.add(panel_5);
		
		panel_6.add(btnNewButton);
		Controller.fenetre.getVueCourante().panAjouter.add(panel_6);
	}
}
