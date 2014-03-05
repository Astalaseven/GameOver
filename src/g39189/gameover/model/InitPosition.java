package g39189.gameover.model;

public enum InitPosition
{
    NORD_OUEST(-1, 0),
    SUD_EST(Dungeon.N, Dungeon.N - 1),
    NORD_EST(0, Dungeon.N),
    SUD_OUEST(Dungeon.N - 1, -1);

    private final int column;
    private final int row;

    private InitPosition(int row, int column)
    {
        this.column = column;
        this.row = row;
    }

    /**
     * Retourne la valeur de column
     * 
     * @return column colonne dans le tableau
     */
    public int getColumn()
    {
        return column;
    }

    /**
     * Retourne la valeur de row
     * 
     * @return row ligne dans le tableau
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Retourne le nom de la position en fonction de sa colonne
     * 
     * @param column
     *            colonne utilisée pour retrouver le nom de l’enum
     */
    public String getName(int column)
    {
        String name = null;

        for (InitPosition pos : InitPosition.values())
        {
            name = (pos.column == column) ? pos.name() : null;
        }

        return name;
    }
}
