package g39189.gameover.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MoveTest
{
    private DungeonPosition posInit;
    private DungeonPosition expected;
    
    /**
     * Essaie de descendre
     * @throws GameOverException
     */
    @Test
    public void moveTest0() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_1;
        posInit = posInit.move(Direction.DOWN);
        expected = new DungeonPosition(0, 0);
        assertEquals(posInit.getColumn(), expected.getColumn());
        assertEquals(posInit.getRow(), expected.getRow());
    }
    
    /**
     * Essaie de monter
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest1() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_1;
        posInit.move(Direction.UP);
    }
    
    /**
     * Essaie d’aller à gauche
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest2() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_1;
        posInit.move(Direction.LEFT);
    }
    
    /**
     * Essaie d’aller à droite
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest3() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_1;
        posInit.move(Direction.RIGHT);
    }

}
