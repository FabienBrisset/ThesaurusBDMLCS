package thesaurus;

import java.sql.*;
import java.util.*;

/**
 * Classe Mot du Modele permettant les operations en BD du type Mot.
 * Cette classe utilise la classe ModelBD pour la gestion des requetes. 
 * Les methodes static qu'elle possède (mis à part getMotByRef()) ont pour but de factoriser le code et sont donc inclus dans les méthodes principales de la classe.
 *  
 * @author Axel
 *
 */

public class Mot {
	
	/**
	 * Liste des attributs de la classe Mot propre à celle de la representation en BD de la table MOT
	 * 
	 * libelleMot : designation du mot
	 * definitionMot : definition du mot
	 * pereMot : variable de type Ref qui est la representation d'un pointeur en BD (i.e Ref permet de stocker des adresses de structure en BD)
	 * filsMot, synonymesMot, associationsMot : différents types de relations représentés par des tableaux de Ref
	 */
	
	protected String libelleMot;
	protected String definitionMot;
	protected Ref pereMot;
	protected ArrayList<Ref> filsMot;
	protected ArrayList<Ref> synonymesMot;
	protected ArrayList<Ref> associationsMot;
	
	
	/**
	 * Constructeur à vide
	 */
	public Mot()
	{
		this.libelleMot = new String();
		this.definitionMot = new String();
		this.pereMot = null;
		this.filsMot = new ArrayList<Ref>();
		this.synonymesMot = new ArrayList<Ref>();
		this.associationsMot = new ArrayList<Ref>();
	}
	
	/**
	 * Constructeur par recopie
	 */
	public Mot(String libelle, String definition, Ref pere, ArrayList<Ref> fils, ArrayList<Ref> synonymes, ArrayList<Ref> associations)
	{
		this.libelleMot = libelle;
		this.definitionMot = definition;
		this.pereMot = pere;
		this.filsMot = fils;
		this.synonymesMot = synonymes;
		this.associationsMot = associations;
	}
	
