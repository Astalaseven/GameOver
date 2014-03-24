package g39189.gameover.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlayTest
{
    private Game game;
    private Room[] rooms = new Room[Dungeon.N];
    private DungeonPosition pos0;
    private DungeonPosition pos1;
    
    /**
     * Met en place le jeu
     * (Appelé avant chaque appel à une méthode) 
     */
    @Before
    public void setUp() throws GameOverException
    {
        game = new Game("Test0", "Test1");
        pos0 = new DungeonPosition(0, 0);
        pos1 = new DungeonPosition(1, 0);
    }
    
    /**
     * Supprime les joueurs et réinitialise le jeu
     * (Appelé à la fin de chaque méthode)
     */
    @After
    public void tearDown()
    {
        game.deletePlayers();
        game = null;        
    }
    
    /**
     * Initialise le donjon
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
     * Position en dehors du tableau
     */
    @Test(expected=GameOverException.class)
    public void constructTest0() throws GameOverException
    {
        new DungeonPosition(-1, 0);
        new DungeonPosition(5, 4);
        new DungeonPosition(0, 5);
        new DungeonPosition(4, -1);
    }

    /**
     * Le joueur tombe sur un blork avec la bonne arme
     */
    @Test
    public void playTest0() throws GameOverException
    {
        rooms[0] = new Room(RoomType.BLORK, true, WeaponType.ARROWS, null);
        initializeDungeon();

        assertTrue(game.play(Direction.DOWN, WeaponType.ARROWS));
        assertFalse(game.getDungeon().getRoom(pos0).isHidden());
        assertEquals(game.getDungeon().getRoom(pos0), rooms[0]);
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
        assertFalse(game.getDungeon().getRoom(pos0).isHidden());
        assertEquals(game.getDungeon().getRoom(pos0), rooms[0]);
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
        assertFalse(game.getDungeon().getRoom(pos0).isHidden());
        assertEquals(game.getDungeon().getRoom(pos0), rooms[0]);
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
        assertFalse(game.getDungeon().getRoom(pos0).isHidden());
        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertFalse(game.getDungeon().getRoom(pos1).isHidden());
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
        assertFalse(game.getDungeon().getRoom(pos0).isHidden());
        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertFalse(game.getDungeon().getRoom(pos1).isHidden());
        assertFalse(game.isOver());
        assertNotEquals(game.getWinner(), game.getCurrentPlayer());
    }
    
    /**
     * Trouve une clé et une princesse de sa couleur
     */
    @Test
    public void playTest5() throws GameOverException
    {
        rooms[0] = new Room(RoomType.KEY, true, null, null);
        rooms[1] = new Room(RoomType.PRINCESS, true, null, BarbarianColor.RED);
        initializeDungeon();

        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertTrue(game.isOver());
        assertEquals(game.getWinner(), game.getCurrentPlayer());
    }
    
    /**
     * Trouve une clé et une princesse de sa couleur
     */
    @Test
    public void playTest6() throws GameOverException
    {
        rooms[0] = new Room(RoomType.GATE, true, null, null);
        initializeDungeon();

        assertTrue(game.play(Direction.DOWN, WeaponType.POTION));
        assertFalse(game.isOver());
        assertEquals(game.getCurrentPlayer().getColor(), BarbarianColor.RED);
        assertEquals(game.getWinner(), null);
    }
}
