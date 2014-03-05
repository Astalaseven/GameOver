package g39189.gameover.view;

import g39189.gameover.model.Dungeon;
import g39189.gameover.model.DungeonPosition;
import g39189.gameover.model.Game;
import g39189.gameover.model.GameOverException;
import g39189.gameover.model.InitPosition;
import g39189.gameover.model.Player;
import g39189.gameover.model.Room;
import g39189.gameover.model.RoomType;

import java.io.Console;

public class Display
{
    /**
     * Affiche une ligne de séparation
     */
    public static void printLine()
    {
        for (int i = 0; i < 56; i++)
        {
            System.out.print("=");
        }

        System.out.println();
    }

    /**
     * Affiche le type de la carte retournée
     * 
     * @param room
     *            la carte à afficher
     */
    public static void printRoomInDungeon(Room room)
    {
        String template = String.format("| %1$8s ", room.getType());
        boolean hidden = room.isHidden();

        System.out.print(hidden ? String.format("| %1$8s ", " ") : template);
    }

    /**
     * Affiche des détails sur la carte retournée (arme, couleur…)
     * 
     * @param room
     *            la carte à détailler
     */
    public static void printRoomInDungeonDetails(Room room)
    {
        String details = null;

        if (room.getWeapon() != null)
        {
            details = formatRoom(room.getWeapon());
        }
        if (room.getColor() != null)
        {
            details = formatRoom(room.getColor());
        }
        if (room.isHidden() || (details == null))
        {
            details = formatRoom(" ");
        }

        System.out.print(details);
    }

    /**
     * Format d’affichage d’une carte
     * 
     * @param o
     *            une carte
     * @return la valeur de la carte formatée
     */
    private static String formatRoom(Object o)
    {
        return String.format("| %1$8s ", o);
    }

    /**
     * Demande à l’utilisateur d’entrer les noms des joueurs 
     * (minimum 2, maximum 4)
     * 
     * @return un tableau contenant les noms des joueurs
     */
    public static String[] createPlayers()
    {
        int nbPlayers = 0;
        final String[] names = new String[Game.maxPlayer];
        boolean newPlayer = true;
        Console console = System.console();

        while (newPlayer && (nbPlayers < Game.maxPlayer))
        {
            String name = "";

            try
            {
                name = console.readLine("Veuillez entrer votre nom : ");

                if (name.length() < 1)
                {
                    throw new GameOverException("Nom trop court");
                }

            }
            catch (GameOverException e)
            {
                printGameOver(false);
                continue;
            }

            names[nbPlayers] = name;
            console.printf("Joueur %s créé avec succès !\n", name);
            ++nbPlayers;

            if ((nbPlayers >= Game.minPlayer)
                    && (nbPlayers <= (Game.maxPlayer - 1)))
            {
                String str = " ";
                char answer;

                do
                {
                    str = console.readLine("Créer un autre joueur ? (O/n) ");
                    str = str.toUpperCase();

                    answer = (str.length() > 0) ? str.charAt(0) : 'O';

                } while ((answer != 'O') && (answer != 'N'));

                newPlayer = (answer == 'O');
            }
        }

        return names;
    }

    /**
     * Demande à l’utilisateur quel mouvement il souhaite faire
     * 
     * @return le mouvement sous forme d’entier
     */
    public static int askMov()
    {
        Console console = System.console();
        int answer = -1;

        while ((answer < 0) || (answer > 3))
        {
            console.printf("Quel mouvement souhaitez-vous faire ?\n");

            try
            {
                // char - 48 pour convertir l’ascii en int
                answer = console.readLine(
                        "UP (0), DOWN (1), RIGHT (2), LEFT (3)\n"
                        ).charAt(0) - 48;
            }
            catch (StringIndexOutOfBoundsException err)
            {
                continue;
            }
        }

        return answer;
    }

    /**
     * Demande à l’utilisateur quelle arme il souhaite prendre
     * 
     * @return l’arme sous forme d’entier
     */
    public static int askWeapon()
    {
        Console console = System.console();
        int answer = -1;

        while ((answer < 0) || (answer > 3))
        {
            console.printf("Équipez-vous d’une arme !\n");

            try
            {
                // char - 48 pour convertir l’ascii en int
                answer = console.readLine(
                        "POTION (0), ARROWS (1), BLUDGEON (2), GUN (3)\n"
                        ).charAt(0) - 48;

            }
            catch (StringIndexOutOfBoundsException err)
            {
                continue;
            }
        }

        return answer;
    }

    /**
     * Affiche la fiche signalétique d’un joueur
     * 
     * @param player
     *            le joueur à afficher
     */
    public static void printPlayer(Player player)
    {
        printLine();

        String template = String.format("| %1$10s : %2$10s \t %3$10s : %4$10s %n"
                                      + "| %5$10s : %6$10s", 
                 bold("Nom du joueur"), player.getName(), 
                 bold("Couleur"), player.getColor(),
                 bold("Position init"), InitPosition.values()[player.getId()]);

        System.out.println(template);
        printLine();
    }