	/**
	 * Constructeur qui à partir du libelle d'un mot recupère le mot en BD et le construit
	 */
	public Mot(String libelle)
	{
		try 
		{
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("SELECT * FROM LesMots lm WHERE lm.libelleMot= ?");
			requete.setString(1, libelle);
			ResultSet rs = requete.executeQuery();
			
			if (rs.next())
			{
				this.libelleMot=rs.getString(1);
				this.definitionMot=rs.getString(2);
				this.pereMot=rs.getRef(3);
				
				this.filsMot = listeRef(rs.getArray(4));
				this.synonymesMot = listeRef(rs.getArray(5));
				this.associationsMot = listeRef(rs.getArray(6));	
				
				
				requete.close();
				
			}
			else
			{
				System.out.println("Erreur : mot introuvable");
				return;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return;
		}
	}
	
	/**
	 * 
	 * ACCESSEURS EN CONSULTATION / MODIFICATION
	 * 
	 */
	public String getLibelleMot() {
		return libelleMot;
	}

	public void setLibelleMot(String libelleMot) {
		this.libelleMot = libelleMot;
	}

	public String getDefinitionMot() {
		return definitionMot;
	}

	public void setDefinitionMot(String definitionMot) {
		this.definitionMot = definitionMot;
	}

	public Ref getPereMot() {
		return pereMot;
	}

	public void setPereMot(Ref pereMot) {
		this.pereMot = pereMot;
	}

	public ArrayList<Ref> getFilsMot() {
		return filsMot;
	}

	public void setFilsMot(ArrayList<Ref> filsMot) {
		this.filsMot = filsMot;
	}

	public ArrayList<Ref> getSynonymesMot() {
		return synonymesMot;
	}

	public void setSynonymesMot(ArrayList<Ref> synonymesMot) {
		this.synonymesMot = synonymesMot;
	}

	public ArrayList<Ref> getAssociationsMot() {
		return associationsMot;
	}

	public void setAssociationsMot(ArrayList<Ref> associationsMot) {
		this.associationsMot = associationsMot;
	}
	
	/**
	 * Methode static qui permet d'obtenir un mot par sa reference en BD
	 * 
	 * @param oid : est une reference de Mot
	 * @return une instance de Mot correspondante au mot qui possede la reference oid
	 */
	public static Mot getMotByRef(Ref oid)
	{
		try 
		{
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("SELECT * FROM LesMots lm WHERE ref(lm)= ?");
			requete.setRef(1, oid);
			ResultSet rs = requete.executeQuery();
			
			if (rs.next())
			{
				String libelle=rs.getString(1);
				String definition=rs.getString(2);
				Ref pere=rs.getRef(3);
				
				ArrayList<Ref> fils = listeRef(rs.getArray(4));
				ArrayList<Ref> synonymes = listeRef(rs.getArray(5));
				ArrayList<Ref> associations = listeRef(rs.getArray(6));	
				
				
				requete.close();
				
				return new Mot(libelle,definition,pere,fils,synonymes,associations);
				
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return null;
		}
		
	}
	
	/**
	 * Permet d'obtenir la reference en BD du mot
	 * 
	 * @return la reference du mot
	 */
	public Ref getRef()
	{
		try 
		{
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("SELECT ref(lm) FROM LesMots lm WHERE lm.libelleMot= ?");
			requete.setString(1, this.libelleMot);
			ResultSet rs = requete.executeQuery();
			
			if (rs.next())
			{
				
				Ref maRef = rs.getRef(1);
				requete.close();
				return maRef;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	/**
	 * Permet l'insertion d'un mot en BD
	 * 
	 * Note : Les relations "symetriques" tel que les synonymes et Pere/Fils sont gérer dans les deux sens (coté mot mais aussi au niveau des mots liés à celui-ci)
	 * 
	 * @return le nombre de ligne affecté par l'operation, -1 si echec
	 */
	public int insert()
	{
		try 
		{
			int rowsAffected = 0;
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("INSERT INTO LesMots VALUES (Mot(?,?,?,ensMot(),ensMot(),ensMot()))"); //insertion du mot à "proprement dit"
			requete.setString(1,this.libelleMot);
			requete.setString(2,this.definitionMot);
		
			if (this.pereMot!=null) // verification (si null -> affectation de NULL sinon -> affectation de la reference du pere du mot
			{
				requete.setRef(3,this.pereMot);
			}
			else
			{
				requete.setNull(3,java.sql.Types.REF,"MOT");
			}
			
			rowsAffected = requete.executeUpdate();
			
			requete.close();
			
			if (rowsAffected > 0)
			{
				Ref refMotAjoute = this.getRef();
				
				
				if (this.pereMot!=null) // si le pere du Mot n'est pas null on ajout au pere un fils qui est le mot en question
				{
					requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?) VALUES ((?))");
					requete.setRef(1,this.pereMot);
					requete.setRef(2,refMotAjoute);
					rowsAffected = requete.executeUpdate();
					requete.close();
				}
				
				//insertions des differentes types de relation
				if (this.filsMot.size() > 0)
				{
					rowsAffected = Mot.insertRelations("filsMot",this.filsMot,refMotAjoute);
				}
				
				if (this.synonymesMot.size() > 0)
				{
					rowsAffected = Mot.insertRelations("synonymesMot",this.synonymesMot,refMotAjoute);
				}
				
				if (this.associationsMot.size() > 0)
				{
					rowsAffected = Mot.insertRelations("associationsMot",this.associationsMot,refMotAjoute);
				}
			}
			
			return rowsAffected;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return -1;
		}
		
	}
	
	
	/**
	 * Permet la modification d'un mot en BD
	 * 
	 * Note : Les relations "symetriques" tel que les synonymes et Pere/Fils sont gérer dans les deux sens (coté mot mais aussi au niveau des mots liés à celui-ci)
	 * 
	 * @param ancienMot : Etat (instance du mot) avant les modifications
	 * @return le nombre de ligne affecté par l'operation, -1 si echec
	 */
	public int update(Mot ancienMot)
	{
		try 
		{
			Ref referenceMot = ancienMot.getRef();
			
			int rowsAffected = 0;
			
			if (this.pereMot!=null) // si le mot possede un pere alors on regarde si ce pere est le meme que le pere de l'ancien mot et si ce n'est pas le cas on modifie la relation Pere/fils 
			{
				if (! this.pereMot.toString().equals(ancienMot.getPereMot().toString()))
				{
					PreparedStatement requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?)t WHERE value(t) = ?");
					requete.setRef(1,ancienMot.getPereMot());
					requete.setRef(2,referenceMot);
					rowsAffected = requete.executeUpdate();
					requete.close();
				
					requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?) VALUES ((?))");
					requete.setRef(1,this.pereMot);
					requete.setRef(2,referenceMot);
					rowsAffected = requete.executeUpdate();
					requete.close();
				}
			}
			else // sinon on enleve le fils (la ref du mot) à l'ancien pere
			{
				PreparedStatement requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?)t WHERE value(t) = ?");
				requete.setRef(1,ancienMot.getPereMot());
				requete.setRef(2,referenceMot);
				rowsAffected = requete.executeUpdate();
				
				requete.close();
			}
			
			//modifications des differentes types de relation
			rowsAffected = Mot.updateRelations("filsMot",ancienMot.getFilsMot(),this.filsMot,referenceMot);
			rowsAffected = Mot.updateRelations("synonymesMot",ancienMot.getSynonymesMot(),this.synonymesMot,referenceMot);
			rowsAffected = Mot.updateRelations("associationsMot",ancienMot.getAssociationsMot(),this.associationsMot,referenceMot);
			
			//modification du mot à "proprement dit"
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("UPDATE LesMots lm SET lm.libelleMot = ?, lm.definitionMot = ?, lm.pereMot = ? WHERE REF(lm) = ?");
			requete.setString(1,this.libelleMot);
			requete.setString(2,this.definitionMot);
		
			if (this.pereMot!=null)
			{
				requete.setRef(3,this.pereMot);
			}
			else
			{
				requete.setNull(3,java.sql.Types.REF,"MOT");
			}
			
			requete.setRef(4,referenceMot);
			
			rowsAffected = requete.executeUpdate();
			
			requete.close();
			
			return rowsAffected;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return -1;
		}
	}
	

	/**
	 * Permet la suppression d'un mot en BD
	 * 
	 * @return le nombre de ligne affecté par l'operation, -1 si echec
	 */
	public int delete()
	{
		try 
		{
			Ref referenceMot = this.getRef();
			
			int rowsAffected = 0;
			
			
			
			if (this.pereMot!=null) // si le mot possède un pere on supprime le mot de ses fils
			{
				PreparedStatement requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?)t WHERE value(t) = ?");
				requete.setRef(1,this.pereMot);
				requete.setRef(2,referenceMot);
				rowsAffected = requete.executeUpdate();
			
				requete.close();
			}
			
			//suppression des differentes types de relation
			rowsAffected = Mot.deleteRelations("filsMot",this.filsMot,referenceMot,"delete");
		
			rowsAffected = Mot.deleteRelations("synonymesMot",this.synonymesMot,referenceMot,"delete");
	
			rowsAffected = Mot.deleteRelations("associationsMot",this.associationsMot,referenceMot,"delete");
			
			//suppression du mot à "proprement dit"
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("DELETE FROM LesMots lm WHERE REF(lm) = ?");	
			requete.setRef(1,referenceMot);
			
			rowsAffected = requete.executeUpdate();
			
			requete.close();
			
			return rowsAffected;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return -1;
		}
	}
	
