<!doctype html>
<html lang="fr">
<head>
	<meta charset="UTF-8" />
	<link rel="stylesheet" href="<?= ROOT . 'public/css/style.css'; ?>" />
	<title><?= $titre ?></title>
</head>
<body>
	<div id="global">
		<header>
			<a href="index.php"><h1 id="titrePage"><?= $titre ?></h1></a>
		</header>

		<nav>
			<ul>
				<li><a href="#">Accueil</a></li>
				<li><a href="#">Catégorie 1</a></li>
				<li><a href="#">Catégorie 2</a></li>
			</ul>
		</nav>

		<?php if(!empty($erreur)): ?>
		<div id="erreur">
			<ul>
				<?php foreach($erreur as $error): ?>
				<li class="errorEntry"><?= $error ?></li>
				<?php endforeach; ?>
			</ul>
		</div> <!-- #erreur -->
		<?php endif; ?>

		<div id="contenu">
			<?= $contenu ?>
		</div> <!-- #contenu -->

		<footer>
			Site réalisé avec PHP, HTML5 et CSS.
		</footer>
	</div> <!-- #global -->
</body>
</html>
