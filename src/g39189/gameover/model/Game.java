package g39189.gameover.model;

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
        players = new ArrayList<>();
        int cpt = 0;

        for (String info : names)
        {
            if (info != null)
            {
                ++cpt;
                String[] infos = info.split(" ");
                String name = infos[0];
                boolean beginner = (infos.length > 1)
                        && (infos[1].equals("débutant"));

                Player player = new Player(name, beginner);
                players.add(player);
            }
        }
        
        if ((cpt < 2) || (cpt > 4))
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
        
        if (stateCurrent != BarbarianState.CONTINUE && stateCurrent != BarbarianState.READY_TO_GO)
        {
            System.out.println("DEBUG : " + stateCurrent);
            throw new GameOverException("Le joueur ne peut pas jouer");
        }

        if (dungeon.isSurrounded(lastPosition))
        {
            nextPlayer();
            throw new GameOverException("Vous êtes bloqué et perdez votre tour !");
        }
        
        stateCurrent = BarbarianState.CONTINUE; // autre manière de le changer ?

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
                    stateCurrent = BarbarianState.MOVE_BLORK;
                }
                // Si le joueur n’a pas la bonne arme, il a perdu
                else if ((room.getWeapon() != weapon))
                {
                    if (players.get(idCurrent).isBeginner() && !jokerUsed)
                    {
                        System.out.println("DEBUG play jokerNotUsed");
                        stateCurrent = BarbarianState.JOKER;   
                    }
                    else
                    {
                        stateCurrent = BarbarianState.GAMEOVER;
                    }
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
                    System.out.println("DEBUG " + stateCurrent);
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
            //Display.printEndOfGame(players.get(idWinner));
            System.out.println("DEBUG " + stateCurrent);
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
        
        System.out.println("DEBUG play " + jokerUsed + " " + stateCurrent);
        //Display.printRoom(room);

        if ((room.getType() == RoomType.BLORK) && (room.getWeapon() == null))
        {
            dungeon.show(lastPosition);
            stateCurrent = BarbarianState.MOVE_BLORK;
        }
        else if ((room.getType() == RoomType.BLORK) && (room.getWeapon() != weapon))
        {
            System.out.println("DEBUG joker !weapon " + room.getWeapon() + " " + weapon);
            if (getCurrentPlayer().isBeginner() && !jokerUsed)
            {
                System.out.println("DEBUG joker arme " + room.getWeapon() + " " + weapon);
                stateCurrent = jokerUsed ? BarbarianState.GAMEOVER : BarbarianState.JOKER;
                System.out.println("DEBUG NOOOOOOOOOOOOOOOOOOO");
            }
            else
            {
                dungeon.show(lastPosition);
                stateCurrent = BarbarianState.GAMEOVER;
            }
        }
        else
        {
            dungeon.show(lastPosition);
            stateCurrent = BarbarianState.CONTINUE;
        }

        System.out.println("DEBUG play " + jokerUsed + " " + stateCurrent);
        
        return stateCurrent;
    }
    
    public BarbarianState playJoker(WeaponType weapon) throws GameOverException
    {
        jokerUsed = true;
        
        System.out.println("DEBUG playJoker");
        
        stateCurrent = play(lastPosition, weapon);
        
        System.out.println("DEBUG stateCurrent playJoker " + stateCurrent);
        
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
     * Retourne la dernière position.
     * 
     * @return la dernière position
     */
    public DungeonPosition getLastPosition()
    {
        return lastPosition;
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
