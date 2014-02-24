package g39189.gameover.view;

import java.io.Console;

import g39189.gameover.model.*;


public class GameView {

	public static String[] names = new String[4];
	
	public static void main(String[] args) {
		
		try {
			
			Display.printGameOver(false);
			
			Console console = System.console();
			
			names = Display.createPlayers();

			Game game = new Game(names);
			
			while(!game.isOver()) {
				
				Display.printGameOver(false);
				
				try {
					console.printf("%s, à vous de jouer !\n",
							game.getCurrentPlayer().getName());
					
					Display.printPlayer(game.getCurrentPlayer());
					
					String dir = Display.askMov();
					String wea = Display.askWeapon();
					
					Direction direction = Direction.values()[Integer.parseInt(dir)];
					WeaponType weapon = WeaponType.values()[Integer.parseInt(wea)];
					
					game.play(direction, weapon);
					
				} catch (GameOverException e) {

					game.getCurrentPlayer();
					game.nextPlayer();
					continue;

				}
				
			}
		} catch (GameOverException e) {
			e.printStackTrace();
		}

	}

}