	/**
	 * Permet d'obtenir une instance de Mot qui represente le pere du mot
	 * 
	 * @return un mot qui est le pere du mot en question
	 */
	public Mot getPere()
	{
		if (this.pereMot!=null)
		{
			Mot pere = Mot.getMotByRef(this.pereMot);
			
			return pere;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Permet d'obtenir une liste de mot qui est la liste des fils du Mot
	 * @return un ArrayList de mot 
	 */
	public ArrayList<Mot> getFils()
	{
		try 
		{
			ArrayList<Mot> fils = Mot.getMotsRelation("filsMot",this.libelleMot);
			
			return fils;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Permet d'obtenir une liste de mot qui est la liste des synonymes du Mot
	 * @return un ArrayList de mot 
	 */
	public ArrayList<Mot> getSynonymes()
	{
		try 
		{
			ArrayList<Mot> synonymes = Mot.getMotsRelation("synonymesMot",this.libelleMot);
			
			return synonymes;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Permet d'obtenir une liste de mot qui est la liste des associations du Mot
	 * @return un ArrayList de mot 
	 */
	public ArrayList<Mot> getAssociations()
	{
		try 
		{
			ArrayList<Mot> associations = Mot.getMotsRelation("associationsMot",this.libelleMot);
			
			return associations;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Implementation de la méthode Object toString()
	 * @return une representation String d'un mot
	 */
	public String toString()
	{
		String str = new String();
		
		str = "libelle : " + this.libelleMot + "\n";
		str = str + "definition : " + this.definitionMot + "\n";
		
		if ( this.pereMot!=null)
		{
			str = str + "pere : " + this.getPere().getLibelleMot() + "\n" ;
		}
		else
		{
			str = str + "pere : \n" ;
		}
		
		str = str + "Fils : ";
		
		ArrayList<Mot> mots = this.getFils();
		for (int i=0;i<mots.size();i++)
		{
			str = str + mots.get(i).getLibelleMot() + " ";
		}
		
		str = str + "\n";
		
		str = str + "Synonymes : ";
		
		mots = this.getSynonymes();
		for (int i=0;i<mots.size();i++)
		{
			str = str + mots.get(i).getLibelleMot() + " ";
		}
		
		str = str + "\n";
		
		str = str + "Associations : ";
		
		mots = this.getAssociations();
		
		for (int i=0;i<mots.size();i++)
		{
			str = str + mots.get(i).getLibelleMot() + " ";
		}
		
		str = str + "\n";
		
		return str;			
	}
	
	
	
	/******************************* METHODES STATIQUES UTILISEES POUR LA FACTORISATION DU CODE *******************************/
	/**************************** A NE PAS UTILISER A L'EXTERIEUR DE CETTE CLASSE !!!!! *********************************/
	
	/**
	 * Permet de convertir un Array (tableau en BD) en une liste de réference
	 * 
	 * @param a Array 
	 * @return ArrayList de Ref de mot
	 * @throws SQLException
	 */
	public static ArrayList<Ref> listeRef(Array a) throws SQLException
	{
		ArrayList<Ref> listRef = new ArrayList<Ref>();
		
		if (a!=null)
		{
			ResultSet tableau = a.getResultSet();
			
			while (tableau.next())
			{
				listRef.add(tableau.getRef(2));
			}
		}
		
		return listRef;
	}
	
	/**
	 * Permet d'inserer les relations d'un mot 
	 * Gère aussi la symétrie des relations synonymes et Pere/Fils
	 * 
	 * @param typeRelation type de la relation
	 * @param relations liste des references de mot relié au mot par le type de relation defini en parametre
	 * @param refMot reference du mot en question
	 * @return nombre de lignes affectées
	 * @throws SQLException
	 */
	public static int insertRelations(String typeRelation, ArrayList<Ref> relations, Ref refMot) throws SQLException
	{
		int rowsAffected = 0;
		
		//insertion des relations
		PreparedStatement requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm."+typeRelation+" FROM LesMots lm WHERE REF(lm) = ?) VALUES ((?))");
		
		requete.setRef(1,refMot);
		
		for (int i=0; i<relations.size(); i++)
		{
			requete.setRef(2,relations.get(i));
			rowsAffected = requete.executeUpdate();
		}
		
		requete.close();
		
		//gestion des relations symétrique
		if (typeRelation.equals("filsMot"))
		{
			requete = ModelDB.getDB().db.prepareStatement("UPDATE LesMots lm SET lm.pereMot = ? WHERE REF(lm) = ?");
			
			requete.setRef(1,refMot);
			
			for (int i=0; i<relations.size(); i++)
			{
				requete.setRef(2,relations.get(i));
				rowsAffected = requete.executeUpdate();
			}
			
			requete.close();
		}
		
		if (typeRelation.equals("synonymesMot"))
		{
			requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm.synonymesMot FROM LesMots lm WHERE REF(lm) = ?) VALUES ((?))");
			
			requete.setRef(2,refMot);
			
			for (int i=0; i<relations.size(); i++)
			{
				requete.setRef(1,relations.get(i));
				rowsAffected = requete.executeUpdate();
			}
			
			requete.close();
		}
		
		
		return rowsAffected;
	}
	
	/**
	 * Permet de modifier les relations d'un mot 
	 * 
	 * Dans un premier temps on supprime toutes les anciennes relations et puis on insert les nouvelles relations 
	 * 
	 * Gère aussi la symétrie des relations synonymes et Pere/Fils
	 * 
	 * @param typeRelation type de la relation
	 * @param anciennesRelations liste des anciennes relations à remplacer
	 * @param nouvellesRelations liste des nouvelles relations 
	 * @param refMot reference du mot en question
	 * @return nombre de lignes affectées
	 * @throws SQLException
	 */
	public static int updateRelations(String typeRelation, ArrayList<Ref> anciennesRelations, ArrayList<Ref> nouvellesRelations, Ref refMot) throws SQLException
	{
		int rowsAffected = 0;
		
		rowsAffected = Mot.deleteRelations(typeRelation,anciennesRelations,refMot,"update");
		
		rowsAffected = Mot.insertRelations(typeRelation,nouvellesRelations,refMot);
		
		return rowsAffected;
	}
	
	/**
	 * Permet de supprimer les relations d'un mot 
	 * 
	 * @param typeRelation type de la relation
	 * @param relations liste des references de mot relié au mot par le type de relation defini en parametre
	 * @param refMot reference du mot en question
	 * @param mode utiliser uniquement pour permettre la suppression des relations d'associations
	 * @return
	 * @throws SQLException
	 */
	public static int deleteRelations(String typeRelation, ArrayList<Ref> relations, Ref refMot, String mode) throws SQLException
	{
		int rowsAffected = 0;
		
		//suppression des relations
		PreparedStatement requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm."+typeRelation+" FROM LesMots lm WHERE REF(lm) = ?)");
		requete.setRef(1,refMot);
		
		rowsAffected = requete.executeUpdate();
		
		requete.close();
		
		//gestion des relations symétriques
		if (typeRelation.equals("filsMot"))
		{
			requete = ModelDB.getDB().db.prepareStatement("UPDATE LesMots lm SET lm.pereMot = ? WHERE REF(lm) = ?");
			
			requete.setNull(1,java.sql.Types.REF,"MOT");
			
			for (int i=0; i<relations.size(); i++)
			{
				requete.setRef(2,relations.get(i));
				rowsAffected = requete.executeUpdate();
			}
			
			requete.close();
		}
		
		if (typeRelation.equals("synonymesMot"))
		{
			requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.synonymesMot FROM LesMots lm WHERE REF(lm) = ?) t WHERE value(t) = ?");
			
			requete.setRef(2,refMot);
			
			for (int i=0; i<relations.size(); i++)
			{
				requete.setRef(1,relations.get(i));
				rowsAffected = requete.executeUpdate();
			}
			
			requete.close();
		}
		
		if (typeRelation.equals("associationsMot") && mode.equals("delete"))
		{
			requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.associationsMot FROM LesMots lm WHERE REF(lm) = ?) t WHERE value(t) = ?");
			
			requete.setRef(2,refMot);
			
			for (int i=0; i<relations.size(); i++)
			{
				requete.setRef(1,relations.get(i));
				rowsAffected = requete.executeUpdate();
			}
			
			requete.close();
		}
		
		return rowsAffected;
	}
	
	/**
	 * Donne la liste des mots relié par le mot en question avec la relation defini en parametre
	 * 
	 * @param relation type de relation
	 * @param libelle libelle du mot
	 * @return arraylist de mots liés avec le mot par la relation defini en parametre
	 * @throws SQLException
	 */
	public static ArrayList<Mot> getMotsRelation(String relation,String libelle) throws SQLException
	{
		ArrayList<Mot> mots = new ArrayList<Mot>();
		
		PreparedStatement requete = ModelDB.getDB().db.prepareStatement("SELECT value(t).libelleMot,value(t).definitionMot,value(t).pereMot,value(t).filsMot,value(t).synonymesMot,value(t).associationsMot FROM THE(SELECT lm."+relation+" FROM LesMots lm WHERE lm.libelleMot = ?) t");
		requete.setString(1,libelle);
		ResultSet rs = requete.executeQuery();
		
		while (rs.next())
		{
			mots.add(new Mot(rs.getString(1),rs.getString(2),rs.getRef(3),Mot.listeRef(rs.getArray(4)),Mot.listeRef(rs.getArray(5)),Mot.listeRef(rs.getArray(6))));
		}

		requete.close();
		
		return mots;
	}
}
