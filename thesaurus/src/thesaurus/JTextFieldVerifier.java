package thesaurus;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.examples.complete.Utils;

public class JTextFieldVerifier extends InputVerifier {
        private  int length = 0;
        private BalloonTip balloonTip;
        
        private String errorMessage = "";
        
        public JTextFieldVerifier(int length, ArrayList<VueAjoutMot> classesList) {
        	this.length = length;
        }
        
        private void createBalloonTip(JTextField cmp, String message){
        	if(balloonTip!=null)
        		balloonTip.closeBalloon();
        	
        	cmp.setBackground(new Color(255, 226, 198));
        	balloonTip = new BalloonTip(cmp, message);
        }
        
        public String getErrorMessage(){
        	return this.errorMessage;
        }

        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            
            String mot = textField.getText().trim().toLowerCase();
            Mot m = new Mot(null,mot,"",null,null,null,null);
            boolean existe = Mot.existe(m);
            
            if (textField.getText().trim().length()<=length) {
            	errorMessage = "Le mot doit contenir au moins " + this.length + " caractère";
            	createBalloonTip(textField, errorMessage);	
            	
                return false;
            } 
            else if (existe) {
            	errorMessage = "Ce mot existe déjà";
            	createBalloonTip(textField, errorMessage);	
            	
                return false;
            }
            else {
            	textField.setBackground(Color.WHITE);

            	if(balloonTip!=null)
            		balloonTip.closeBalloon();
            	
                return true;
            }
        }
        
        public String getAttributeNameError(String input, ArrayList<VueAjoutMot> attributesList){
            
            if (input.trim().length()<=length) {
                return "The field must contains more than " + this.length + " characters";
            } 
            else {
                return null;
            }
        }
        
        public String getOperationNameError(String input, ArrayList<VueAjoutMot> operationsList){
            
            if (input.trim().length()<=length) {
                return "The field must contains more than " + this.length + " characters";
            } 
            else {
                return null;
            }
        }
        
        public String getParameterNameError(String input, ArrayList<VueAjoutMot> parametersList){
            
            if (input.trim().length()<=length) {
                return "The field must contains more than " + this.length + " characters";
            } 
            else {
                return null;
            }
        }
}
