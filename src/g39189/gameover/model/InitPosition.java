package g39189.gameover.model;

public enum InitPosition
{
    NORD_OUEST(-1, 0), SUD_EST(5, 4), NORD_EST(0, 5), SUD_OUEST(4, -1);

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
}
