package g39189.gameover.model;

public class DungeonPosition {
	
	private int column;
	private int row;
	final static DungeonPosition P_BARBARIAN_1;
	final static DungeonPosition P_BARBARIAN_2;
	final static DungeonPosition P_BARBARIAN_3;
	final static DungeonPosition P_BARBARIAN_4;
	private static int dungeonCpt = 0;
	
	static {
		P_BARBARIAN_1 = new DungeonPosition();
		P_BARBARIAN_2 = new DungeonPosition();
		P_BARBARIAN_3 = new DungeonPosition();
		P_BARBARIAN_4 = new DungeonPosition();
	}
	
	/** Constructeur de DungeonPosition
	 * @param column colonne initiale
	 * @param row ligne initiale
	 * @throws GameOverException si la colonne ou la ligne est en dehors
	 * du plateau
	 */
	public DungeonPosition(int column, int row) throws GameOverException {

		if(column < 0 || column > Dungeon.N-1) {
			throw new GameOverException("Cette colonne n’existe pas");
		}
		
		if(row < 0 || row > Dungeon.N-1) {
			throw new GameOverException("Cette ligne n’existe pas");
		}
		
		this.column = column;
		this.row = row;

	}

	/** Constructeur de DungeonPosition
	 * 
	 */
	private DungeonPosition() {

		InitPosition init = InitPosition.values()[dungeonCpt];
		column = init.getColumn();
		row = init.getRow();
		++dungeonCpt;

	}

	/** Affiche une position selon sa colonne et sa ligne
	 * @return position au format [colonne,ligne]
	 */
	@Override
	public String toString() {
		return "[" + column + "," + row + "]";
	}

	/** Retourne la valeur de column
	 * @return la colonne de la position
	 */
	public int getColumn() {
		return column;
	}

	/** Retourne la valeur de row
	 * @return la ligne de la position
	 */
	public int getRow() {
		return row;
	}
	
	/** Retourne la position au-dessus de la position actuelle
	 * @return nouvelle position située au-dessus de l’actuelle
	 * @throws GameOverException si la ligne du dessus n’existe pas
	 */
	public DungeonPosition up() throws GameOverException {
		
		DungeonPosition pos = new DungeonPosition(0, 0);

		if(row - 1 < 0 || column < 0 || column > Dungeon.N - 1) {
			throw new GameOverException("Impossible de monter");
		}
		
		pos.column = column;
		pos.row = --row;

		return pos;

	}
	
	/** Retourne la position en dessous de la position actuelle
	 * @return nouvelle position située en dessous de l’actuelle
	 * @throws GameOverException si la ligne du dessous n’existe pas
	 */
	public DungeonPosition down() throws GameOverException {
		DungeonPosition pos = new DungeonPosition(0, 0);

		if(row + 1 > Dungeon.N - 1 || column < 0 || column > Dungeon.N - 1) {
			throw new GameOverException("Impossible de descendre");
		}
		pos.column = column;
		pos.row = ++row;

		return pos;
	}
	
	/** Retourne la position à droite de de la position actuelle
	 * @return nouvelle position située à droite de l’actuelle
	 * @throws GameOverException si la colonne à droite n’existe pas
	 */
	public DungeonPosition right() throws GameOverException {
		DungeonPosition pos = new DungeonPosition(0, 0);

		if(column + 1 > Dungeon.N - 1 || row < 0 || row > Dungeon.N - 1) {
			throw new GameOverException("Impossible d’aller à droite");
		}
		pos.column = ++column;
		pos.row = row;

		return pos;
	}
	
	/** Retourne la position à gauche de la position actuelle
	 * @return nouvelle position située à gauche de l’actuelle
	 * @throws GameOverException si la colonne à gauche n’existe pas
	 */
	public DungeonPosition left() throws GameOverException {
		DungeonPosition pos = new DungeonPosition(0, 0);

		if(column - 1 < 0 || row < 0 || row > Dungeon.N - 1) {
			throw new GameOverException("Impossible d’aller à gauche");
		}
		pos.column = --column;
		pos.row = row;

		return pos;
	}
	
	/** Avance le jouer d’une case
	 * @param dir la direction dans laquelle avancer
	 * @return la nouvelle position du joueur
	 * @throws GameOverException si le mouvement n’est pas possible
	 */
	public DungeonPosition move(Direction dir) throws GameOverException {
		DungeonPosition movDir = null;

		switch(dir) {
		case UP:	movDir = up();		break;
		case DOWN:  movDir = down(); 	break;
		case RIGHT: movDir = right(); 	break;
		case LEFT:  movDir = left(); 	break;
		default:	throw new GameOverException("Direction inconnue");
		}

		return movDir;
	}

}
