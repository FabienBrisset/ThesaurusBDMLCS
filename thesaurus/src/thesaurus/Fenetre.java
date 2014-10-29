package thesaurus;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	
	private View vueCourante;
	
	public Fenetre() {
		this.vueCourante = null;
	}
	
	public Fenetre(View v) {
		this.vueCourante = v;
	}
	
	public void setView(View vue) {
		
	}
	
	public View getVueCourante() {
		return this.vueCourante;
	}
}
