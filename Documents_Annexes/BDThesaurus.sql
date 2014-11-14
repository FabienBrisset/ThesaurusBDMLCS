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

-- Insertion des termes de façon indépendante
INSERT INTO LesMots VALUES (Mot('table','instrument à plusieurs pattes',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('chaise','instrument à 4 pattes',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('assiette','support de nourriture',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('fourchette','sert à attrapper la nourriture',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('couteau','instrument tranchant',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('cuilliere','dos assez arrondi',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('verre','support pour boire',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('aliment','sert à manger',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('eau','indemodable',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('vin','trés bon alcool pour le repas du dimanche',NULL,EnsMot(),EnsMot(),EnsMot()));
INSERT INTO LesMots VALUES (Mot('cuisine','chimie generale',NULL,EnsMot(),EnsMot(),EnsMot()));

--Creation de quelques dépendances

--affectation d'un pere à "assiette"
UPDATE LesMots lm
SET lm.pereMot=(SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='table')
WHERE lm.libelleMot='assiette';

--affectation d'un pere à "aliment"
UPDATE LesMots lm
SET lm.pereMot=(SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='assiette')
WHERE lm.libelleMot='aliment';

--ajout des fils à "table"
UPDATE LesMots lm
SET lm.filsMot=(ensMot((SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='assiette')))
WHERE lm.libelleMot='table';

INSERT INTO THE(SELECT lm.filsMot FROM LesMots lm WHERE lm.libelleMot='table') VALUES ((SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='fourchette'));
INSERT INTO THE(SELECT lm.filsMot FROM LesMots lm WHERE lm.libelleMot='table') VALUES ((SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='couteau'));
INSERT INTO THE(SELECT lm.filsMot FROM LesMots lm WHERE lm.libelleMot='table') VALUES ((SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='verre'));

--ajout des fils à "verre"
UPDATE LesMots lm
SET lm.filsMot=(ensMot((SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='vin')))
WHERE lm.libelleMot='verre';

UPDATE LesMots lm
SET lm.pereMot=(SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='verre')
WHERE lm.libelleMot='vin';

--liste des fils de "verre"
SELECT value(f).libelleMot FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE lm.libelleMot='verre') f;

--liste des fils de "table"
SELECT value(f).libelleMot FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE lm.libelleMot='table') f;


--Exactement le meme principe pour synonymes et associations

UPDATE LesMots lm
SET lm.pereMot=(SELECT ref(lm1) FROM LesMots lm1 WHERE lm1.libelleMot='chaise')
WHERE lm.libelleMot='table';

SELECT value(t).libelleMot,value(t).definitionMot,value(t).pereMot,value(t).filsMot,value(t).synonymesMot,value(t).associationsMot FROM THE(SELECT lm.filsMot FROM LesMots lm WHERE lm.libelleMot='cuisine') t;
