package g39189.gameover.view;

import g39189.gameover.model.BarbarianState;
import g39189.gameover.model.Direction;
import g39189.gameover.model.Dungeon;
import g39189.gameover.model.DungeonPosition;
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
            Display.printGameOver();

            Console console = System.console();

            String[] names = Display.createPlayers();

            Game game = new Game(names);

            while (!game.isOver())
            {
                Display.printGameOver();

                try
                {
                    Player player = game.getCurrentPlayer();

                    console.printf("%s, à vous de jouer !\n", player.getName());

                    Display.printPlayer(player);
                    Display.printDungeon(Dungeon.getInstance());

                    // Demande à l’utilisateur quel mouvement et quelle arme
                    // il souhaite choisir
                    Direction direction = Display.askMov();
                    WeaponType weapon = Display.askWeapon();

                    switch (game.play(direction, weapon))
                    {
                        case GAMEOVER:
                            game.nextPlayer();
                            throw new GameOverException("Mauvaise arme !");
                            break;
                        case MOVE_BLORK:
                            DungeonPosition pos = Display.askNewPosition();
                            game.playBlorkInvincible(pos);
                            break;
                        case BEAM_ME_UP:
                            DungeonPosition pos = Display.askNewPosition();
                            weapon = Display.askWeapon();
                            game.playGate(pos, weapon);
                            break;
                        case WIN:
                        case JOKER:
                        default:
                            throw new GameOverException("Statut inconnu");
                            break;
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
