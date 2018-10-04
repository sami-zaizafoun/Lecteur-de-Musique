## Projet TPA ##
## Projet réalisé par ZAIZFOUN Sami et JACQUELINE Martin ##


################### Depuis Windows ###################
## Pour faciliter l'exécution, nous avons créé un fichier batch (Win_launch.bat) qui se trouve dans le dossier "livraison". ##
## Il suffit de cliquer dessus. ##

## Ce fichier contient les lignes de commandes suivantes: ##

javac -d build -cp "lib/jsoup.jar;." src/*.java
java -cp "build;lib/jsoup.jar;." src.Main

## Pour faciliter la création de la javadoc, nous avons créé un fichier batch (Win_doc.bat) qui se trouve dans le dossier "livraison". ##
## Il suffit de cliquer dessus. ##

## Ce fichier contient la ligne de commande suivante: ##

javadoc -charset utf8 -cp "lib/jsoup.jar" src/*.java -d javadoc

## Il y a possibilté de nettoyer les .class et la JavaDoc en utilisant un batch (Win_clean.bat)


################### Depuis Linux ###################
## Pour faciliter l'exécution, nous avons créé un fichier sh (Lin_launch.sh) qui se trouve dans le dossier "livraison". ##
## Il faut executer les commandes suivantes dans un terminal: ##

chmod +x Lin_launch.sh 
sh Lin_launch.sh

## Et l'application se lancera ##

## Ce fichier contient les lignes de commandes suivantes: ##

javac -d build -cp "lib/jsoup.jar:." src/*.java
java -cp "build:lib/jsoup.jar:." src.Main

## Pour faciliter la création de la javadoc, nous avons créé un fichier sh (Lin_doc.sh) qui se trouve dans le dossier "livraison". ##
chmod +x Lin_doc.sh 
sh Lin_doc.sh

## Et la javadoc se créera ##

## Ce fichier contient la ligne de commande suivante: ##

javadoc -charset utf8 -cp "lib/jsoup.jar" src/*.java -d javadoc

