package g39189.gameover.view;

import g39189.gameover.model.*;

import java.io.Console;

public class Display
{
    /**
     * Touches utilisées pour déplacer un joueur
     */
    public final static String MVT_KEYS = "0123";
    /**
     * Touches utilisées pour choisir une arme
     */
    public final static String WPN_KEYS = "0123";
    
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
        String template = String.format("| %8s ", room.getType());
        boolean hidden = room.isHidden();

        System.out.print(hidden ? String.format("| %8s ", " ") : template);
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
        return String.format("| %8s ", o);
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
        final String[] names = new String[Game.MAX_PLAYER];
        boolean newPlayer = true;
        Console console = System.console();

        while (newPlayer && (nbPlayers < Game.MAX_PLAYER))
        {
            String name = " ";

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

            if ((nbPlayers >= Game.MIN_PLAYER)
                    && (nbPlayers < Game.MAX_PLAYER))
            {
                String str = " ";                
                
                while(!str.matches("[ON]"))
                {
                    str = console.readLine("Créer un autre joueur ? (O/n) ");
                    str = str.toUpperCase();
                    str = (str.length() > 0) ? str : "O";
                }

                newPlayer = (str.startsWith("O"));
            }
        }

        return names;
    }

    /**
     * Demande à l’utilisateur quel mouvement il souhaite faire
     * 
     * @return le mouvement choisi par le joueur
     */
    public static Direction askMov()
    {
        Console console = System.console();
        String answer = " ";
        
        while(!answer.matches("[" + MVT_KEYS + "]"))
        {
            console.printf("Quel mouvement souhaitez-vous faire ?\n");
            answer = console.readLine(
                    "UP (%s), DOWN (%s), RIGHT (%s), LEFT (%s)\n",
                    MVT_KEYS.charAt(0), MVT_KEYS.charAt(1),
                    MVT_KEYS.charAt(2), MVT_KEYS.charAt(3));
        }

        return Direction.values()[MVT_KEYS.indexOf(answer)];
    }

    /**
     * Demande à l’utilisateur quelle arme il souhaite prendre
     * 
     * @return l’arme choisie par le joueur
     */
    public static WeaponType askWeapon()
    {
        Console console = System.console();
        String answer = " ";
        
        while(!answer.matches("[" + WPN_KEYS + "]"))
        {
            console.printf("Équipez-vous d’une arme !\n");
            answer = console.readLine(
                    "POTION (%s), ARROWS (%s), BLUDGEON (%s), GUN (%s)\n",
                    WPN_KEYS.charAt(0), WPN_KEYS.charAt(1),
                    WPN_KEYS.charAt(2), WPN_KEYS.charAt(3));
        }

        return WeaponType.values()[WPN_KEYS.indexOf(answer)];
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

        String template = String.format("| %10s : %10s \t %10s : %10s %n"
                                      + "| %10s : %10s",
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
                    pos = new DungeonPosition(row, column);
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
                    pos = new DungeonPosition(row, column);
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

        String template = String.format("| %10s : %10s", bold("Type"),
                room.getType());

        if (room.getColor() != null)
        {
            template += String.format("%n| %10s : %10s", bold("Couleur"),
                    room.getColor());
        }
        else if (room.getWeapon() != null)
        {
            template += String.format("%n| %10s : %10s", bold("Arme"),
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
     * "Nettoie" la sortie de la console
     */
    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
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
     * Affiche le nom du jeu en ASCII
     * @param skull vrai s’il faut afficher la tête de mort, faux sinon
     */
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
        
        askMov();
        // printSkull();
        // printGameOver();
        // Player player = new Player("Jean");
        // printPlayer(player);

        // WeaponType weapon = WeaponType.ARROWS;
        // printCurrPlayer(player, weapon);
    }

}
