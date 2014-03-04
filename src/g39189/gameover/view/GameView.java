package g39189.gameover.view;

import java.io.Console;

import g39189.gameover.model.*;


public class GameView
{
    private static String[] names = new String[Game.maxPlayer];

    public static void main(String[] args)
    {
        try 
        {
            Display.printGameOver(false);

            Console console = System.console();

            names = Display.createPlayers();

            Game game = new Game(names);

            while(!game.isOver())
            {
                Display.printGameOver(false);

                try
                {
                    Player player = game.getCurrentPlayer();

                    console.printf("%s, à vous de jouer !\n", player.getName());

                    Display.printPlayer(player);
                    Display.printDungeon(Dungeon.getInstance());

                    int dir = Display.askMov();
                    int wea = Display.askWeapon();

                    Direction direction = Direction.values()[dir];
                    WeaponType weapon = WeaponType.values()[wea];

                    if(!game.play(direction, weapon))
                    {
                        game.getCurrentPlayer();
                        game.nextPlayer();
                        throw new GameOverException("Mauvaise arme !");
                    }
                }
                catch (GameOverException e)
                {
                    continue;
                }
            }
        }
        catch (GameOverException e)
        {
            e.printStackTrace();
        }

    }

}
