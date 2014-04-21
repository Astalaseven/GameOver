package g39189.gameover.model;

/**
 * Positions initiales des joueurs
 * 
 * @author Bovyn Gatien - 39189
 */
enum InitPosition
{
    NORD_OUEST(-1, 0), SUD_EST(5, 4), NORD_EST(0, 5), SUD_OUEST(4, -1);

    final int column;
    final int row;

    private InitPosition(int row, int column)
    {
        this.column = column;
        this.row = row;
    }
}
