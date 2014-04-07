package g39189.gameover.view;

import g39189.gameover.model.Direction;
import g39189.gameover.model.Dungeon;
import g39189.gameover.model.DungeonPosition;
import g39189.gameover.model.Game;
import g39189.gameover.model.GameOverException;
import g39189.gameover.model.Player;
import g39189.gameover.model.WeaponType;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;

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
    private static Console console = System.console();
    
    public static void main(String[] args)
    {
        try
        {
            Display.printGameOver();

            String[] names = new String[4];

            if (args.length >= 1)
            {
                names = parseFile(args[0]);
            }
            else
            {
                names = Display.createPlayers();
            }

            Game game = new Game(names);
       
            console.readLine("\nTapez sur « %s » à n’importe quel moment "
                    + "pour quitter.\n(Tapez sur Enter pour commencer "
                    + "la partie)", Display.QUIT_KEY);

            while (!game.isOver())
            {
                try
                {
                    Display.printGameOver();
                    play(game);
                }
                catch (GameOverException e)
                {
                    continue;
                }
            }
            
            Display.printGameOver();
            Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());
            Display.printEndOfGame(game.getCurrentPlayer());
        }
        catch (GameOverException e)
        {
            e.getMessage();
        }
    }
    
    /**
     * Lit les 4 premières lignes du fichier passé en paramètre
     * 
     * @param fileName le fichier contenant les noms et statuts
     * des joueurs
     * @return un tableau contenant les noms des joueurs
     * et leur statut (débutant ou non)
     */
    private static String[] parseFile(String fileName)
    {
        String[] lines = new String[Game.MAX_PLAYER];
        int cpt = 0;

        try(BufferedReader stream = new BufferedReader(new FileReader(fileName)))
        {
            String line = stream.readLine();
    
            while (line != null && cpt < Game.MAX_PLAYER)
            {
                lines[cpt] = line;
                cpt++;
                line = stream.readLine();
            }
        }
        catch (IOException err)
        {
            System.out.println("Impossible d’ouvrir le fichier");
            System.exit(0);
        }

        return lines;
    }
    
    private static void play(Game game) throws GameOverException
    {
        Player player = game.getCurrentPlayer();
        DungeonPosition pos = null;
        Direction direction = null;
        WeaponType weapon = null;

        switch (game.getCurrentState())
        {
            case CONTINUE:
                // Demande à l’utilisateur quel mouvement et quelle arme
                // il souhaite choisir
                Display.printPlayer(player);
                Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());

                // Si le joueur ne sait plus bouger, il perd son tour
                if (game.getDungeon().isSurrounded(game.getLastPosition()))
                {
                    game.nextPlayer();
                    throw new GameOverException("Vous êtes bloqué et perdez votre tour !");
                }

                direction = Display.askMov();
                weapon = Display.askWeapon();
                game.play(direction, weapon);
                break;
            case GAMEOVER:
                // Passe au joueur suivant
                //Display.printSkull();
                //Display.printRoom(game.getDungeon().getRoom(game.getLastPosition()));
                Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());

                game.nextPlayer();
                throw new GameOverException("Vous avez été tué !");
            case MOVE_BLORK:
                Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());

                pos = Display.askNewPosition();
                game.playBlorkInvincible(pos);
                break;
            case BEAM_ME_UP:
                Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());

                pos = Display.askNewPosition();
                weapon = Display.askWeapon();
                game.playGate(pos, weapon);
                break;
            case JOKER:
                Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());
                System.out.println("DEBUG joker used");
                console.printf("Joker activé ! ");
                
                weapon = Display.askWeapon();
                game.playJoker(weapon);
                break;
            case READY_TO_GO:
                console.printf("%s, à vous de jouer !\n\n", player.getName());
                Display.printPlayer(player);
                Display.printDungeon(Dungeon.getInstance(), game.getCurrentState());

                direction = Display.askMov();
                weapon = Display.askWeapon();
                game.play(direction, weapon);
                break;
            default:
                throw new GameOverException("Statut inconnu");
        }
    }
}
