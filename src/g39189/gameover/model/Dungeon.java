package g39189.gameover.model;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Cette classe représente le donjon, c'est-à-dire le plateau de jeu.
 * 
 * @author Bovyn Gatien - 39189
 */
public class Dungeon
{
    /**
     * Taille du donjon
     */
    public final static int N = 5;

    private static Dungeon instance;
    private LinkedList<Room> donjon = new LinkedList<>();
    private Room[][] roomss = new Room[N][N];

    /**
     * Remplit la liste donjon avec les cartes et les place sur le plateau.
     */
    private Dungeon()
    {
        populateDungeon();
        Collections.shuffle(donjon);

        for (int row = 0; row < N; row++)
        {
            for (int column = 0; column < N; column++)
            {
                roomss[row][column] = donjon.pop();
            }
        }
        
        // Debug
        roomss[0][0] = new Room(RoomType.GATE, true, null, null);
        roomss[0][1] = new Room(RoomType.KEY, true, null, null);
        roomss[1][1] = new Room(RoomType.KEY, true, null, null);
        roomss[2][1] = new Room(RoomType.KEY, true, null, null);
        roomss[2][0] = new Room(RoomType.KEY, true, null, null);
        roomss[1][0] = new Room(RoomType.GATE, true, null, null);
        roomss[4][4] = new Room(RoomType.BLORK, true, null, null);
        roomss[0][4] = new Room(RoomType.BLORK, true, WeaponType.BLUDGEON, null);
        roomss[3][0] = new Room(RoomType.KEY, true, null, null);
        roomss[4][0] = new Room(RoomType.PRINCESS, true, null, BarbarianColor.RED);
        roomss[4][1] = new Room(RoomType.KEY, true, null, null);
        roomss[3][1] = new Room(RoomType.KEY, true, null, null);
    }

    /**
     * Construit un donjon avec le plateau passé en paramètre.
     * 
     * @param configuration
     *            un donjon personnalisé
     */
    Dungeon(Room[][] configuration)
    {
        roomss = configuration;
    }
    
    /**
     * Retourne l’instance, la crée si elle n’existe pas encore.
     * 
     * @return une instance unique de dungeon
     */
    public static Dungeon getInstance()
    {
        if (instance == null)
        {
            instance = new Dungeon();
        }

        return instance;
    }

    /**
     * Retourne la valeur d’une carte sur base d’une position.
     * 
     * @param pos
     *            la position de la carte dans le donjon
     * @return la valeur d’une carte
     */
    public Room getRoom(DungeonPosition pos)
    {
        return roomss[pos.getRow()][pos.getColumn()];
    }

    /**
     * Retourne toutes les cartes pour les cacher.
     */
    public void hideAll()
    {
        for (int row = 0; row < N; row++)
        {
            for (int column = 0; column < N; column++)
            {
                roomss[row][column].setHidden(true);
            }
        }
    }

    /**
     * Retourne une carte pour l’afficher.
     * 
     * @param pos
     *            la position de la carte à retourner
     */
    public void show(DungeonPosition pos)
    {
        getRoom(pos).setHidden(false);
    }

    /**
     * Permet d’échanger 2 positions.
     * 
     * @param pos0 la position initiale
     * @param pos1 la nouvelle position
     */
    public void swap(DungeonPosition pos0, DungeonPosition pos1)
    {
        Room roomTmp = getRoom(pos0);

        roomss[pos0.getRow()][pos0.getColumn()] = getRoom(pos1);
        roomss[pos1.getRow()][pos1.getColumn()] = roomTmp;
    }
    
    /**
     * Indique si une pièce est entourée de pièces toutes visibles.
     */
    public boolean isSurrounded(DungeonPosition pos) throws GameOverException
    {
        boolean posUp = false;
        boolean posDown = false;
        boolean posLeft = false;
        boolean posRight = false;

        // Si la position est dans le tableau, vérifie si elle est entourée de cartes cachées
        if (((pos.getRow() - 1) >= 0) && (pos.getColumn() >= 0) && (pos.getColumn() < N))
        {
            posUp = getRoom(pos.up()).isHidden();
        }
        if (((pos.getRow() + 1) < N) && (pos.getColumn() >= 0) && (pos.getColumn() < N))
        {
            posDown = getRoom(pos.down()).isHidden();
        }
        if (((pos.getColumn() - 1) >= 0) && (pos.getRow() >= 0) && (pos.getRow() < N))
        {
            posLeft = getRoom(pos.left()).isHidden();
        }
        if (((pos.getColumn() + 1) < N) && (pos.getRow() >= 0) && (pos.getRow() < N))
        {
            posRight = getRoom(pos.right()).isHidden();
        }

        return (!posUp && !posDown && !posLeft && !posRight);
    }

    /**
     * Crée et ajoute un nombre de cartes passé en paramètre à la liste donjon.
     * 
     * @param type
     *            le type de carte à créer
     * @param nb
     *            le nombre de cartes à créer
     * @param colored
     *            indique si la carte a une couleur
     * @param armed
     *            indique si la carte a une arme
     */
    private void addRoom(RoomType type, int nb, boolean colored, boolean armed)
    {
        int j = 0;

        for (int i = 0; i < nb; i++)
        {
            WeaponType weapon = armed ? WeaponType.values()[j] : null;
            BarbarianColor color = colored ? BarbarianColor.values()[j] : null;

            donjon.add(new Room(type, true, weapon, color));

            j = (j + 1) % 4;
        }
    }

    /**
     * Crée et ajoute les cartes dans la liste donjon.
     */
    private void populateDungeon()
    {
        addRoom(RoomType.BLORK, 16, false, true);
        addRoom(RoomType.PRINCESS, 4, true, false);
        addRoom(RoomType.KEY, 2, false, false);
        addRoom(RoomType.BLORK, 2, false, false);
        addRoom(RoomType.GATE, 1, false, false);
    }
}
