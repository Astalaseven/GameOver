package g39189.gameover.model;

import g39189.gameover.view.Display;

import java.util.ArrayList;

/**
 * Cette classe représente une partie du jeu
 * 
 * @author Bovyn Gatien - 39189
 */
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
    private BarbarianState stateCurrent;
    private boolean jokerUsed;

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

        idCurrent = 0;
        idWinner = -1;
        keyFound = false;
        jokerUsed = false;
        princessFound = false;
        dungeon = Dungeon.getInstance();
        stateCurrent = BarbarianState.CONTINUE;
        lastPosition = players.get(0).getInitPosition();
    }

    /**
     * Retourne l’id du joueur courant
     * 
     * @return l’id du joueur actuel
     */
    public Player getCurrentPlayer()
    {
        return players.get(idCurrent);
    }

    /**
     * Retourne le donjon
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
        return (isOver() ? players.get(idWinner) : null);
    }

    /**
     * Indique si la partie est finie
     * 
     * @return vrai si le joueur a trouvé une clé et sa princesse, faux sinon
     */
    public boolean isOver()
    {
        return (idWinner != -1);
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
        jokerUsed = false;
    }

    /**
     * Exécute le coup d’un joueur
     * 
     * @param dir
     *            la direction vers laquelle le joueur souhaite se déplacer
     * @param weapon
     *            l’arme choisie par le joueur
     * @return faux si le joueur ne peut pas vaincre la carte dévoilée,
     *         vrai sinon
     * @throws GameOverException
     *             si la partie est finie ou si la carte sur laquelle
     *             le joueur se déplace a déjà été retournée
     */
    public BarbarianState play(Direction dir, WeaponType weapon)
            throws GameOverException
    {
        if (isOver())
        {
            throw new GameOverException("La partie est finie");
        }
        
        if (stateCurrent != BarbarianState.CONTINUE)
        {
            throw new GameOverException("Le joueur ne peut pas jouer");
        }

        // Si la partie n’est pas finie, fait le mouvement
        DungeonPosition newPos = lastPosition.move(dir);
        Player player = players.get(idCurrent);
        Room room = dungeon.getRoom(newPos);

        if (!room.isHidden())
        {
            throw new GameOverException("Carte déjà retournée");
        }

        // Si la carte n’était pas encore retournée, la retourne
        dungeon.show(newPos);

        switch (room.getType())
        {
            case BLORK:
                // Si le joueur tombe sur un blork invincible
                if (room.getWeapon() == null)
                {
                    stateCurrent = BarbarianState.MOVE_BLORK;
                }
                // Si le joueur n’a pas la bonne arme, il a perdu
                else if (room.getWeapon() != weapon)
                {
                    stateCurrent = BarbarianState.GAMEOVER;
                }
                break;
            case GATE:
                // Si le joueur trouve une porte, il peut rejouer
                stateCurrent = BarbarianState.BEAM_ME_UP;
                break;
            case KEY:
                // Si la carte est une clé
                keyFound = true;
                break;
            case PRINCESS:
                // Si la carte est la princesse de la couleur du joueur
                if (room.getColor() == player.getColor())
                {
                    princessFound = true;
                }
                break;
            default:
                break;
        }

        // Si le joueur tombe sur un blork invincible
        if ((room.getType() == RoomType.BLORK) && (room.getWeapon() == null))
        {
            stateCurrent = BarbarianState.MOVE_BLORK;
        }

        // Si le joueur n’a pas la bonne arme, il a perdu
        if ((room.getType() == RoomType.BLORK) && (room.getWeapon() != weapon))
        {
//            Display.printGameOver();
//            Display.printSkull();
//            Display.printRoom(room);
            stateCurrent = BarbarianState.GAMEOVER;
        }
        
        // Si le joueur trouve une porte
        if (room.getType() == RoomType.GATE)
        {
            stateCurrent = BarbarianState.BEAM_ME_UP;
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

        // Si le joueur a trouvé une clé et sa princesse, il a gagné et
        // la partie est finie
        if (keyFound && princessFound)
        {
            idWinner = idCurrent;
            stateCurrent = BarbarianState.WIN;
            Display.printEndOfGame(players.get(idWinner));
        }


//            playGate(lastPosition, weapon);
//            Display.clearScreen();
//            Display.printGameOver();
//            Display.printDungeon(dungeon);
//            lastPosition = Display.askNewPosition();
//        }

        return stateCurrent;
    }
    
    private BarbarianState play(DungeonPosition pos, WeaponType weapon)
    {
        lastPosition = pos;
        return stateCurrent;
    }
    
    public BarbarianState playJoker(WeaponType weapon)
    {
        jokerUsed = true;
        return stateCurrent;
    }
    
    public BarbarianState playGate(DungeonPosition pos, WeaponType weapon)
            throws GameOverException
    {
        if (stateCurrent != BarbarianState.BEAM_ME_UP || isOver())
        {
            throw new GameOverException("Statut incorrect ou partie terminée");
        }
        
        lastPosition = Display.askNewPosition();

        return stateCurrent;
    }
    
    public BarbarianState playBlorkInvincible(DungeonPosition pos)
            throws GameOverException
    {
        if (stateCurrent != BarbarianState.MOVE_BLORK)
        {
            throw new GameOverException("Statut incorrect");
        }
        
        if (isOver())
        {
            throw new GameOverException("La partie est terminée");
        }
        
        if (pos.isCorner())
        {
            throw new GameOverException("La nouvelle position ne peut pas "
                    + "être dans un coin");
        }
        
        dungeon.swap(lastPosition, pos);
        dungeon.show(lastPosition);
        dungeon.show(pos);
        
        stateCurrent = BarbarianState.GAMEOVER;

        return stateCurrent;
    }

    /**
     * Mutateur prévu pour les tests
     * 
     * @param dungeon
     *            un donjon personnalisé
     */
    protected void setDungeon(Dungeon dungeon)
    {
        this.dungeon = dungeon;
    }
    
    /**
     * Supprime tous les joueurs et remet à zéro le compteur
     */
    protected void deletePlayers()
    {
        Player.resetN();
        players.clear();
    }
}
