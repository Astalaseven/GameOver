package g39189.gameover.view;

import g39189.gameover.model.*;

import java.io.Console;

/**
 * Gère les affichages et demandes à l’utilisateur.
 * 
 * @author Bovyn Gatien - 39189
 */
public class Display
{
    /**
     * Touches utilisées pour déplacer un joueur
     */
    public final static String MVT_KEYS = "8264";

    /**
     * Touches utilisées pour choisir une arme
     */
    public final static String WPN_KEYS = "1234";
    
    /**
     * Touche permettant de quitter le jeu
     */
    public final static String QUIT_KEY = "Q";

    private static Console console = System.console();
    
    private static String dungeonLine;
    
    static
    {
        dungeonLine = dungeonLine();
    }

    /**
     * Demande à l’utilisateur quel mouvement il souhaite faire.
     * 
     * @return le mouvement choisi par le joueur
     */
    public static Direction askMov()
    {
        String answer = " ";
        
        while (!answer.matches("[" + MVT_KEYS + QUIT_KEY + "]"))
        {
            console.printf("Quel mouvement souhaitez-vous faire ?\n");
            answer = console.readLine(
                      "\t    (%s)\n"       //     UP
                    + "\t (%s)   (%s)\n"   // LEFT  RIGHT
                    + "\t    (%s)\n",      //    DOWN
                    MVT_KEYS.charAt(0), MVT_KEYS.charAt(3),
                    MVT_KEYS.charAt(2), MVT_KEYS.charAt(1));
            answer = answer.toUpperCase();
        }
        
        if (answer.contains("Q"))
        {
            System.exit(0);
        }

        return Direction.values()[MVT_KEYS.indexOf(answer)];
    }

    /**
     * Demande à l’utilisateur quelle arme il souhaite prendre.
     * 
     * @return l’arme choisie par le joueur
     */
    public static WeaponType askWeapon()
    {
        String answer = " ";
        
        while (!answer.matches("[" + WPN_KEYS + QUIT_KEY + "]"))
        {
            console.printf("Équipez-vous d’une arme !\n");
            answer = console.readLine(
                    "POTION (%s), ARROWS (%s), BLUDGEON (%s), GUN (%s)\n",
                    WPN_KEYS.charAt(0), WPN_KEYS.charAt(1),
                    WPN_KEYS.charAt(2), WPN_KEYS.charAt(3));
            answer = answer.toUpperCase();
        }
        
        if (answer.contains("Q"))
        {
            System.exit(0);
        }

        return WeaponType.values()[WPN_KEYS.indexOf(answer)];
    }

