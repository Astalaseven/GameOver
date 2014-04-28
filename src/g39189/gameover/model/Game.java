package g39189.gameover.model;

import java.util.ArrayList;

/**
 * Cette classe représente une partie. Une partie se finit
 * lorsqu’un joueur trouve la princesse de sa couleur et une clé.
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
    private ArrayList<Player> players;
    private boolean princessFound;
    private BarbarianState stateCurrent;

    /**
     * Initialise une partie.
     * 
     * @param names
     *            noms des joueurs
     * @throws GameOverException
     *             si le nb de joueurs n’est pas compris
     *             entre 2 et 4 inclus.
     */
    public Game(String... names) throws GameOverException
    {
        players = new ArrayList<>();

        for (String info : names)
        {
            if (info == null)
            {
                break;
            }

            String[] infos = info.split(" ");
            String name = "";
            boolean beginner = false;
            int i = 0;

            for (i = 0; i < infos.length; i++)
            {
                // Remet l’espace perdu lors du split
                if ((i > 0) && (i < (infos.length - 1)))
                {
                    name += " ";
                }

                if (infos[i].equals("débutant"))
                {
                    beginner = true;
                }
                else
                {
                    name += infos[i];
                }
            }

            Player player = new Player(name, beginner);
            players.add(player);
        }

        if ((players.size() < MIN_PLAYER) || (players.size() > MAX_PLAYER))
        {
            throw new GameOverException("Nombre de concurrents invalide");
        }

        dungeon = Dungeon.getInstance();
        idCurrent = 0;
        idWinner = -1;
        jokerUsed = false;
        keyFound = false;
        lastPosition = players.get(0).getInitPosition();
        princessFound = false;
        stateCurrent = BarbarianState.READY_TO_GO;
    }

    /**
     * Retourne le joueur courant.
     * 
     * @return le joueur actuel
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
     * Retourne la dernière position.
     * 
     * @return la dernière position
     */
    public DungeonPosition getLastPosition()
    {
        return lastPosition;
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
        jokerUsed = false;
        keyFound = false;
        lastPosition = players.get(idCurrent).getInitPosition();
        princessFound = false;
        stateCurrent = BarbarianState.READY_TO_GO;
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

        if ((stateCurrent != BarbarianState.CONTINUE)
                && (stateCurrent != BarbarianState.READY_TO_GO))
        {
            throw new GameOverException("Le joueur ne peut pas jouer");
        }

        // Si la partie n’est pas finie, fait le mouvement
        DungeonPosition newPos = lastPosition.move(dir);

        Room room = dungeon.getRoom(newPos);

        if (!room.isHidden())
        {
            throw new GameOverException("Carte déjà retournée");
        }

        lastPosition = newPos;

        stateCurrent = play(lastPosition, weapon);

        // Si le joueur a trouvé une clé et sa princesse, il a gagné et
        // la partie est finie
        if (keyFound && princessFound)
        {
            idWinner = idCurrent;
            stateCurrent = BarbarianState.WIN;
        }

        return stateCurrent;
    }
    
    private BarbarianState play(DungeonPosition pos, WeaponType weapon)
            throws GameOverException
    {
        lastPosition = pos;
        Player player = players.get(idCurrent);
        Room room = dungeon.getRoom(lastPosition);

        if (!room.isHidden())
        {
            throw new GameOverException("Carte déjà retournée");
        }

        stateCurrent = BarbarianState.CONTINUE;

        switch (room.getType())
        {
            case BLORK:

                stateCurrent = playBlork(room, weapon);
                break;

            case GATE:

                stateCurrent = BarbarianState.BEAM_ME_UP;
                break;

            case KEY:

                keyFound = true;
                break;

            case PRINCESS:

                if (room.getColor() == player.getColor())
                {
                    princessFound = true;
                }

                break;

            default:
                break;
        }
        
        if (stateCurrent != BarbarianState.JOKER)
        {
            dungeon.show(lastPosition);
        }
        
        return stateCurrent;
    }
    
    private BarbarianState playBlork(Room room, WeaponType weapon)
    {
        if (room.getWeapon() == null)
        {
            stateCurrent = BarbarianState.MOVE_BLORK;
        }
        // Si le joueur n’a pas la bonne arme, il perd son tour
        else if (room.getWeapon() != weapon)
        {
            // sauf s’il a un joker
            if (players.get(idCurrent).isBeginner() && !jokerUsed)
            {
                stateCurrent = BarbarianState.JOKER;   
            }
            else
            {
                stateCurrent = BarbarianState.GAMEOVER;
            }
        }

        return stateCurrent;
    }
    
    public BarbarianState playJoker(WeaponType weapon) throws GameOverException
    {
        jokerUsed = true;
        
        return play(lastPosition, weapon);
    }
    
    public BarbarianState playGate(DungeonPosition pos, WeaponType weapon)
            throws GameOverException
    {
        if (stateCurrent != BarbarianState.BEAM_ME_UP)
        {
            throw new GameOverException("Statut incorrect, devrait être"
                    + " : BEAM_ME_UP");
        }

        if (isOver())
        {
            throw new GameOverException("La partie est terminée");
        }

        return play(pos, weapon);
    }
    
    public BarbarianState playBlorkInvincible(DungeonPosition pos)
            throws GameOverException
    {
        if (stateCurrent != BarbarianState.MOVE_BLORK)
        {
            throw new GameOverException("Statut incorrect, devrait être"
                    + " : MOVE_BLORK");
        }
        
        if (isOver())
        {
            throw new GameOverException("La partie est terminée");
        }
        
        if (pos.isCorner())
        {
            throw new GameOverException("La nouvelle position ne peut pas"
                    + " être dans un coin");
        }
        
        dungeon.swap(lastPosition, pos);
        dungeon.show(lastPosition);
        dungeon.show(pos);
        
        if (getCurrentPlayer().isBeginner() && !jokerUsed)
        {
            jokerUsed = true;
            stateCurrent = BarbarianState.CONTINUE;
        }
        else
        {
            stateCurrent = BarbarianState.GAMEOVER;
        }

        return stateCurrent;
    }

    /**
     * Supprime tous les joueurs et remet à zéro le compteur.
     */
    void deletePlayers()
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
    void setDungeon(Dungeon dungeon)
    {
        this.dungeon = dungeon;
    }
}
