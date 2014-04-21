package g39189.gameover.model;

/**
 * Cette classe représente une position dans le plateau de jeu.
 * 
 * @author Bovyn Gatien - 39189
 */
public class DungeonPosition
{
    /**
     * Position initiale du premier joueur
     */
    public final static DungeonPosition P_BARBARIAN_1;

    /**
     * Position initiale du deuxième joueur
     */
    public final static DungeonPosition P_BARBARIAN_2;

    /**
     * Position initiale du troisième joueur
     */
    public final static DungeonPosition P_BARBARIAN_3;

    /**
     * Position initiale du quatrième joueur
     */
    public final static DungeonPosition P_BARBARIAN_4;

    private int column;
    private int row;

    static
    {
        P_BARBARIAN_1 = new DungeonPosition();
        P_BARBARIAN_1.row = -1;
        P_BARBARIAN_1.column = 0;

        P_BARBARIAN_2 = new DungeonPosition();
        P_BARBARIAN_2.row = Dungeon.N;
        P_BARBARIAN_2.column = (Dungeon.N - 1);

        P_BARBARIAN_3 = new DungeonPosition();
        P_BARBARIAN_3.row = 0;
        P_BARBARIAN_3.column = Dungeon.N;

        P_BARBARIAN_4 = new DungeonPosition();
        P_BARBARIAN_4.row = (Dungeon.N - 1);
        P_BARBARIAN_4.column = -1;
    }

    /**
     * Crée une nouvelle position.
     * 
     * @param column
     *            colonne initiale
     * @param row
     *            ligne initiale
     * @throws GameOverException
     *             si la colonne ou la ligne est en dehors du plateau
     */
    public DungeonPosition(int row, int column) throws GameOverException
    {
        if ((column < 0) || (column > (Dungeon.N - 1)))
        {
            throw new GameOverException("Cette colonne n’existe pas");
        }

        if ((row < 0) || (row > (Dungeon.N - 1)))
        {
            throw new GameOverException("Cette ligne n’existe pas");
        }

        this.column = column;
        this.row = row;
    }

    /**
     * Sert à définir les constantes publiques P_BARBARIAN_*.
     */
    private DungeonPosition()
    {

    }

    /**
     * Retourne la valeur de column.
     * 
     * @return la colonne de la position
     */
    public int getColumn()
    {
        return column;
    }

    /**
     * Retourne la valeur de row.
     * 
     * @return la ligne de la position
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Avance le joueur d’une case.
     * 
     * @param dir
     *            la direction dans laquelle avancer
     * @return la nouvelle position du joueur
     * @throws GameOverException
     *             si le mouvement n’est pas possible
     */
    public DungeonPosition move(Direction dir) throws GameOverException
    {
        DungeonPosition movDir = null;

        switch (dir)
        {
            case UP:
                movDir = up();
                break;
            case DOWN:
                movDir = down();
                break;
            case RIGHT:
                movDir = right();
                break;
            case LEFT:
                movDir = left();
                break;

            default:
                throw new GameOverException("Direction inconnue");
        }

        return movDir;
    }

    /**
     * Affiche une position selon sa colonne et sa ligne.
     * 
     * @return position au format [colonne,ligne]
     */
    @Override
    public String toString()
    {
        return "[" + row + "," + column + "]";
    }

    /**
     * Retourne la position en dessous de la position actuelle.
     * 
     * @return nouvelle position située en dessous de l’actuelle
     * @throws GameOverException
     *             si la ligne du dessous n’existe pas
     */
    DungeonPosition down() throws GameOverException
    {
        DungeonPosition pos = new DungeonPosition(0, 0);

        if (((row + 1) >= Dungeon.N) || (column < 0) || (column >= Dungeon.N))
        {
            throw new GameOverException("Impossible de descendre");
        }

        pos.column = column;
        pos.row = row + 1;

        return pos;
    }

    /**
     * Retourne la position à gauche de la position actuelle.
     * 
     * @return nouvelle position située à gauche de l’actuelle
     * @throws GameOverException
     *             si la colonne à gauche n’existe pas
     */
    DungeonPosition left() throws GameOverException
    {
        DungeonPosition pos = new DungeonPosition(0, 0);

        if (((column - 1) < 0) || (row < 0) || (row >= Dungeon.N))
        {
            throw new GameOverException("Impossible d’aller à gauche");
        }

        pos.column = column - 1;
        pos.row = row;

        return pos;
    }

    /**
     * Retourne la position à droite de la position actuelle.
     * 
     * @return nouvelle position située à droite de l’actuelle
     * @throws GameOverException
     *             si la colonne à droite n’existe pas
     */
    DungeonPosition right() throws GameOverException
    {
        DungeonPosition pos = new DungeonPosition(0, 0);

        if (((column + 1) >= Dungeon.N) || (row < 0) || (row >= Dungeon.N))
        {
            throw new GameOverException("Impossible d’aller à droite");
        }

        pos.column = column + 1;
        pos.row = row;

        return pos;
    }

    /**
     * Retourne la position au-dessus de la position actuelle.
     * 
     * @return nouvelle position située au-dessus de l’actuelle
     * @throws GameOverException
     *             si la ligne du dessus n’existe pas
     */
    DungeonPosition up() throws GameOverException
    {
        DungeonPosition pos = new DungeonPosition(0, 0);

        if (((row - 1) < 0) || (column < 0) || (column >= Dungeon.N))
        {
            throw new GameOverException("Impossible de monter");
        }

        pos.column = column;
        pos.row = row - 1;

        return pos;
    }
    
    /**
     * Indique si la position est dans un coin du plateau.
     * @return vrai si la position est dans un coin, faux sinon
     */
    public boolean isCorner()
    {
        return (((column % (Dungeon.N - 1)) == 0)
                && ((row % (Dungeon.N - 1)) == 0));
    }
}
