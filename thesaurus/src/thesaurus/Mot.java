package thesaurus;

import java.sql.*;
import java.util.*;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * Classe Mot du Modele permettant les operations en BD du type Mot.
 * Cette classe utilise la classe ModelBD pour la gestion des requetes. 
 * Elle implemente l'interface SQLDate permettant de "mapper" la classe avec son type en BD
 *  
 * @author Axel
 *
 */

public class Mot implements SQLData {
	
	/**
	 * Liste des attributs de la classe Mot propre à celle de la representation en BD de la table MOT
	 * 
	 * -- Attributs propres au Mot --
	 * libelleMot : designation du mot
	 * definitionMot : definition du mot
	 * pereMot : variable de type Mot permettant d'avoir une structure de type Mot du pere
	 * filsMot, synonymesMot, associationsMot : différents types de relations représentés par des ArrayLists de Mot
	 * 
	 * -- Attributs complémentaires --
	 * oidMot : adresse (Ref) du Mot en BD, ce qui nous permet les operations sur les relations (pere,fils,synonymes,associations) en BD
	 * sqlType : type du Mot en BD, est obligatoire pour implémenter l'interface SQLData
	 * 
	 * oid : attribut static permettant l'attribution de l'oid du Mot lors de l'optention en BD
	 * historiqueMotFils,historiqueMotSynonymes,historiqueMotAssociations : ces attributs static permettent de garder un historique 
	 * des différents types de relation et d'evité de ce fait une eventuel recursivité "infini" à cause de doublons rencontrés lors de l'optention du Mot en BD
	 */
	
	protected String libelleMot;
	protected String definitionMot;
	protected Mot pereMot;
	protected ArrayList<Mot> filsMot;
	protected ArrayList<Mot> synonymesMot;
	protected ArrayList<Mot> associationsMot;
	
	private Ref oidMot;
	private String sqlType = "SYSTEM.MOT";
	
	private static Ref oid; 
	private static ArrayList<Ref> historiqueMotFils;
	private static ArrayList<Ref> historiqueMotSynonymes;
	private static ArrayList<Ref> historiqueMotAssociations;
	
	/**
	 * 								******************************  CONSTRUCTEURS ******************************
	 */
	
	/**
	 * Constructeur à vide
	 */
	public Mot()
	{
		this.libelleMot = new String();
		this.definitionMot = new String();
		this.pereMot = null;
		this.filsMot = new ArrayList<Mot>();
		this.synonymesMot = new ArrayList<Mot>();
		this.associationsMot = new ArrayList<Mot>();
	}
	
	/**
	 * Constructeur par recopie
	 */
	public Mot(Ref oid,String libelle, String definition, Mot pere, ArrayList<Mot> fils, ArrayList<Mot> synonymes, ArrayList<Mot> associations)
	{
		this.oidMot = oid;
		this.libelleMot = libelle;
		this.definitionMot = definition;
		this.pereMot = pere;
		this.filsMot = fils;
		this.synonymesMot = synonymes;
		this.associationsMot = associations;
		
		historiqueMotFils = new ArrayList<Ref>();
		historiqueMotSynonymes = new ArrayList<Ref>();
		historiqueMotAssociations = new ArrayList<Ref>();
	}
	
