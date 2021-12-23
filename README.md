# 3DGameOfLife

Auteur: Yann Trou

Sujet 4:
Le jeu de la vie est un automate cellulaire bien connu du monde informatique. Il s'agit ici de l'étendre à 3 dimensions
et de permettre de lancer des simulations en utilisant plusieurs machines afin d'accélérer les traitements et de trouver
des configurations simples.

## execution

Format du fichier de définition d'état initial:
1 cellule par ligne sous la forme X Y Z.

Pour lancer le serveur: ./server.sh

arguments disponibles:

* -nodisplay: désactive l'affichage
* -file patterns/blinker.txt : précise le fichier contenant la configuration initiale a charger
* -envsize 10 : valeur du coté de l'environnement
* -chunksize 10 : taille d'un sous-ensemble
* -cells 500 : nombre de cellules aléatoires à générer
* -alive 5 : seuil pour lequel une cellule est considérée vivante
* -current 4 : seuil pour lequel une cellule conserve son état précédent.

Pour lancer le client: ./client.sh

Pas d'arguments disponibles.