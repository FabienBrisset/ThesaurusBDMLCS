<?php
$fromName = NOM_SITE; 
$sujetMail = 'Activation de votre compte';

$bodyMail = '
<html>
	<head>
	</head>
	<body>
		<p>Merci de vous être inscrit sur ' . NOM_SITE . ' </p>
    		<p>Veuillez cliquer sur <a href="' . ABSOLUTE_ROOT . '/app/index.php?controller=User&amp;action=valider_compte&amp;hash='.$params[0].'">ce lien</a> pour activer votre compte !</p>
    	</body>
</html>';
	
?>


