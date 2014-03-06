package g39189.gameover.model;

public class Player
{
    private int n;
    private static int nextN;
    private String name;
    private BarbarianColor color;
    private final DungeonPosition initPosition;
    private final DungeonPosition[] POSITIONS =
        {
            DungeonPosition.P_BARBARIAN_1, DungeonPosition.P_BARBARIAN_2,
            DungeonPosition.P_BARBARIAN_3, DungeonPosition.P_BARBARIAN_4
        };


    /**
     * Crée un joueur
     * 
     * @param name
     *            nom du joueur
     */
    public Player(String name)
    {
        this.name = name;
        this.n = nextN++;
        this.color = BarbarianColor.values()[n];
        this.initPosition = POSITIONS[n];
    }

    /**
     * Retourne la valeur de name
     * 
     * @return le nom du joueur
     */
    public String getName()
    {
        return name;
    }

    /**
     * Retourne la valeur de color
     * 
     * @return la couleur du joueur
     */
    public BarbarianColor getColor()
    {
        return color;
    }

    /**
     * Retourne la valeur de initPosition
     * 
     * @return la position initiale du joueur
     */
    public DungeonPosition getInitPosition()
    {
        return initPosition;
    }

    /**
     * Retourne le numéro du joueur
     * 
     * @return le numéro du joueur
     */
    public int getId()
    {
        return n;
    }

    @Override
    public String toString()
    {
        return "Player [n=" + n + ", name=" + name + ", color=" + color
                + ", initPosition=" + initPosition + "]";
    }

    public static void main(String[] args)
    {
        Player player = new Player("Paul");
        System.out.println(player);
        Player player0 = new Player("Jean");
        System.out.println(player0);
    }

}
