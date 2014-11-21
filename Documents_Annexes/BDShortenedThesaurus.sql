-- LDD

Create or replace type Mot;
/

Create or replace type ensMot as table of ref Mot;
/

Create or replace type Mot as object
(libelleMot VARCHAR(50),
definitionMot VARCHAR(300),
pereMot ref Mot,
filsMot ensMot,
synonymesMot ensMot,
associationsMot ensMot);
/

Create table LesMots of Mot 
Nested table filsMot store as fils 
Nested table synonymesMot store as synonymes 
Nested table associationsMot store as associations;



-- LMD 

-- Insertion de la racine
INSERT INTO LesMots VALUES (Mot('cuisine','Ensemble des techniques de préparation des aliments',NULL,EnsMot(),EnsMot(),EnsMot()));