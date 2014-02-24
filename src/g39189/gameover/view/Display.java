package g39189.gameover.view;

import java.io.Console;

import g39189.gameover.model.*;

public class Display {
	
	/** Affiche une ligne de séparation
	 * 
	 */
	public static void printLine() {
		
		for(int i = 0; i < 56; i++) {
			System.out.print("=");
		}
		
		System.out.println();
	}
	
	/** Affiche le type de la carte retournée
	 * @param room la carte à afficher
	 */
	public static void printRoomInDungeon(Room room) {
		
		String template = String.format("| %1$8s ", room.getType());
		boolean isHidden = false;//room.isHidden();
		
		System.out.print(isHidden ? String.format("| %1$8s ", " ") : template);
	}
	
	/** Affiche des détails sur la carte retournée (arme, couleur…)
	 * @param room la carte à détailler
	 */
	public static void printRoomInDungeonDetails(Room room) {
		
		String details = null;
		
		WeaponType weapon = room.getWeapon();
		if(weapon instanceof WeaponType) {
			details = formatRoom(weapon);
		}

		BarbarianColor color = room.getColor();
		if(color instanceof BarbarianColor) {
			details = formatRoom(color);
		}

		boolean isHidden = false;//room.isHidden();
		if(isHidden || details == null) {
			details = formatRoom(" ");
		}

		System.out.print(details);

	}
	
	public static String formatRoom(Object o) {
		
		return String.format("| %1$8s ", o);

	}
	
	public static String[] createPlayers() {

		int nbPlayers = 0;
		String[] names = new String[4];
		boolean newPlayer = true;
		Console console = System.console();
		

		while((newPlayer) && (nbPlayers <= Game.getMaxPlayer() - 1)) {
			
			String name = "";
			try {
				name = console.readLine("Veuillez entrer votre nom : ");
				if(name.length() < 1) {
					throw new GameOverException("Nom trop court");
				}
			} catch (GameOverException e) {
				
				printGameOver(false);
				continue;
			}
			names[nbPlayers] = name;
			console.printf("Joueur %s créé avec succès !\n", name);
			++nbPlayers;
			
			if(nbPlayers >= Game.getMinPlayer() 
					&& nbPlayers <= Game.getMaxPlayer() - 1) {

				String str = " ";
				char answer;
				
				do {
					
					str = console.readLine("Créer un autre joueur ? (O/n) ");
					str = str.toUpperCase();
					
					answer = (str.length() > 0) ? str.charAt(0) : 'O';
					
				} while(answer != 'O' && answer != 'N');

				newPlayer = (answer == 'O');
			}
		}
		return names;
	}
	
	public static String askMov() {
		
		Console console = System.console();
		console.printf("Quel mouvement souhaitez-vous faire ?\n");
		String dir = console.readLine("UP (0), DOWN (1), " 
				+ "RIGHT (2), LEFT (3)\n");

		return dir;
	}
	
	public static String askWeapon() {
		
		Console console = System.console();
		console.printf("Équipez-vous d’une arme !\n");
		String weapon = console.readLine("POTION (0), ARROWS (1), "
				+ "BLUDGEON (2), GUN (3)\n");
	
		return weapon;
	}
	
	public static void printPlayer(Player player) {
		
		printLine();

		String template = String.format(
				"| %1$10s : %2$10s \t %3$10s : %4$10s \n"
              + "| %5$10s : %6$10s",
              bold("Nom du joueur"), player.getName(),
              bold("Couleur"),       player.getColor(),
              bold("Position actu"), player.getInitPosition());
	
		System.out.println(template);
		System.out.println(player);
		printLine();

	}
	
	public static void printDungeon(Dungeon dungeon) {
		
		printLine();
		
		for(int row = 0; row < Dungeon.N; row++) {
			
			if(row != 0) {
				System.out.print("|\n");
				printLine();
			}
			
			DungeonPosition pos = null;
			
			for(int column = 0; column < Dungeon.N; column++) {				
				
				try {
					pos = new DungeonPosition(column, row);
				} catch (GameOverException e) {
					e.printStackTrace();
				}
				
				Room room = dungeon.getRoom(pos);
				printRoomInDungeon(room);
				
			}
			
			System.out.print("|\n");
			
			for(int column = 0; column < Dungeon.N; column++) {				

				try {
					pos = new DungeonPosition(column, row);
				} catch (GameOverException e) {
					e.printStackTrace();
				}
				
				Room room = dungeon.getRoom(pos);
				printRoomInDungeonDetails(room);
				
			}

		}
		
		System.out.println("|");
		printLine();
		
	}
	
	public static String bold(String str) {
		return "\033[1m" + str + "\033[0m";
	}
	
