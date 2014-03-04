package g39189.gameover.model;

import g39189.gameover.view.Display;
import java.util.ArrayList;

public class Game
{
    public final static int minPlayer = 2;
    public final static int maxPlayer = 4;

    private Dungeon dungeon;
    private ArrayList<Player> players;
    private int idCurrent;
    private DungeonPosition lastPosition;
    private boolean keyFound;
    private boolean princessFound;
    private int idWinner;

    /** Constructeur de Game
     * @param names noms des joueurs
     * @throws GameOverException si le nb de joueurs n’est pas compris
     * entre 2 et 4 inclus.
     */
    public Game(String... names) throws GameOverException
    {
        if((names.length < minPlayer) || (names.length > maxPlayer))
        {
            throw new GameOverException("Nombre de concurrents invalide");
        }

        players = new ArrayList<Player>();
        for(String name : names)
        {
            if(name != null)
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

    /** Retourne la valeur de dungeon
     * @return le plateau de jeu
     */
    public Dungeon getDungeon()
    {
        return dungeon;
    }

    /** Retourne la valeur de idCurrent
     * @return l’id du joueur actuel
     */
    public Player getCurrentPlayer()
    {
        return players.get(idCurrent);
    }

    /** Indique si la partie est finie
     * @return booléen indiquant le statut de la partie
     */
    public boolean isOver()
    {
        return keyFound && princessFound;
    }

    /** Retourne le joueur gagnant
     * @return le joueur gagnant si la partie est finie, sinon null
     */
    public Player getWinner()
    {
        Player player = null;

        if(isOver())
        {
            player = players.get(idWinner);
        }

        return player;
    }

    /** Mutateur prévu pour les tests
     * @param dungeon
     */
    void setDungeon(Dungeon dungeon)
    {
        this.dungeon = dungeon;
    }

    /** Exécute un coup d’un joueur
     * @param dir la direction vers laquelle le joueur souhaite se déplacer
     * @param weapon l’arme choisie par le joueur
     * @return faux si le joueur ne peut pas vaincre la carte dévoilée, 
     * vrai sinon
     * @throws GameOverException si la partie est finie ou
     * si la carte sur laquelle le joueur se déplace a déjà été retournée
     */
    public boolean play(Direction dir, WeaponType weapon) throws GameOverException
    {
        if(isOver())
        {
            throw new GameOverException("La partie est finie");
        }
        // Si la partie n’est pas finie, fait le mouvement
        DungeonPosition newPos = lastPosition.move(dir);
        Player player = players.get(idCurrent);
        Room room = dungeon.getRoom(newPos);
        boolean perdu = false;

        if(!room.isHidden())
        {
            throw new GameOverException("Carte déjà retournée");
        }
        // Si la carte n’était pas encore retournée, la retourne
        dungeon.show(newPos);

        // Si le joueur n’a pas la bonne arme, il a perdu
        if((room.getType() == RoomType.BLORK) && (room.getWeapon() != weapon))
        {
            Display.printGameOver(true);
            Display.printRoom(room);
            perdu = true;
        }

        // Si la carte est la princesse de la couleur du joueur
        if(room.getColor() == player.getColor())
        {
            princessFound = true;
        }

        // Si la carte est une clé
        if(room.getType() == RoomType.KEY)
        {
            keyFound = true;
        }

        if(isOver())
        {
            idWinner = idCurrent;
        }

        lastPosition = newPos;

        return perdu;
    }

    /** Passe au joueur suivant
     *
     */
    public void nextPlayer()
    {
        ++idCurrent;

        if(idCurrent >= players.size())
        {
            idCurrent = 0;
        }

        dungeon.hideAll();
        keyFound = false;
        princessFound = false;
        lastPosition = players.get(idCurrent).getInitPosition();
    }

    public static void main(String[] args) throws GameOverException {
        //Game game = new Game("Paul", "Jean");
        //System.out.println(game.players.get(0).getId());
    }

}
