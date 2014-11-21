package thesaurus;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	
	private View vueCourante;
	private VueAjoutMot vueAjout;
	private VueMot vueMot;
	private VueArborescenceMot vueArborescence;
	
	public VueMot getVueMot() {
		return vueMot;
	}

	public void setVueMot(VueMot vueMot) {
		this.vueMot = vueMot;
	}

	public VueArborescenceMot getVueArborescence() {
		return vueArborescence;
	}

	public void setVueArborescence(VueArborescenceMot vueArborescence) {
		this.vueArborescence = vueArborescence;
	}

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
