package g39189.gameover.model;

import g39189.gameover.view.Display;

import java.util.ArrayList;

/**
 * Cette classe représente une partie. Une partie se finit lorsqu’un joueur
 * trouve la princesse de sa couleur et une clé.
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
    private int idCurrent;
    private int idWinner;
    private boolean jokerUsed;
    private boolean keyFound;
    private DungeonPosition lastPosition;
    private boolean princessFound;
    private ArrayList<Player> players;
    private BarbarianState stateCurrent;

    /**
     * Initialise une partie.
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
        idWinner = -1;
        jokerUsed = false;
        keyFound = false;
        lastPosition = players.get(0).getInitPosition();
        princessFound = false;
        stateCurrent = BarbarianState.CONTINUE;
    }

    /**
     * Retourne l’id du joueur courant.
     * 
     * @return l’id du joueur actuel
     */
    public Player getCurrentPlayer()
    {
        return players.get(idCurrent);
    }
    
    /**
     * Retourne l’état du joueur.
     * 
     * @return l’état du joueur
     */
    public BarbarianState getCurrentState()
    {
        return stateCurrent;
    }

    /**
     * Retourne le donjon.
     * 
     * @return le plateau de jeu
     */
    public Dungeon getDungeon()
    {
        return dungeon;
    }

    /**
     * Retourne le joueur gagnant.
     * 
     * @return le joueur gagnant si la partie est finie, sinon null
     */
    public Player getWinner()
    {
        return (isOver() ? players.get(idWinner) : null);
    }

    /**
     * Indique si la partie est finie.
     * 
     * @return vrai si le joueur a trouvé une clé et sa princesse, faux sinon
     */
    public boolean isOver()
    {
        return (idWinner != -1);
    }

    /**
     * Passe au joueur suivant.
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
        stateCurrent = BarbarianState.CONTINUE;
    }

    /**
     * Exécute le coup d’un joueur.
     * 
     * @param dir
     *            la direction vers laquelle le joueur souhaite se déplacer
     * @param weapon
     *            l’arme choisie par le joueur
     * @return faux si le joueur ne peut pas vaincre la carte dévoilée, vrai
     *         sinon
     * @throws GameOverException
     *             si la partie est finie ou si la carte sur laquelle le joueur
     *             se déplace a déjà été retournée
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
            System.out.println("DEBUG : " + stateCurrent);
            throw new GameOverException("Le joueur ne peut pas jouer");
        }

        // Si la partie n’est pas finie, fait le mouvement
        DungeonPosition newPos = lastPosition.move(dir);
        System.out.println("DEBUG : " + newPos);
        Player player = players.get(idCurrent);
        Room room = dungeon.getRoom(newPos);

        if (!room.isHidden())
        {
            throw new GameOverException("Carte déjà retournée");
        }

        // Si la carte n’était pas encore retournée, la retourne
        lastPosition = newPos;
        System.out.println("DEBUG " + stateCurrent);
        dungeon.show(lastPosition);

        switch (room.getType())
        {
            case BLORK:
                // Si le joueur tombe sur un blork invincible
                if (room.getWeapon() == null)
                {
                    Display.printRoom(room);
                    stateCurrent = BarbarianState.MOVE_BLORK;
                }
                // Si le joueur n’a pas la bonne arme, il a perdu
                else if (room.getWeapon() != weapon)
                {
                    Display.printRoom(room);
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

        // Si le joueur a trouvé une clé et sa princesse, il a gagné et
        // la partie est finie
        if (keyFound && princessFound)
        {
            idWinner = idCurrent;
            stateCurrent = BarbarianState.WIN;
            Display.printEndOfGame(players.get(idWinner));
        }

        return stateCurrent;
    }
    
    private BarbarianState play(DungeonPosition pos, WeaponType weapon)
            throws GameOverException
    {
        lastPosition = pos;
        Room room = dungeon.getRoom(lastPosition);
        
        if (!room.isHidden())
        {
            throw new GameOverException("Carte déjà retournée");
        }
        
        dungeon.show(lastPosition);
        
        if ((room.getType() == RoomType.BLORK) && (room.getWeapon() == null))
        {
            stateCurrent = BarbarianState.MOVE_BLORK;
        }
        else if ((room.getType() == RoomType.BLORK) && (room.getWeapon() != weapon))
        {
            Display.printGameOver();
            Display.printSkull();
            Display.printRoom(room);
            stateCurrent = BarbarianState.GAMEOVER;
        }
        else
        {
            stateCurrent = BarbarianState.CONTINUE;
        }
        
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
        
        stateCurrent = play(pos, weapon);

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
     * Supprime tous les joueurs et remet à zéro le compteur.
     */
    protected void deletePlayers()
    {
        Player.resetN();
        players.clear();
    }

    /**
     * Mutateur prévu pour les tests.
     * 
     * @param dungeon
     *            un donjon personnalisé
     */
    protected void setDungeon(Dungeon dungeon)
    {
        this.dungeon = dungeon;
    }
}