    /**
     * Affiche le donjon
     * 
     * @param dungeon
     *            le donjon à afficher
     */
    public static void printDungeon(Dungeon dungeon)
    {
        printLine();

        for (int row = 0; row < Dungeon.N; row++)
        {
            if (row != 0)
            {
                System.out.println("|");
                printLine();
            }

            DungeonPosition pos = null;

            for (int column = 0; column < Dungeon.N; column++)
            {
                try
                {
                    pos = new DungeonPosition(column, row);
                }
                catch (GameOverException e)
                {
                    e.printStackTrace();
                }

                Room room = dungeon.getRoom(pos);
                printRoomInDungeon(room);
            }

            System.out.println("|");

            for (int column = 0; column < Dungeon.N; column++)
            {
                try
                {
                    pos = new DungeonPosition(column, row);
                }
                catch (GameOverException e)
                {
                    e.printStackTrace();
                }

                Room room = dungeon.getRoom(pos);
                printRoomInDungeonDetails(room);
            }
        }

        System.out.println("|");
        printLine();
    }

    /**
     * Met le texte passé en paramètre en gras
     * 
     * @param str
     *            le texte à mettre en gras
     * @return le texte mis en gras
     */
    public static String bold(String str)
    {
        return "\033[1m" + str + "\033[0m";
    }

    /**
     * Affiche la carte retournée
     * 
     * @param room
     *            la carte passée en paramètre
     */
    public static void printRoom(Room room)
    {
        printLine();
        System.out.println("| Carte retournée :");
        printLine();

        String template = String.format("| %1$10s : %2$10s", bold("Type"),
                room.getType());

        // TODO fix princess display
        if (room.getColor() != null)
        {
            template += String.format("%n| %1$10s : %2$10s", bold("Couleur"),
                    room.getColor());
        }
        else if (room.getWeapon() != null)
        {
            template += String.format("%n| %1$10s : %2$10s", bold("Arme"),
                    room.getWeapon());
        }
        else if ((room.getType() == RoomType.BLORK)
                && (room.getWeapon() == null))
        {
            template += String.format(" invincible");
        }

        System.out.println(template);
        printLine();
    }

    /**
     * Affiche un crâne en ASCII
     */
    public static void printSkull()
    {
        System.out.println();

        String skull = 
                  "\t\t             uu$$$$$$$$$$$uu             \n"
                + "\t\t          uu$$$$$$$$$$$$$$$$$uu          \n"
                + "\t\t         u$$$$$$$$$$$$$$$$$$$$$u         \n"
                + "\t\t        u$$$$$$$$$$$$$$$$$$$$$$$u        \n"
                + "\t\t       u$$$$$$$$$$$$$$$$$$$$$$$$$u       \n"
                + "\t\t       u$$$$$$$$$$$$$$$$$$$$$$$$$u       \n"
                + "\t\t       u$$$$$$     $$$     $$$$$$u       \n"
                + "\t\t        $$$$       u$u       $$$$        \n"
                + "\t\t        $$$u       u$u       u$$$        \n"
                + "\t\t        $$$u      u$$$u      u$$$        \n"
                + "\t\t         $$$$$uu$$$   $$$uu$$$$$         \n"
                + "\t\t           $$$$$$$     $$$$$$$           \n"
                + "\t\t            u$$$$$$$u$$$$$$$u            \n"
                + "\t\t             u$ $ $ $ $ $ $u             \n"
                + "\t\t  uuu        $$u$ $ $ $ $u$$       uuu   \n"
                + "\t\t u$$$$        $$$$$u$u$u$$$       $$$$u  \n"
                + "\t\t  $$$$$uu       $$$$$$$$$      uu$$$$$$  \n"
                + "\t\t $$$$$$$$$$$uu    $$$$$    uu$$$$$$$$$$$ \n"
                + "\t\t       $$$$$$$$$$$$$$$$$$$$$$$$$$$       \n"
                + "\t\t           $$$$$$$$$$$$$$$$$$$           \n"
                + "\t\t          uuuu$$$$$   $$$$$$uuuu         \n"
                + "\t\t $$$uuu$$$$$$$$$$       $$$$$$$$$$uuu$$$ \n"
                + "\t\t  $$$$$$$$$$                $$$$$$$$$$$  \n"
                + "\t\t    $$$$$                      $$$$$     \n"
                + "\t\t     $$$                        $$$      \n";

        System.out.println(skull);
        System.out.println();
    }

    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printGameOver(boolean skull)
    {
        clearScreen();

        String gameover = 
                  "**************************************************************************\n"
                + "**************************************************************************\n"
                + "**                                                                      **\n"
                + "**   #####     #    #     # #######    ####### #     # ####### ######   **\n"
                + "**  #     #   # #   ##   ## #          #     # #     # #       #     #  **\n"
                + "**  #        #   #  # # # # #          #     # #     # #       #     #  **\n"
                + "**  #  #### #     # #  #  # #####      #     # #     # #####   ######   **\n"
                + "**  #     # ####### #     # #          #     #  #   #  #       #   #    **\n"
                + "**  #     # #     # #     # #          #     #   # #   #       #    #   **\n"
                + "**   #####  #     # #     # #######    #######    #    ####### #     #  **\n"
                + "**                                                                      **\n"
                + "**************************************************************************\n"
                + "**************************************************************************\n";

        System.out.println(gameover);

        if (skull)
        {
            printSkull();
        }
    }

    public static void main(String[] args)
    {

        final Dungeon dungeon = Dungeon.getInstance();
        printDungeon(dungeon);
        DungeonPosition pos = null;

        try
        {

            pos = new DungeonPosition(0, 0);

        }
        catch (GameOverException e)
        {

            e.printStackTrace();
        }

        printRoom(dungeon.getRoom(pos));
        System.out.println();
        printLine();
        // printSkull();
        // printGameOver();
        // Player player = new Player("Jean");
        // printPlayer(player);

        // WeaponType weapon = WeaponType.ARROWS;
        // printCurrPlayer(player, weapon);
    }

}
