# knndann
UNIVERSITE DE CAEN, MASTER 1 INFORMATIQUE
HUET Bryan, RECH Florian

MODELE DE CLASSIFICATION DANN

Pour compiler l'application il faut utiliser le logiciel ant
  $ ant -Darg0="chemin vers le fichier des data" -Darg1="liste des voisins testés par le model" -Darg2="nombre segment k-fold" 

Exemple de commande : 
  $ ant -Darg0=data/iris.data -Darg1=1 10 25 50 -Darg2=10 
