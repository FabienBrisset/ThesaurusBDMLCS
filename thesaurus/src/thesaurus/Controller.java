package thesaurus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller implements ActionListener, ChangeListener {
	
	protected static Fenetre fenetre;
	public static ControllerMots controllerMots;

	public synchronized static ControllerMots getControllerMots(){
		if(controllerMots == null)
			controllerMots = new ControllerMots();
		
		return controllerMots;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public Fenetre getFenetre() {		
		return fenetre;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
}