    /**
     * Met le texte passé en paramètre en gras.
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
     * "Nettoie" la sortie de la console.
     */
    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
    }

    /**
     * Demande à l’utilisateur d’entrer les noms des joueurs 
     * (minimum 2, maximum 4).
     * 
     * @return un tableau contenant les noms des joueurs
     */
    public static String[] createPlayers()
    {
        int nbPlayers = 0;
        final String[] names = new String[Game.MAX_PLAYER];
        boolean newPlayer = true;

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
                printGameOver();
                continue;
            }

            names[nbPlayers] = name;
            console.printf("Joueur %s créé avec succès !\n", name.split(" ")[0]);
            ++nbPlayers;

            if ((nbPlayers >= Game.MIN_PLAYER)
                    && (nbPlayers < Game.MAX_PLAYER))
            {
                String str = " ";

                while (!str.matches("[ON]"))
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
     * Message de fin de partie, affiche quel joueur a gagné.
     * @param player
     *            le joueur qui a gagné la partie
     */
    public static void printEndOfGame(Player player)
    {
        printLine();
        System.out.println("| Fin de la partie :");
        System.out.println("| Le joueur " + player.getName() + " a gagné !");
        printLine();
    }
    
    public static void main(String[] args)
    {
        try
        {
//            printDungeon(Dungeon.getInstance(), BarbarianState.MOVE_BLORK);
//            printDungeon(Dungeon.getInstance(), null);
            
            for (int i = 1; i <= 25; i++) {
                System.out.print(i + " ");
                convertToPosition(i);
                System.out.println();
            }

        }
        catch (GameOverException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Affiche le donjon.
     * 
     * @param dungeon
     *            le donjon à afficher
     * @throws GameOverException
     *             si la position à afficher n’existe pas
     */
    public static void printDungeon(Dungeon dungeon, BarbarianState state) throws GameOverException
    {
        String template = "";
        String hidden = " ";
        int cpt = 1;
        DungeonPosition pos = null;
        
        System.out.println("DEBUG : " + state);

        // Indique la position du premier joueur
        template += String.format("%27s%n", Color.red("*"));

        for (int row = 0; row < Dungeon.N; row++)
        {
            template += dungeonLine;
            
            // Indique la position du troisième joueur
            template += (row == 4) ?
                    String.format("%18s", Color.blue("*")) : "\t";

            // Ajoute le type des cartes sur la ligne
            for (int column = 0; column < Dungeon.N; column++)
            {
                if ((state == BarbarianState.MOVE_BLORK)
                        || (state == BarbarianState.BEAM_ME_UP))
                {
                    hidden = String.valueOf(cpt++);
                }
                
                pos = new DungeonPosition(row, column);
                Room room = dungeon.getRoom(pos);

                template += String.format(" %s", "|");
                template += room.isHidden() ? String.format("%11s", hidden)
                        : String.format("%11s", room.getType());
            }

            template += " |";
            
            // Indique la position du quatrième joueur
            template += (row == 0)
                    ? String.format("%12s", Color.yellow("*")) : "";
            
            template += "\n\t";

            // Ajoute le détail des cartes sur la ligne (arme, couleur…)
            for (int column = 0; column < Dungeon.N; column++)
            {
                pos = new DungeonPosition(row, column);
                Room room = dungeon.getRoom(pos);
                
                template += String.format(" %s", "|");
                template += formatRoomInDungeonDetails(room);
            }
            
            template += " |\n";
        }
        
        template += dungeonLine;
        
        // Indique la position du deuxième joueur
        template += String.format("%79s", Color.green("*"));
        
        System.out.println(template);
    }
    
    private static String dungeonLine()
    {
        String line = String.format("%9s", " ");
        for (int i = 0; i < 66; i++)
        {
            line += "−";
        }
        return line += "\n";
    }


    /**
     * Affiche le nom du jeu en ASCII.
     */
    public static void printGameOver()
    {
        clearScreen();

        System.out.println( 
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
  + "**************************************************************************\n");
    }

    /**
     * Affiche une ligne de séparation.
     */
    public static void printLine()
    {
        String line = "";

        for (int i = 0; i < 56; i++)
        {
            line += "=";
        }

        System.out.println(line);
    }

    /**
     * Affiche la fiche signalétique d’un joueur.
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
     * Affiche la carte retournée.
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
     * Affiche un crâne en ASCII.
     */
    public static void printSkull()
    {
        System.out.println(
                "\n\t\t             uu$$$$$$$$$$$uu             \n"
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
                + "\t\t     $$$                        $$$      \n\n");
    }
    
    /**
     * Retourne des détails sur la carte retournée (arme, couleur…).
     * 
     * @param room
     *            la carte à détailler
     */
    private static String formatRoomInDungeonDetails(Room room)
    {
        String details = null;
        
        if (room.getWeapon() != null)
        {
            details = String.format("%10s", room.getWeapon());
        } 
        else if (room.getColor() != null)
        {
            details = String.format("%10s", room.getColor());
        }
        else if ((room.getType() == RoomType.BLORK)
                && (room.getWeapon() == null))
        {
            details = "INVINCIBLE";
        }

        if (room.isHidden() || (details == null))
        {
            details = " ";
        }
        
        return String.format("%11s", details);
    }
    
    private static DungeonPosition convertToPosition(int entier) throws GameOverException
    {
//        int row = (int) Math.floor(entier / 6);
//        int column = entier - (row * 5) - 1;
        --entier;
        int row = entier / 5;
        int column = entier % 5;
        
        System.out.println("DEBUG " + row + ", " + column);

        return new DungeonPosition(row, column);
    }

    public static DungeonPosition askNewPosition() throws GameOverException
    {
        String answer;
        int nb = 0;
        
        //Display.printDungeon();

        while (nb < 1 || nb > 25)
        {
            answer = "-1";

            while (!answer.matches("[0-9]{1,2}"))
            {
                answer = console.readLine("Choisissez une nouvelle position : ");
            }
            
            nb = Integer.parseInt(answer);
        }
        
        return convertToPosition(nb);
    }
}
