<?php

require_once "Controller.php";


class ControllerAccueil extends Controller{

  private $billet;
  private $vue;

  public function __construct() {
	parent::__construct();
  }

  public function getVue(){
	return $this->vue;
  }

  // Affiche la liste de tous les billets du blog
  public function afficherAccueil() {
    $this->vue = new Vue("Accueil");

    if( !empty($this->erreurs) )
    	$this->vue->setErreurs($this->erreurs);

    $this->vue->generer(array());
  }
}

?>
