package g39189.gameover.model;

import java.util.Collections;
import java.util.LinkedList;

public class Dungeon
{
    public final static int N = 5;

    private Room[][] roomss = new Room[N][N];
    private static Dungeon instance;
    private LinkedList<Room> donjon = new LinkedList<>();

    /**
     * Constructeur de Dungeon
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
    }

    /**
     * Constructeur utilisé pour les tests
     * 
     * @param configuration
     *            un donjon personnalisé
     */
    Dungeon(Room[][] configuration)
    {
        this.roomss = configuration;
    }

    /**
     * Retourne l’instance, la crée si elle n’existe pas encore
     * 
     * @return une instance unique de dungeon
     */
    public static Dungeon getInstance()
    {
        return (instance != null) ? instance : (instance = new Dungeon());
    }

    /**
     * Retourne la valeur d’une carte
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
     * Retourne une carte pour l’afficher
     * 
     * @param pos
     *            la position de la carte à retourner
     */
    public void show(DungeonPosition pos)
    {
        roomss[pos.getRow()][pos.getColumn()].setHidden(false);
    }

    /**
     * Retourne toutes les cartes pour les cacher
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
     * Crée et ajoute les cartes dans la liste dungeon
     */
    private void populateDungeon()
    {
        addRoom(RoomType.BLORK, 16, false, true);
        addRoom(RoomType.PRINCESS, 4, true, false);
        addRoom(RoomType.KEY, 2, false, false);
        addRoom(RoomType.BLORK, 2, false, false);
        addRoom(RoomType.GATE, 1, false, false);
    }

    /**
     * Crée et ajoute une carte à la liste dungeon
     * 
     * @param type
     *            le type de carte à créer
     * @param nb
     *            le nombre de cartes à créer
     * @param colored
     *            indique si la carte à une couleur
     * @param armed
     *            indique si la carte à une arme
     */
    private void addRoom(RoomType type, int nb, boolean colored, boolean armed)
    {
        int i = 0;
        int j = 0;

        while (i < nb)
        {
            if (j > 3)
            {
                j = 0;
            }

            WeaponType weapon = armed ? WeaponType.values()[j] : null;
            BarbarianColor color = colored ? BarbarianColor.values()[j] : null;

            donjon.add(new Room(type, true, weapon, color));

            i++;
            j++;
        }
    }
}
