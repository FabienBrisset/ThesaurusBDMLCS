package thesaurus;

import java.sql.*;

public class MainTestModele {

	public static void main(String[] args) {
		
		System.out.println("**** TEST MODELE ***** \n\n\n");
		
/****************** TEST INSERT **********************/
		
//		Mot unMotInsert = new Mot(); 
//		unMotInsert.setLibelleMot("plateau");
//		unMotInsert.setDefinitionMot("un suppport tres util");
//		//unMotInsert.setPereMot((new Mot("table","",null,null,null,null).getRef()));
//		unMotInsert.setPereMot(null);
//		
//		String[] fils = {"eau","vin"};
//		String[] syn = {"verre","chaise"};
//		String[] asso = {"fourchette"};
//		
//		for (int i=0;i<fils.length;i++)
//		{
//			unMotInsert.getFilsMot().add(new Mot(fils[i],"",null,null,null,null).getRef());
//			unMotInsert.getSynonymesMot().add(new Mot(syn[i],"",null,null,null,null).getRef());
//		}
//		
//		unMotInsert.getAssociationsMot().add(new Mot(asso[0],"",null,null,null,null).getRef());
//		
//		unMotInsert.insert();
//		
//		System.out.println(unMotInsert.toString());		
		
/****************** TEST UPDATE **********************/
//		Mot monMot = new Mot();
//		
//		monMot.setLibelleMot("table");
//		
//		Ref maref = monMot.getRef();
//		
//		
//
//		System.out.println(maref.toString());
//		
//		
//		
//		Mot ancienMot = Mot.getMotByRef(maref);
//		Mot monMot2 = new Mot();
//		
//		monMot2.setLibelleMot("toto");
//		monMot2.setDefinitionMot(ancienMot.getDefinitionMot());
//		monMot2.setPereMot(new Mot("cuisine","",null,null,null,null).getRef());
//		//monMot2.setPereMot(null);
//		//monMot2.setFilsMot(ancienMot.getFilsMot());
//		
//		System.out.println(monMot2.toString());
//		
////		monMot2.getFilsMot().remove(monMot2.getFilsMot().size()-1);
////		monMot2.getFilsMot().remove(monMot2.getFilsMot().size()-2);
//		
//		String[] fils = {"vin"};
//		String[] syn = {"couteau","vin"};
//		String[] asso = {"fourchette"};
//		
//		for (int i=0;i<syn.length;i++)
//		{
//			
//			monMot2.getSynonymesMot().add(new Mot(syn[i],"",null,null,null,null).getRef());
//		}
//		
//		monMot2.getFilsMot().add(new Mot(fils[0],"",null,null,null,null).getRef());
//		monMot2.getAssociationsMot().add(new Mot(asso[0],"",null,null,null,null).getRef());
//		
//		monMot2.update(ancienMot);
//		
//		monMot2 = Mot.getMotByRef(maref);
//		
//		System.out.println(monMot2.toString());
	
		

		
/****************** TEST DELETE **********************/	
//		Mot monMot = new Mot();
//		
//		monMot.setLibelleMot("plateau");
//		
//		Ref maref = monMot.getRef();
//		
//		
//
//		System.out.println(maref.toString());
//		
//		
//		
//		Mot ancienMot = Mot.getMotByRef(maref);
//		
//		ancienMot.delete();
	}
}