	public static void printRoom(Room room) {
		
		printLine();
		System.out.println("| Carte retournée :");
		printLine();

		String template = String.format("| %1$10s : %2$10s", 
				bold("Type"), room.getType());
		
		// TODO fix princess display
		if(room.getColor() != null)
			template += String.format("\n| %1$10s : %2$10s", 
					bold("Couleur"), room.getColor());
		else if(room.getWeapon() != null)
			template += String.format("\n| %1$10s : %2$10s", 
					bold("Arme"), room.getWeapon());
		else if(room.getType() == RoomType.BLORK && room.getWeapon() == null)
			template += String.format(" invincible");
	
		System.out.println(template);
		printLine();
	}
	
	public static void printSkull() {
		
		System.out.println();
		
		System.out.println("\t\t             uu$$$$$$$$$$$uu             ");
		System.out.println("\t\t          uu$$$$$$$$$$$$$$$$$uu          ");
		System.out.println("\t\t         u$$$$$$$$$$$$$$$$$$$$$u         ");
		System.out.println("\t\t        u$$$$$$$$$$$$$$$$$$$$$$$u        ");
		System.out.println("\t\t       u$$$$$$$$$$$$$$$$$$$$$$$$$u       ");
		System.out.println("\t\t       u$$$$$$$$$$$$$$$$$$$$$$$$$u       ");
		System.out.println("\t\t       u$$$$$$     $$$     $$$$$$u       ");
		System.out.println("\t\t        $$$$       u$u       $$$$        ");
		System.out.println("\t\t        $$$u       u$u       u$$$        ");
		System.out.println("\t\t        $$$u      u$$$u      u$$$        ");
		System.out.println("\t\t         $$$$$uu$$$   $$$uu$$$$$         ");
		System.out.println("\t\t           $$$$$$$     $$$$$$$           ");
		System.out.println("\t\t            u$$$$$$$u$$$$$$$u            ");
		System.out.println("\t\t             u$ $ $ $ $ $ $u             ");
		System.out.println("\t\t  uuu        $$u$ $ $ $ $u$$       uuu   ");
		System.out.println("\t\t u$$$$        $$$$$u$u$u$$$       $$$$u  ");
		System.out.println("\t\t  $$$$$uu       $$$$$$$$$      uu$$$$$$  ");
		System.out.println("\t\t $$$$$$$$$$$uu    $$$$$    uu$$$$$$$$$$$u");
		System.out.println("\t\t       $$$$$$$$$$$$$$$$$$$$$$$$$$$       ");
		System.out.println("\t\t           $$$$$$$$$$$$$$$$$$$           ");
		System.out.println("\t\t          uuuu$$$$$   $$$$$$uuuu         ");
		System.out.println("\t\t  u$$$uuu$$$$$$$$       $$$$$$$$$$uuu$$$ ");
		System.out.println("\t\t  $$$$$$$$$$                $$$$$$$$$$$  ");
		System.out.println("\t\t    $$$$$                      $$$$$     ");
		System.out.println("\t\t     $$$                        $$$      ");

		System.out.println();
	}
	
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	public static void printGameOver(boolean skull) {
		
		clearScreen();
		
		System.out.println("**************************************************************************");
		System.out.println("**************************************************************************");
		System.out.println("**                                                                      **");
		System.out.println("**   #####     #    #     # #######    ####### #     # ####### ######   **");
		System.out.println("**  #     #   # #   ##   ## #          #     # #     # #       #     #  **");
		System.out.println("**  #        #   #  # # # # #          #     # #     # #       #     #  **");
		System.out.println("**  #  #### #     # #  #  # #####      #     # #     # #####   ######   **");
		System.out.println("**  #     # ####### #     # #          #     #  #   #  #       #   #    **");
		System.out.println("**  #     # #     # #     # #          #     #   # #   #       #    #   **");
		System.out.println("**   #####  #     # #     # #######    #######    #    ####### #     #  **");
		System.out.println("**                                                                      **");
		System.out.println("**************************************************************************");
		System.out.println("**************************************************************************");

		if(skull)
			printSkull();
	}
	
	public static void main(String[] args) {
		Dungeon dungeon = Dungeon.getInstance();
		printDungeon(dungeon);
		DungeonPosition pos = null;
		try {
			pos = new DungeonPosition(0, 0);
		} catch (GameOverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		printRoom(dungeon.getRoom(pos));
		System.out.println();
		printLine();
//		printGameOver();
//		Player player = new Player("Jean");
//		printPlayer(player);
		
//		WeaponType weapon = WeaponType.ARROWS;
//		printCurrPlayer(player, weapon);
	}

}
