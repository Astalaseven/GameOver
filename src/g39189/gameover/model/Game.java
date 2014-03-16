package g39189.gameover.model;

import g39189.gameover.view.Display;

import java.util.ArrayList;

public class Game
{
    /**
     * Nombre minimal de joueurs nécessaires
     */
    public final static int MIN_PLAYER = 2;
    /**
     * Nombre maximal de joueurs nécessaires
     */
    public final static int MAX_PLAYER = 4;

    private Dungeon dungeon;
    private ArrayList<Player> players;
    private int idCurrent;
    private int idWinner;
    private DungeonPosition lastPosition;
    private boolean keyFound;
    private boolean princessFound;

    /**
     * Constructeur de Game
     * 
     * @param names
     *            noms des joueurs
     * @throws GameOverException
     *             si le nb de joueurs n’est pas compris entre 2 et 4 inclus.
     */
    public Game(String... names) throws GameOverException
    {
        if ((names.length < MIN_PLAYER) || (names.length > MAX_PLAYER))
        {
            throw new GameOverException("Nombre de concurrents invalide");
        }

        players = new ArrayList<>();

        for (String name : names)
        {
            if (name != null)
            {
                Player player = new Player(name);
                players.add(player);
            }
        }

        dungeon = Dungeon.getInstance();
        idCurrent = 0;
        lastPosition = players.get(0).getInitPosition();
        keyFound = false;
        princessFound = false;
        idWinner = -1;
    }

    /**
     * Retourne la valeur de idCurrent
     * 
     * @return l’id du joueur actuel
     */
    public Player getCurrentPlayer()
    {
        return players.get(idCurrent);
    }

    /**
     * Retourne la valeur de dungeon
     * 
     * @return le plateau de jeu
     */
    public Dungeon getDungeon()
    {
        return dungeon;
    }

    /**
     * Retourne le joueur gagnant
     * 
     * @return le joueur gagnant si la partie est finie, sinon null
     */
    public Player getWinner()
    {
        Player player = null;

        if (isOver())
        {
            player = players.get(idWinner);
        }

        return player;
    }

    /**
     * Indique si la partie est finie
     * 
     * @return vrai si le joueur a trouvé une clé et sa princesse, faux sinon
     */
    public boolean isOver()
    {
        return keyFound && princessFound;
    }

    /**
     * Passe au joueur suivant
     */
    public void nextPlayer()
    {
        ++idCurrent;

        if (idCurrent >= players.size())
        {
            idCurrent = 0;
        }

        dungeon.hideAll();
        keyFound = false;
        princessFound = false;
        lastPosition = players.get(idCurrent).getInitPosition();
    }

    /**
     * Exécute un coup d’un joueur
     * 
     * @param dir
     *            la direction vers laquelle le joueur souhaite se déplacer
     * @param wea
     *            l’arme choisie par le joueur
     * @return faux si le joueur ne peut pas vaincre la carte dévoilée,
     *         vrai sinon
     * @throws GameOverException
     *             si la partie est finie ou si la carte sur laquelle le joueur
     *             se déplace a déjà été retournée
     */
    public boolean play(Direction dir, WeaponType wea) throws GameOverException
    {
        if (isOver())
        {
            throw new GameOverException("La partie est finie");
        }
        // Si la partie n’est pas finie, fait le mouvement
        DungeonPosition newPos = lastPosition.move(dir);
        Player player = players.get(idCurrent);
        Room room = dungeon.getRoom(newPos);
        boolean rejoue = true;

        if (!room.isHidden())
        {
            throw new GameOverException("Carte déjà retournée");
        }
        // Si la carte n’était pas encore retournée, la retourne
        dungeon.show(newPos);

        // Si le joueur n’a pas la bonne arme, il a perdu
        if ((room.getType() == RoomType.BLORK) && (room.getWeapon() != wea))
        {
            Display.printGameOver(true);
            Display.printRoom(room);
            rejoue = false;
        }

        // Si la carte est la princesse de la couleur du joueur
        if (room.getColor() == player.getColor())
        {
            princessFound = true;
        }

        // Si la carte est une clé
        if (room.getType() == RoomType.KEY)
        {
            keyFound = true;
        }

        if (isOver())
        {
            idWinner = idCurrent;
            
            // TODO
            System.out.println("Fin de la partie :");
            System.out.println("Le joueur " + players.get(idWinner).getName() + " a gagné !");
        }

        lastPosition = newPos;

        return rejoue;
    }

    /**
     * Mutateur prévu pour les tests
     * 
     * @param dungeon
     */
    protected void setDungeon(Dungeon dungeon)
    {
        this.dungeon = dungeon;
    }
}
