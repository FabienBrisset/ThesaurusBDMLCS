<?php

require_once ROOT . '/models/BD.php';

class User extends BD {


	private $id_utilisateur;
	private $nom_utilisateur;
	private $email;
	private $hash_validation;
	private $mdp_verif;
	private $avatar;
	private $mdp;

	public function __construct() {
		$this->id_utilisateur = -1;
		$this->nom_utilisateur = '';
		$this->email = '';
		$this->hash_validation = '';
		$this->mdp_verif = '';
		$this->mdp = '';
	}

	public function getMdp(){
		return $this->mdp;
	}

	public function getMdp_verif(){
		return $this->mdp_verif;
	}

	public function getId_utilisateur(){
		return $this->id_utilisateur;
	}

	public function getNom_utilisateur(){
		return $this->nom_utilisateur;
	}

	public function getHash_validation(){
		return $this->hash_validation;
	}

	public function getEmail(){
		return $this->email;
	}

	/* Retourne le mot de passer $this->mdp encrypté */
	public function getMotDePasseEncrypte(){
		return sha1($this->mdp);
	}

	public function generateHash_validation(){
		$this->hash_validation = md5(uniqid(rand(), true).$this->email); //génération d'un hashage aléatoire qui sera stocké en base pour l'utilisateur
		return $this->hash_validation;
	}

	/* Vérification de la validité nom d'utilisateur */ 
	public function validateNomUtilisateur(){
		$regexp = "/[a-zA-Z0-9_]{4,15}/";
		if(empty($this->nom_utilisateur) || (!empty($this->nom_utilisateur) && !preg_match($regexp, $this->nom_utilisateur)) ){
			return "Votre nom d'utilisateur doit comporter entre 4 et 15 caractères";
		} else{
			return 1;
		}
	}

	/* Vérification de la validité email */ 
	public function validateEmail(){
		$regexp = '`^[[:alnum:]]([-_.]?[[:alnum:]])*@[[:alnum:]]([-.]?[[:alnum:]])*\.([a-z]{2,4})$`'; 
		if( empty($this->email) || (!empty($this->email) && !preg_match($regexp, $this->email)) ){
			return "Votre adresse email n'est pas valide";
		} else{
			return 1;
		}
	}

	/* Vérification de la validité mot de passe */ 
	public function validateMotDePasse(){
		$regexp = "/[a-zA-Z0-9\W\D]{6,15}/";
		if( empty($this->mdp) || (!empty($this->mdp) && !preg_match($regexp, $this->mdp)) ){
			return "Votre mot de passe doit comporter entre 6 et 15 caractères";
		} else{
			return 1;
		}
	}

	/* Formate les variables récupérées d'un formulaire et les stocke dans $this */
	public function POSTToVar($array){
		foreach ($array as $key => $value) {
		    //Suppression des espaces en début et en fin de chaîne
		    $trimedValue = trim($value);
		    //Conversion des tags HTML par leur entité HTML
		    $this->$key = htmlspecialchars($trimedValue);
		}
	}

	/* Ajoute un nouvel utilisateur en base 
	   @return: l'identifiant de l'utilisateur ajouté
	*/
	public function addUser() {
		$sql = 'INSERT INTO user SET
			pseudo = ?,
			email = ?,
			password = ?,
			hash_validation = ?,
			date_inscription = NOW()';

		$insertUser = $this->insererValeur($sql, array($this->nom_utilisateur, $this->email, $this->getMotDePasseEncrypte(), $this->generateHash_validation()));

		return $insertUser;
	}

	/* Valide un compte utilisateur avec le numéro de hashage envoyé par email 
	   @return: vrai si le compte a été trouvé, faux sinon 
	*/
	public function valider_compte_avec_hash($hash_validation) {
		$sql = "UPDATE user SET
			hash_validation = ''
			WHERE
			hash_validation = ?";

		$activerUser = $this->executerRequete($sql, array($hash_validation));

		return ($activerUser->rowCount() == 1);
	}
	


	public function combinaison_connexion_valide() {
		$sql = "SELECT id, pseudo FROM user
			WHERE
			email = ? AND 
			password = ? AND
			hash_validation = ''";

		$connexion = $this->executerRequete($sql, array($this->email, $this->getMotDePasseEncrypte()));

		if ($result = $connexion->fetch(PDO::FETCH_ASSOC)) {
			return $result;
		}
		return false;
	}


  // Renvoie la liste des billets du blog
  public function getBillets() {
    $sql = 'select BIL_ID as id, BIL_DATE as date,'
      . ' BIL_TITRE as titre, BIL_CONTENU as contenu from T_BILLET'
      . ' order by BIL_ID desc';
    $billets = $this->executerRequete($sql);
    return $billets;
  }

  // Renvoie les informations sur un billet
  public function getBillet($idBillet) {
    $sql = 'select BIL_ID as id, BIL_DATE as date,'
      . ' BIL_TITRE as titre, BIL_CONTENU as contenu from T_BILLET'
      . ' where BIL_ID=?';
    $billet = $this->executerRequete($sql, array($idBillet));
    if ($billet->rowCount() == 1)
      return $billet->fetch();  // Accès à la première ligne de résultat
    else
      throw new Exception("Aucun billet ne correspond à l'identifiant '$idBillet'");
    }
}

?>
