package g39189.gameover.view;

import g39189.gameover.model.Direction;
import g39189.gameover.model.Dungeon;
import g39189.gameover.model.DungeonPosition;
import g39189.gameover.model.Game;
import g39189.gameover.model.GameOverException;
import g39189.gameover.model.Player;
import g39189.gameover.model.WeaponType;

import java.io.Console;

/**
 * Cette classe est destinée à l’interface utilisateur.
 * Cette classe instancie le jeu. Tant que le jeu n’est pas fini,
 * c’est-à-dire qu’aucun joueur n’a trouvé la princesse de sa couleur
 * et une clé, elle demande au joueur d’introduire un mouvement et une arme.
 *
 * @author Bovyn Gatien - 39189
 */
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
       
            console.readLine("\nTapez sur %s à n’importe quel moment "
                    + "pour quitter.\n(Tapez sur Enter pour commencer "
                    + "la partie)", Display.QUIT_KEY);

            while (!game.isOver())
            {
                Display.printGameOver();

                try
                {
                    Player player = game.getCurrentPlayer();

                    console.printf("%s, à vous de jouer !\n", player.getName());

                    Display.printPlayer(player);
                    Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());

                    switch (game.getCurrentState())
                    {
                        case CONTINUE:
                            // Demande à l’utilisateur quel mouvement et quelle arme
                            // il souhaite choisir
                            Direction direction = Display.askMov();
                            WeaponType weapon = Display.askWeapon();
                            game.play(direction, weapon);
                            break;
                        case GAMEOVER:
                            game.nextPlayer();
                            throw new GameOverException("Vous avez été tué !");
                        case MOVE_BLORK:
                            Display.printGameOver();
                            Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());
                            DungeonPosition pos = Display.askNewPosition();
                            game.playBlorkInvincible(pos);
                            break;
                        case BEAM_ME_UP:
                            Display.printGameOver();
                            Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());
                            DungeonPosition pos1 = Display.askNewPosition();
                            weapon = Display.askWeapon();
                            game.playGate(pos1, weapon);
                            break;
                        case WIN:
                        case JOKER:

                        default:
                            throw new GameOverException("Statut inconnu");
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
