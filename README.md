GameOver
========

Projet de Java basé sur le jeu _Game Over_ (jeu de cartes/mémorisation pour 2 à 4 joueurs).

Pour les règles, voir [ici](http://www.lahauteroche.eu/gameover/gameover_home.html).

Installation
---------

    cd ~
    git clone https://github.com/Astalaseven/GameOver.git


Classpath
---------

    CLASSPATH=$CLASSPATH:~/GameOver/bin/

Compilation
---------

    javac -O -g:none -d ~/GameOver/bin ~/GameOver/src/g39189/gameover/*/*.java

Exécution
---------

    java g39189.gameover.view.GameView
