package g39189.gameover.model;

/**
 * Cette classe représente un joueur (player).
 * 
 * @author Bovyn Gatien - 39189
 */
public class Player
{
    private int n;
    private static int nextN;
    private String name;
    private BarbarianColor color;
    private boolean beginner;

    private final DungeonPosition initPosition;

    // stocke les positions initiales des joueurs
    private final DungeonPosition[] POSITIONS =
        {
            DungeonPosition.P_BARBARIAN_1, DungeonPosition.P_BARBARIAN_2,
            DungeonPosition.P_BARBARIAN_3, DungeonPosition.P_BARBARIAN_4
        };

    /**
     * Crée un joueur avec un nom, un numéro, une couleur
     * et une position initiale.
     * 
     * @param name
     *            nom du joueur
     * @throws GameOverException
     */
    public Player(String name, boolean beginner)
    {
        this.name = name;
        this.beginner = beginner;
        n = nextN++;
        color = BarbarianColor.values()[n];
        initPosition = POSITIONS[n];
    }

    /**
     * Retourne la valeur de color.
     * 
     * @return la couleur du joueur
     */
    public BarbarianColor getColor()
    {
        return color;
    }

    /**
     * Retourne le numéro du joueur.
     * 
     * @return le numéro du joueur
     */
    public int getId()
    {
        return n;
    }

    /**
     * Retourne la valeur de initPosition.
     * 
     * @return la position initiale du joueur
     */
    public DungeonPosition getInitPosition()
    {
        return initPosition;
    }

    /**
     * Retourne la valeur de name.
     * 
     * @return le nom du joueur
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Retourne la valeur de beginner
     * @return true si le joueur est débutant, faux sinon
     */
    public boolean isBeginner()
    {
        return beginner;
    }
    
    /**
     * Modifie la valeur de beginner
     */
    public void setBeginner(boolean beginner)
    {
        this.beginner = beginner;
    }
    
    /**
     * Remet le compteur de joueurs à zéro.
     */
    static void resetN()
    {
        nextN = 0;
    }
}
