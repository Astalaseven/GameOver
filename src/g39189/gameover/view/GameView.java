package g39189.gameover.view;

import g39189.gameover.model.*;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;

/**
 * Cette classe est destinée à l’interface utilisateur.
 * Cette classe instancie le jeu. Tant que le jeu n’est pas fini,
 * c’est-à-dire qu’aucun joueur n’a trouvé la princesse de sa couleur
 * et une clé, elle demande au joueur d’introduire un mouvement
 * et une arme.
 *
 * @author Bovyn Gatien - 39189
 */
public class GameView
{
    private Game game = null;
    private Player player = null;
    private DungeonPosition pos = null;
    private Direction direction = null;
    private WeaponType weapon = null;
    
    private static Console console = System.console();
    
    /**
     * Lance une partie et interagit avec l’utilisateur.
     * 
     * @param args éventuellement un fichier contenant les noms des
     * joueurs et leur statut (débutant ou non)
     */
    public static void main(String[] args)
    {
        try
        {
            new GameView(args);
        }
        catch (GameOverException e)
        {
            System.out.println(e.getMessage());
            System.console().readLine();
        }
    }
    
    private GameView(String[] args) throws GameOverException
    {
        Display.printGameOver();
        String[] names = loadPlayers(args);
        game = new Game(names);
   
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
                System.out.print(e.getMessage());
                System.out.print("  −  Tapez sur Enter pour continuer");
                System.console().readLine();
                continue;
            }
        }
        
        Display.printGameOver();
        Display.printDungeon(game.getDungeon(), game.getCurrentState());
        Display.printEndOfGame(game.getCurrentPlayer());
    }
    
    /**
     * Lit les 4 premières lignes du fichier passé en paramètre
     * 
     * @param file le fichier contenant les noms et statuts
     * des joueurs
     * @return un tableau contenant les noms des joueurs
     * et leur statut (débutant ou non)
     */
    private String[] parseFile(String file)
    {
        String[] lines = new String[Game.MAX_PLAYER];
        int cpt = 0;

        try (BufferedReader stream = new BufferedReader(new FileReader(file)))
        {
            String line = stream.readLine();

            while ((line != null) && (cpt < Game.MAX_PLAYER))
            {
                lines[cpt] = line;
                cpt++;
                line = stream.readLine();
            }
            
            console.printf("Chargement des joueurs"
                    + " depuis le fichier « %s ».\n", file);
        }
        catch (IOException err)
        {
            System.err.println("Impossible d’ouvrir le fichier");
            System.exit(0);
        }

        return lines;
    }
    
    private String[] loadPlayers(String[] args)
    {
        String[] names;

        // Parse le fichier s’il est passé en paramètre
        if (args.length >= 1)
        {
            names = parseFile(args[0]);
        }
        // sinon propose de rentrer les noms manuellement
        else
        {
            names = Display.createPlayers();
        }
        
        return names;
    }
    
    private void play(Game game) throws GameOverException
    {
        player = game.getCurrentPlayer();

        switch (game.getCurrentState())
        {
            case CONTINUE:

                playContinue();
                break;

            case GAMEOVER:

                playGameOver();

            case MOVE_BLORK:

                playMoveBlork();
                break;

            case BEAM_ME_UP:

                playBeamMeUp();
                break;

            case JOKER:

                playJoker();
                break;

            case READY_TO_GO:

                playReadyToGo();
                break;

            default:
                throw new GameOverException("Statut inconnu");
        }
    }
    
    private void playReadyToGo() throws GameOverException
    {
        console.printf("%s, à vous de jouer !\n\n", player.getName());
        Display.printPlayer(player);
        Display.printDungeon(game.getDungeon(),
                game.getCurrentState());

        direction = Display.askMov();
        weapon = Display.askWeapon();
        game.play(direction, weapon);
    }

    private void playJoker() throws GameOverException
    {
        Display.printDungeon(game.getDungeon(),
                game.getCurrentState());
        console.printf("Joker activé ! ");

        weapon = Display.askWeapon();
        game.playJoker(weapon);        
    }

    private void playBeamMeUp() throws GameOverException
    {
        Display.printDungeon(game.getDungeon(),
                game.getCurrentState());

        pos = Display.askNewPosition();
        weapon = Display.askWeapon();
        game.playGate(pos, weapon);
    }

    private void playMoveBlork() throws GameOverException
    {
        Display.printDungeon(game.getDungeon(),
                game.getCurrentState());

        pos = Display.askNewPosition();
        game.playBlorkInvincible(pos);
    }

    private void playGameOver() throws GameOverException
    {
        Display.printDungeon(Dungeon.getInstance(),
                game.getCurrentState());

        game.nextPlayer();
        throw new GameOverException("Vous avez été tué !");
    }

    private void playContinue() throws GameOverException
    {
        Display.printPlayer(player);
        Display.printDungeon(game.getDungeon(),
                game.getCurrentState());

        // Si le joueur ne sait plus bouger, il perd son tour
        if (game.getDungeon().isSurrounded(game.getLastPosition()))
        {
            game.nextPlayer();
            throw new GameOverException("Vous êtes bloqué"
                    + " et perdez votre tour !");
        }

        direction = Display.askMov();
        weapon = Display.askWeapon();
        game.play(direction, weapon);
    }
}