	/**
	 * Constructeur qui à partir du libelle d'un mot recupère le mot en BD et le construit
	 */
	public Mot(String libelle)
	{
		historiqueMotFils = new ArrayList<Ref>();
		historiqueMotSynonymes = new ArrayList<Ref>();
		historiqueMotAssociations = new ArrayList<Ref>();
		
		try 
		{
			// On mappe l'objet Mot Java avec celui en BD
			Map<String,Class<?>> map = ModelDB.getDB().db.getTypeMap(); 
			ModelDB.getDB().db.setTypeMap(map);
			map.put("SYSTEM.MOT", Class.forName("thesaurus.Mot"));
			
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("SELECT REF(lm),VALUE(lm) FROM LesMots lm WHERE lm.libelleMot= ?");
			requete.setString(1, libelle);
			ResultSet rs = requete.executeQuery();
			
			if (rs.next())
			{
				Mot m = new Mot();
				
				//permet de garder l'oid courant en recursivité
				oid = rs.getRef(1);
				
				//on sauvegarde l'oid du mot en cours pour eviter une possible recursivité du mot sur lui-meme ce qui engendrerai un "Stack overflow"
				historiqueMotFils.add(oid);
				historiqueMotSynonymes.add(oid);
				historiqueMotAssociations.add(oid);
				
				//on stocke directement l'objet dans m grace au mappage
				m = (Mot) rs.getObject(2);
				
				//une fois l'objet m construit, on affecte ses différents attributs à l'objet courant this 
				this.oidMot = m.getOid();
				this.libelleMot = m.getLibelleMot();
				this.definitionMot = m.getDefinitionMot();
				this.pereMot = m.getPereMot();
				this.filsMot = m.getFilsMot();
				this.synonymesMot = m.getSynonymesMot();
				this.associationsMot = m.getAssociationsMot();	
				
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
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	
	/**
	 * 				******************************  ACCESSEURS EN CONSULTATION / MODIFICATION ******************************
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

	public Mot getPereMot() {
		return pereMot;
	}

	public void setPereMot(Mot pereMot) {
		this.pereMot = pereMot;
	}

	public ArrayList<Mot> getFilsMot() {
		return filsMot;
	}

	public void setFilsMot(ArrayList<Mot> filsMot) {
		this.filsMot = filsMot;
	}

	public ArrayList<Mot> getSynonymesMot() {
		return synonymesMot;
	}

	public void setSynonymesMot(ArrayList<Mot> synonymesMot) {
		this.synonymesMot = synonymesMot;
	}

	public ArrayList<Mot> getAssociationsMot() {
		return associationsMot;
	}

	public void setAssociationsMot(ArrayList<Mot> associationsMot) {
		this.associationsMot = associationsMot;
	}
	
	public Ref getOid()
	{
		return this.oidMot;
	}
	
	public void setOid(Ref r)
	{
		this.oidMot = r;
	}
	
	/**
	 * 				******************************  IMPLEMENTATION DE L'INTERFACE SQLData ******************************
	 * 
	 * méthodes à implémenter avec l'interface SQLData
	 * - getSQLTypeName() : retourne le type du Mot en BD
	 * - readSQL(SQLInput stream, String type) : permet la lecture d'un objet mappé en BD et est applé à chaque fois que l'on effectue un getObject()
	 * - writeSQL(SQLOutput stream) : permet l'écriture d'un objet mappé en BD et est applé à chaque fois que l'on effectue un setObject()
	 * 
	 */
	
	public String getSQLTypeName()
	{
		return this.sqlType;
	}
	
	public void readSQL(SQLInput stream, String type) throws SQLException 
	{
		this.sqlType = type;
		
		this.oidMot = oid;
		
		this.libelleMot = stream.readString();
		this.definitionMot = stream.readString();
		Ref r = stream.readRef();
		
		//concernant la lecture d'un tableau, on utilise une fonction auxiliare 'traitementDesRefs' afin de convertir le tableau en ArrayList<Ref> et de traiter les possibles doublons rencontrés grace 
		//aux attributs static "historique" 
		ArrayList<Ref> fils = Mot.traitementDesRefs(stream.readArray(),historiqueMotFils);
		ArrayList<Ref> synonymes = Mot.traitementDesRefs(stream.readArray(),historiqueMotSynonymes);
		ArrayList<Ref> associations = Mot.traitementDesRefs(stream.readArray(),historiqueMotAssociations);
		
		//permet la convertion ArrayList<Ref> --> ArrayList<Mot> fonction auxiliaire 'ArrayReftoArraylisteMot'
		this.filsMot = Mot.ArrayReftoArraylisteMot(fils);
		this.synonymesMot = Mot.ArrayReftoArraylisteMot(synonymes);
		this.associationsMot = Mot.ArrayReftoArraylisteMot(associations);
		
		//traitement de l'attribut Père
		if (r != null)
		{
			oid = r;
			this.pereMot = (Mot) r.getObject();
		}
		else
		{
			this.pereMot = null;
		}
	}

	public void writeSQL(SQLOutput stream) throws SQLException 
	{
		stream.writeString(this.libelleMot);
		stream.writeString(this.definitionMot);
		
		if (this.pereMot != null)
		{
			stream.writeRef(this.pereMot.getOid());
		}
		
		//N'ayant trouvé aucune solution concernant l'ecriture direct d'un Array en BD, on effectue l'initialisation et les traitements à la main (en traitant un par un par les elements)
		// dans les methodes insert,update,delete.
		stream.writeArray(null);
		stream.writeArray(null);
		stream.writeArray(null);
	}
	
	/**
	 * 					******************************  METHODE POUR REPRESENTATION GRAPHIQUE DU MOT ******************************
	 */
	
	public TreeNode<Mot> getArborescence(){
		ArrayList<TreeNode<Mot>> aTraiterTreeNode = new ArrayList<TreeNode<Mot>>();
		ArrayList<Mot> aTraiterMot = new ArrayList<Mot>();
		aTraiterMot.add(this);
		TreeNode<Mot> top = new TreeNode<Mot>(this);
		aTraiterTreeNode.add(top);
		TreeNode<Mot> currentNode = top;
		
		while(aTraiterMot.size() > 0){
			Mot motCourant = aTraiterMot.get(0);
			ArrayList<Mot> filsMotCourant = motCourant.getFilsMot();
			for(int i=0; i<filsMotCourant.size(); i++){
				TreeNode<Mot> addedNode = currentNode.addChild(filsMotCourant.get(i));
				aTraiterTreeNode.add(addedNode);
				aTraiterMot.add(filsMotCourant.get(i));
			}
			
			aTraiterMot.remove(0);
			aTraiterTreeNode.remove(0);
			if(aTraiterMot.size() > 0){
				currentNode = aTraiterTreeNode.get(0);
			}
		}
		
		return top;
	}
	
	
	
	/**
	 * 				******************************  METHODES D'OPERATION EN BD ******************************
	 */
	
	/**
	 * Permet l'ajout d'un mot en BD
	 * @return vrai si le mot a été correctement ajouté, faux sinon
	 */
	public boolean insert()
	{
		try 
		{
			// On mappe l'objet Mot Java avec celui en BD
			Map<String,Class<?>> map = ModelDB.getDB().db.getTypeMap(); 
			ModelDB.getDB().db.setTypeMap(map);
			map.put("SYSTEM.MOT", Class.forName("thesaurus.Mot"));
			
			int rowsAffected = 0;
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("INSERT INTO LesMots VALUES (?)"); // on insere le mot
			
			requete.setObject(1,this);
			
			rowsAffected = requete.executeUpdate();
			
			requete.close();
			
			if (rowsAffected > 0)
			{
				this.oidMot = this.getRef();
				
				// on insere l'ensemble des relations du Mot grace à la méthode auxilaire 'insertRelations' permettant l'insertion un par un des elements de chaque relation
				boolean insertionFils = Mot.insertRelations("filsMot", Mot.ArrayListMotToArrayRef(this.filsMot), this.oidMot);
				boolean insertionSynonymes = Mot.insertRelations("synonymesMot", Mot.ArrayListMotToArrayRef(this.synonymesMot), this.oidMot);
				boolean insertionAssociations = Mot.insertRelations("associationsMot", Mot.ArrayListMotToArrayRef(this.associationsMot), this.oidMot);
				
				if (insertionFils && insertionSynonymes && insertionAssociations)
				{
					if (this.pereMot != null) // si le pere du Mot n'est pas null on ajout au pere un fils qui est le mot en question
					{
						requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?) VALUES (?)");
						
						requete.setRef(1,this.pereMot.getOid());
						requete.setRef(2,this.oidMot);
						
						rowsAffected = requete.executeUpdate();
						requete.close();
					}
					
					if (rowsAffected > 0)
					{
						rowsAffected = 0;
						
						// on met à jour le pere de chacun des fils appartenant au Mot courant (MAJ symétrique)
						for (int i=0;i<this.filsMot.size();i++)
						{
							requete = ModelDB.getDB().db.prepareStatement("UPDATE LesMots lm SET lm.pereMot = ? WHERE REF(lm) = ?");
							
							requete.setRef(1,this.oidMot);
							requete.setRef(2,this.filsMot.get(i).getOid());
							
							rowsAffected = rowsAffected + requete.executeUpdate();
							requete.close();
						}
						
						if (rowsAffected == this.filsMot.size())
						{
							rowsAffected = 0;
							
							// on met à jour les synonymes de chacun des synonymes appartenant au Mot courant (MAJ symétrique)
							for (int i=0;i<this.synonymesMot.size();i++)
							{
								requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm.synonymesMot FROM LesMots lm WHERE REF(lm) = ?) VALUES (?)");
								
								requete.setRef(1,this.synonymesMot.get(i).getOid());
								requete.setRef(2,this.oidMot);
								
								rowsAffected = rowsAffected + requete.executeUpdate();
								requete.close();
							}
							
							if (rowsAffected == this.synonymesMot.size())
							{
								return true;
							}
							else
							{
								System.out.println("pb2");
								return false;
							}
						}
						else
						{
							System.out.println("pb3");
							return false;
						}
					}
					else
					{
						System.out.println("pb4");
						return false;
					}
				}
				else
				{
					System.out.println("pb5");
					return false;
				}
			}
			else
			{
				System.out.println("pb6");
				return false;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Permet la modif d'un mot en BD 
	 * @return vrai si le mot a été correctement modifié, faux sinon
	 */
	public boolean update(Mot ancienMot)
	{
		try 
		{
			// on affecte l'oid du Mot à modifier
			this.oidMot = ancienMot.getOid();
			
			// On mappe l'objet Mot Java avec celui en BD
			Map<String,Class<?>> map = ModelDB.getDB().db.getTypeMap(); 
			ModelDB.getDB().db.setTypeMap(map);
			map.put("SYSTEM.MOT", Class.forName("thesaurus.Mot"));
			
			int rowsAffected = 0;
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("UPDATE LesMots lm SET lm = ? WHERE REF(lm)= ?"); //MAJ du mot
			
			requete.setObject(1,this);
			requete.setObject(2,ancienMot.getOid());
			
			rowsAffected = requete.executeUpdate();
			
			requete.close();
			
			if (rowsAffected > 0)
			{
				// on insere l'ensemble des relations du Mot grace à la méthode auxilaire 'insertRelations' permettant l'insertion un par un des elements de chaque relation
				boolean insertionFils = Mot.insertRelations("filsMot", Mot.ArrayListMotToArrayRef(this.filsMot), this.oidMot);
				boolean insertionSynonymes = Mot.insertRelations("synonymesMot", Mot.ArrayListMotToArrayRef(this.synonymesMot), this.oidMot);
				boolean insertionAssociations = Mot.insertRelations("associationsMot", Mot.ArrayListMotToArrayRef(this.associationsMot), this.oidMot);
				
				if (insertionFils && insertionSynonymes && insertionAssociations)
				{
					// si le pere a changé alors on effectue les modifs neccessaires afin de prendre en compte ce changement
					if (this.pereMot != null && ancienMot.getPereMot() != null)
					{
						if (!this.pereMot.equals(ancienMot.getPereMot()))
						{
							requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?) t WHERE VALUE(t) = ?"); 
							requete.setRef(1, ancienMot.getPereMot().getOid());
							requete.setRef(2, this.oidMot);
							rowsAffected = requete.executeUpdate();
							requete.close();
							
							if (rowsAffected > 0)
							{
								requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?) VALUES (?)");
								requete.setRef(1, this.pereMot.getOid());
								requete.setRef(2, this.oidMot);
								rowsAffected = requete.executeUpdate();
								requete.close();
							}
							else
							{
								return false;
							}
							
						}
					}
					else if (this.pereMot != ancienMot.getPereMot())
					{
						if (this.pereMot == null)
						{
							requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?) t WHERE VALUE(t) = ?"); 
							requete.setRef(1, ancienMot.getPereMot().getOid());
							requete.setRef(2, this.oidMot);
							rowsAffected = requete.executeUpdate();
							requete.close();
						}
						else
						{
							requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?) VALUES (?)"); 
							requete.setRef(1, this.pereMot.getOid());
							requete.setRef(2, this.oidMot);
							System.out.println(rowsAffected + " -- " + this.pereMot.getOid().toString());
							rowsAffected = requete.executeUpdate();
							requete.close();
						}
					}
					
					if (rowsAffected > 0)
					{
						rowsAffected = 0;
						
						// on met à NULL le pere des fils de l'ancien Mot
						for (int i=0;i<ancienMot.getFilsMot().size();i++)
						{
							requete = ModelDB.getDB().db.prepareStatement("UPDATE LesMots lm SET lm.pereMot = NULL WHERE REF(lm) = ?");
							
							requete.setRef(1,ancienMot.getFilsMot().get(i).getOid());
							
							rowsAffected = rowsAffected + requete.executeUpdate();
							requete.close();
						}
						
						
						if (rowsAffected == ancienMot.getFilsMot().size())
						{
							rowsAffected = 0;
							
							// on met à jour le pere des fils du nouveau mot
							for (int i=0;i<this.filsMot.size();i++)
							{
								requete = ModelDB.getDB().db.prepareStatement("UPDATE LesMots lm SET lm.pereMot = ? WHERE REF(lm) = ?");
								
								requete.setRef(1,this.oidMot);
								requete.setRef(2,this.filsMot.get(i).getOid());
								
								rowsAffected = rowsAffected + requete.executeUpdate();
								requete.close();
							}
							
							if (rowsAffected == this.filsMot.size())
							{
								rowsAffected = 0;
								
								// on met à jour les synonymes de chacun des synonymes appartenant au Mot courant (MAJ symétrique)
								for (int i=0; i<this.synonymesMot.size(); i++)
								{
										requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm.synonymesMot FROM LesMots lm WHERE REF(lm) = ?) VALUES (?)"); 
										requete.setRef(1, this.synonymesMot.get(i).getOid());
										requete.setRef(2, this.oidMot);
										rowsAffected = rowsAffected + requete.executeUpdate();
										requete.close();
								}
								
								if (rowsAffected == this.synonymesMot.size())
								{
									return true;
								}
								else
								{
									return false;
								}
							}
							else
							{
								return false;
							}
						}
						else
						{
							return false;
						}
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Permet la suppression recursive d'un mot en BD 
	 * @return vrai si le mot a été correctement supprimé, faux sinon
	 */
	public boolean delete()
	{
		try 
		{
			int rowsAffected = 0;
			PreparedStatement requete;
				
			// on supprime le fils du pere du mot qui correspond au mot à supprimer
			if (this.pereMot != null)
			{
				requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE REF(lm) = ?) t WHERE VALUE(t) = ?"); 
				requete.setRef(1, this.pereMot.getOid());
				requete.setRef(2, this.oidMot);
				rowsAffected = requete.executeUpdate();
				requete.close();
			}
			else
			{
				rowsAffected = 1;
			}

			if (rowsAffected > 0)
			{
				rowsAffected = 0;
				
				// on supprime le mot dans les synonymes des synonymes du mot qui correspond au mot à supprimer
				for (int i=0; i<this.synonymesMot.size(); i++)
				{	
					requete = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.synonymesMot FROM LesMots lm WHERE REF(lm) = ?) t WHERE VALUE(t) = ?"); 
					requete.setRef(1, this.synonymesMot.get(i).getOid());
					requete.setRef(2, this.oidMot);
					rowsAffected = rowsAffected + requete.executeUpdate();
					requete.close();
				}
				
				if (rowsAffected == this.synonymesMot.size())
				{
					// on supprime les possibles associations avec le mot à supprimer
					
					// liste des possibles associations avec le Mot en question
					requete = ModelDB.getDB().db.prepareStatement("SELECT REF(lm) FROM LesMots lm WHERE (?) IN (SELECT VALUE(t) FROM TABLE(lm.associationsMot) t)"); 
					requete.setRef(1, this.oidMot);
					ResultSet rs = requete.executeQuery();
					
					// suppression de ces associations
					while (rs.next())
					{
						PreparedStatement majAssoc = ModelDB.getDB().db.prepareStatement("DELETE FROM THE(SELECT lm.associationsMot FROM LesMots lm WHERE REF(lm) = ?) t WHERE VALUE(t) = ?");
						majAssoc.setRef(1,rs.getRef(1));
						majAssoc.setRef(2,this.oidMot);
						
						rowsAffected = majAssoc.executeUpdate();
						
						if (rowsAffected == 0)
						{
							System.out.println("pb1 : " + this.libelleMot );
							return false;
						}
						
						majAssoc.close();
					}
					
					requete.close();
					
					// suppression du mot
					requete = ModelDB.getDB().db.prepareStatement("DELETE FROM LesMots lm WHERE REF(lm) = ?"); 
					requete.setRef(1,this.oidMot);
					rowsAffected = requete.executeUpdate();
					requete.close();
					
					if (rowsAffected > 0)
					{
						boolean deleteOk = true;
						int i=0;
						
						// suppression recursive de ses fils
						while (i<this.filsMot.size() && deleteOk)
						{
							Mot fils = new Mot(this.filsMot.get(i).getLibelleMot());
							fils.setPereMot(null);
							deleteOk = fils.delete();
							i++;
						}
						
						return deleteOk;
					}
					else
					{
						System.out.println("pb2 : " + this.libelleMot );
						return false;
					}
				}
				else
				{
					System.out.println("pb3 : " + this.libelleMot );
					return false;
				}
			}
			else
			{
				System.out.println("pb4 : " + this.libelleMot );
				return false;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Permet de savoir si oui ou non un mot existe en BD
	 * @return vrai si le mot existe, faux sinon
	 */
	public boolean existe()
	{
		try 
		{
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("SELECT COUNT(*) FROM LesMots lm WHERE lm.libelleMot= ?");
			requete.setString(1, this.libelleMot);
			ResultSet rs = requete.executeQuery();
			
			if (rs.next())
			{
				return  (rs.getInt(1) > 0);
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return false;
		}
	}

	
	/**
	 * Permet d'obtenir la reference en BD du mot
	 * @return la reference du mot
	 */
	public Ref getRef()
	{
		try 
		{
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("SELECT REF(lm) FROM LesMots lm WHERE lm.libelleMot= ?");
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
	
	public static ArrayList<Mot> listeDesMots()
	{
		try
		{
			Map<String,Class<?>> map = ModelDB.getDB().db.getTypeMap(); 
			ModelDB.getDB().db.setTypeMap(map);
			map.put("SYSTEM.MOT", Class.forName("thesaurus.Mot"));
			
			ArrayList<Mot> mots = new ArrayList<Mot>();
			
			PreparedStatement requete = ModelDB.getDB().db.prepareStatement("SELECT lm.libelleMot FROM LesMots lm");
			ResultSet rs = requete.executeQuery();
			
			while (rs.next())
			{
				mots.add(new Mot(rs.getString(1)));
			}
			
			return mots;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return null;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/**
	 * 				******************************  METHODE D'OPERATION SUR LA CLASSE ******************************
	 */
	
	/**
	 * Permet de savoir si un mot est present parmis les fils d'un mot
	 * @param m : mot à chercher
	 * @return vrai si le mot est trouvé, faux sinon
	 */
	public boolean estDansFils(Mot m)
	{
		int i=0;
		boolean trouve = false;
		
		while (i<this.filsMot.size() && !trouve)
		{
			if (this.filsMot.get(i).equals(m))
			{
				trouve = true;
			}
			
			i++;
		}
		
		return trouve;
	}
	
	/**
	 * Permet de savoir si un mot est present parmis les synonymes d'un mot
	 * @param m : mot à chercher
	 * @return vrai si le mot est trouvé, faux sinon
	 */
	public boolean estDansSynonymes(Mot m)
	{
		int i=0;
		boolean trouve = false;
		
		while (i<this.synonymesMot.size() && !trouve)
		{
			if (this.synonymesMot.get(i).equals(m))
			{
				trouve = true;
			}
			
			i++;
		}
		
		return trouve;
	}
	
	/**
	 * Permet de savoir si un mot est present parmis les associations d'un mot
	 * @param m : mot à chercher
	 * @return vrai si le mot est trouvé, faux sinon
	 */
	public boolean estDansAssociations(Mot m)
	{
		int i=0;
		boolean trouve = false;
		
		while (i<this.associationsMot.size() && !trouve)
		{
			if (this.associationsMot.get(i).equals(m))
			{
				trouve = true;
			}
			
			i++;
		}
		
		return trouve;		
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
			str = str + "pere : " + this.pereMot.getLibelleMot() + "\n" ;
		}
		else
		{
			str = str + "pere : \n" ;
		}
		
		str = str + "Fils : ";
		
		
		for (int i=0;i<this.filsMot.size();i++)
		{
			str = str + this.filsMot.get(i).getLibelleMot() + " ";
		}
		
		str = str + "\n";
		
		str = str + "Synonymes : ";
		
		for (int i=0;i<this.synonymesMot.size();i++)
		{
			str = str + this.synonymesMot.get(i).getLibelleMot() + " ";
		}
		
		str = str + "\n";
		
		str = str + "Associations : ";
		
		for (int i=0;i<this.associationsMot.size();i++)
		{
			str = str + this.associationsMot.get(i).getLibelleMot() + " ";
		}
		
		str = str + "\n";
		
		return str;			
	}
	
	/**
	 * Implementation de la méthode Object equals()
	 * @param m : mot à comparer avec le mot courant
	 * @return vrai si deux mots sont egaux, faux sinon
	 */
	public boolean equals(Mot m)
	{
		return (this.libelleMot.equals(m.getLibelleMot()));
	}
	
//	
//	
//	/******************************* METHODES STATIQUES UTILISEES COMME FONCTION AUXILIAIRE *******************************/
//	/**************************** A NE PAS UTILISER A L'EXTERIEUR DE CETTE CLASSE !!!!! *********************************/
//	
	/**
	 * Permet de convertir ArrayList<Ref> en ArrayList<Mot>
	 * @param relation ArrayList<Ref>
	 * @return ArrayList de Mot
	 * @throws SQLException
	 */
	public static ArrayList<Mot> ArrayReftoArraylisteMot(ArrayList<Ref> relation) throws SQLException
	{
		ArrayList<Mot> listMot = new ArrayList<Mot>();
		
		for (int i=0;i<relation.size();i++)
		{
			oid = relation.get(i);
			listMot.add((Mot)relation.get(i).getObject());
		}
		
		return listMot;
	}
	
	/**
	 * Permet de convertir ArrayList<Mot> en ArrayList<Ref>
	 * @param a ArrayList<Mot>
	 * @return ArrayList<Ref>
	 */
	public static ArrayList<Ref> ArrayListMotToArrayRef(ArrayList<Mot> a)
	{
		ArrayList<Ref> tref = new ArrayList<Ref>();
		
		for (int i=0; i<a.size(); i++)
		{
			tref.add(a.get(i).getOid());
		}
		
		return tref;
	}
	
	/**
	 * Permet de savoir si une Ref est dans un tableau de Ref
	 * @param lr ArrayList<Ref>
	 * @param r Ref à chercher
	 * @return vrai si r trouvé, faux sinon
	 */
	public static boolean estDansArrayListRef(ArrayList<Ref> lr, Ref r)
	{
		int i=0;
		boolean trouve = false;
		
		while (i<lr.size() && !trouve)
		{
			if (lr.get(i).toString().equals(r.toString()))
			{
				trouve = true;
			}
			
			i++;
		}
		
		return trouve;
	}
	
	/**
	 * Permet de traiter les possibles doublons rencontré lors de la construction recursive du Mot
	 * @param a Array venant de la BD
	 * @param h ArrayList<Ref> qui correspond à l'historique des Mots deja trouvé
	 * @return un tableau sans doublons
	 * @throws SQLException
	 */
	public static ArrayList<Ref> traitementDesRefs(Array a, ArrayList<Ref> h) throws SQLException
	{
		ArrayList<Ref> listRef = new ArrayList<Ref>();
		
		if (a!=null)
		{
			ResultSet tableau = a.getResultSet();
			boolean trouve = false;
			
			while (tableau.next())
			{
				trouve = Mot.estDansArrayListRef(h,tableau.getRef(2));
				if (!trouve)
				{
					h.add(tableau.getRef(2));
					listRef.add(tableau.getRef(2));
				}
			}
		}
		return listRef;
	}
	
	/**
	 * Permet d'inserer les relations d'un mot 
	 * @param typeRelation type de la relation
	 * @param relations liste des references du mot
	 * @param refMot reference du mot
	 * @return vrai si l'insertion s'est bien passé, faux sinon
	 * @throws SQLException
	 */
	public static boolean insertRelations(String typeRelation, ArrayList<Ref> relations, Ref refMot) throws SQLException
	{
		int rowsAffected = 0;
		
		PreparedStatement requete = ModelDB.getDB().db.prepareStatement("UPDATE LesMots lm SET lm." + typeRelation + " = ENSMOT()  WHERE REF(lm) = ?");
		
		requete.setRef(1,refMot);
		
		rowsAffected = requete.executeUpdate();
		
		//insertion des relations
		requete = ModelDB.getDB().db.prepareStatement("INSERT INTO THE(SELECT lm."+typeRelation+" FROM LesMots lm WHERE REF(lm) = ?) VALUES ((?))");

		requete.setRef(1,refMot);
		
		for (int i=0; i<relations.size(); i++)
		{
			requete.setRef(2,relations.get(i));
			rowsAffected = rowsAffected + requete.executeUpdate();
		}
		
		requete.close();
		
		System.out.println(rowsAffected + "-"+ relations.size());
		return (rowsAffected == relations.size() + 1);
	}
}