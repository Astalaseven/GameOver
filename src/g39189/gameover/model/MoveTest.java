package g39189.gameover.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MoveTest
{
    private DungeonPosition posInit;
    
    /**
     * Essaie de descendre depuis P_BARBARIAN_1
     * @throws GameOverException
     */
    @Test
    public void moveTest0() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_1;
        posInit = posInit.move(Direction.DOWN);

        assertEquals(posInit.getColumn(), 0);
        assertEquals(posInit.getRow(), 0);
    }
    
    /**
     * Essaie de monter depuis P_BARBARIAN_1
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest1() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_1;
        posInit.move(Direction.UP);
    }
    
    /**
     * Essaie d’aller à gauche depuis P_BARBARIAN_1
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest2() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_1;
        posInit.move(Direction.LEFT);
    }
    
    /**
     * Essaie d’aller à droite depuis P_BARBARIAN_1
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest3() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_1;
        posInit.move(Direction.RIGHT);
    }
    
    /**
     * Essaie d’aller à droite depuis (0,0)
     * @throws GameOverException
     */
    @Test
    public void moveTest4() throws GameOverException
    {
        posInit = new DungeonPosition(0, 0);
        posInit = posInit.move(Direction.RIGHT);
        
        assertEquals(posInit.getColumn(), 1);
        assertEquals(posInit.getRow(), 0);
    }

    /**
     * Essaie d’aller à gauche depuis (0,0)
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest5() throws GameOverException
    {
        posInit = new DungeonPosition(0, 0);
        posInit.move(Direction.LEFT);
    }
    
    /**
     * Essaie de monter depuis (0,0)
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest6() throws GameOverException
    {
        posInit = new DungeonPosition(0, 0);
        posInit.move(Direction.UP);
    }
    
    /**
     * Essaie de descendre depuis (0,0)
     * @throws GameOverException
     */
    @Test
    public void moveTest7() throws GameOverException
    {
        posInit = new DungeonPosition(0, 0);
        posInit = posInit.move(Direction.DOWN);
        
        assertEquals(posInit.getColumn(), 0);
        assertEquals(posInit.getRow(), 1);
    }
    
    /**
     * Essaie de descendre depuis P_BARBARIAN_2
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest8() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_2;
        posInit = posInit.move(Direction.DOWN);
    }
    
    /**
     * Essaie de monter depuis P_BARBARIAN_2
     * @throws GameOverException
     */
    @Test
    public void moveTest9() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_2;
        posInit = posInit.move(Direction.UP);
        
        assertEquals(posInit.getColumn(), 4);
        assertEquals(posInit.getRow(), 4);
    }
    
    /**
     * Essaie d’aller à gauche depuis P_BARBARIAN_2
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest10() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_2;
        posInit.move(Direction.LEFT);
    }
    
    /**
     * Essaie d’aller à droite depuis P_BARBARIAN_2
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest11() throws GameOverException
    {
        posInit = DungeonPosition.P_BARBARIAN_2;
        posInit.move(Direction.RIGHT);
    }
    
    /**
     * Essaie d’aller à droite depuis (4,4)
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest12() throws GameOverException
    {
        posInit = new DungeonPosition(4, 4);
        posInit.move(Direction.RIGHT);
    }

    /**
     * Essaie d’aller à gauche depuis (4,4)
     * @throws GameOverException
     */
    @Test
    public void moveTest13() throws GameOverException
    {
        posInit = new DungeonPosition(4, 4);
        posInit = posInit.move(Direction.LEFT);
        
        assertEquals(posInit.getColumn(), 3);
        assertEquals(posInit.getRow(), 4);
    }
    
    /**
     * Essaie de monter depuis (4,4)
     * @throws GameOverException
     */
    @Test
    public void moveTest14() throws GameOverException
    {
        posInit = new DungeonPosition(4, 4);
        posInit = posInit.move(Direction.UP);
        
        assertEquals(posInit.getColumn(), 4);
        assertEquals(posInit.getRow(), 3);
    }
    
    /**
     * Essaie de descendre depuis (4,4)
     * @throws GameOverException
     */
    @Test(expected=GameOverException.class)
    public void moveTest15() throws GameOverException
    {
        posInit = new DungeonPosition(4, 4);
        posInit.move(Direction.DOWN);
    }
}
