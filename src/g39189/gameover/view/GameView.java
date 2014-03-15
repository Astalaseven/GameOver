package g39189.gameover.view;

import g39189.gameover.model.Direction;
import g39189.gameover.model.Dungeon;
import g39189.gameover.model.Game;
import g39189.gameover.model.GameOverException;
import g39189.gameover.model.Player;
import g39189.gameover.model.WeaponType;

import java.io.Console;

public class GameView
{
    public static void main(String[] args)
    {
        try
        {
            Display.printGameOver(false);

            Console console = System.console();

            String[] names = Display.createPlayers();

            Game game = new Game(names);

            while (!game.isOver())
            {
                Display.printGameOver(false);

                try
                {
                    Player player = game.getCurrentPlayer();

                    console.printf("%s, à vous de jouer !\n", player.getName());

                    Display.printPlayer(player);
                    Display.printDungeon(Dungeon.getInstance());

                    // Demande à l’utilisateur quel mouvement et quelle arme
                    // il souhaite choisir
                    int dir = Display.askMov();
                    int wea = Display.askWeapon();

                    Direction direction = Direction.values()[dir];
                    WeaponType weapon = WeaponType.values()[wea];

                    // Si le coup du joueur s’est mal passé,
                    // il laisse la main au suivant
                    if (!game.play(direction, weapon))
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
