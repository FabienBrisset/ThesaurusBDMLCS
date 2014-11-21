package thesaurus;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	
	private View vueCourante;
	private VueAjoutMot vueAjout;
	
	public Fenetre() {
		this.vueCourante = null;
		this.setTitle("Thesaurus");
	    this.setSize(1280, 720);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    this.setVisible(true);
	}
	
	public Fenetre(View v) {
		this.vueCourante = v;
	}
	
	public void setView(View vue) {
		this.vueCourante = vue;
	}
	
	public View getVueCourante() {
		return this.vueCourante;
	}

	public VueAjoutMot getVueAjout() {
		return vueAjout;
	}

	public void setVueAjout(VueAjoutMot vueAjout) {
		this.vueAjout = vueAjout;
	}
}
