package g39189.gameover.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameOverTest
{
    private Game game;
    private Room[] rooms = new Room[Dungeon.N];
    
    /**
     * Sets up the test fixture. 
     * (Called before every test case method.)
     * @throws GameOverException 
     */
    @Before
    public void setUp() throws GameOverException
    {
        game = new Game("Test0", "Test1");
    }
    
    @After
    public void tearDown()
    {
        game.deletePlayers();
        game = null;        
    }

    /**
     * Initialise le donjon
     * @throws GameOverException
     */
    private void initializeDungeon() throws GameOverException
    {
        Room[][] roomss = new Room[Dungeon.N][Dungeon.N];

        for (int i = 0; i < rooms.length; i++)
        {
            if (rooms[i] != null)
            {
                roomss[i][0] = rooms[i];
            }
        }

        Dungeon dungeon = new Dungeon(roomss);
        game.setDungeon(dungeon);
    }

    /**
     * Le joueur tombe sur un blork avec la bonne arme
     * @throws GameOverException
     */
    @Test
    public void playTest0() throws GameOverException
    {
        rooms[0] = new Room(RoomType.BLORK, true, WeaponType.ARROWS, null);
        initializeDungeon();

        assertTrue(game.play(Direction.DOWN, WeaponType.ARROWS));
    }
    
    /**
     * Le joueur n’a pas la bonne arme
     * @throws GameOverException
     */
    @Test
    public void playTest1() throws GameOverException
    {
        rooms[0] = new Room(RoomType.BLORK, true, WeaponType.ARROWS, null);
        initializeDungeon();

        assertFalse(game.play(Direction.DOWN, WeaponType.POTION));
    }
    
    /**
     * Tombe sur un blork invincible
     */
    @Test
    public void playTest2() throws GameOverException
    {
        rooms[0] = new Room(RoomType.BLORK, true, null, null);
        initializeDungeon();

        assertFalse(game.play(Direction.DOWN, WeaponType.POTION));
    }
    
    /**
     * Trouve une princesse de sa couleur puis une clé
     */
    @Test
    public void playTest3() throws GameOverException
    {
        rooms[0] = new Room(RoomType.PRINCESS, true, null, BarbarianColor.RED);
        rooms[1] = new Room(RoomType.KEY, true, null, null);
        initializeDungeon();

        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertTrue(game.isOver());
        assertEquals(game.getWinner(), game.getCurrentPlayer());
    }
    
    /**
     * Trouve une princesse pas de sa couleur puis une clé
     */
    @Test
    public void playTest4() throws GameOverException
    {
        rooms[0] = new Room(RoomType.PRINCESS, true, null, BarbarianColor.BLUE);
        rooms[1] = new Room(RoomType.KEY, true, null, null);
        initializeDungeon();

        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertFalse(game.isOver());
        assertNotEquals(game.getWinner(), game.getCurrentPlayer());
    }
}
