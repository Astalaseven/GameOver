package g39189.gameover.model;

public class Player {
	
	//private DungeonPosition[] POSITIONS;
	private int n;
	private int nextN;
	private String name;
	private BarbarianColor color;
	private final DungeonPosition initPosition;
	
	/** Constructeur de Player
	 * @param name nom du joueur
	 * @throws GameOverException 
	 */
	public Player(String name) {
		
		this.name = name;
		this.n = nextN++;
		this.color = BarbarianColor.values()[n];

		switch(n) {

		case 0:
			this.initPosition = DungeonPosition.P_BARBARIAN_1;
			break;
		case 1:
			this.initPosition = DungeonPosition.P_BARBARIAN_2;
			break;
		case 2:
			this.initPosition = DungeonPosition.P_BARBARIAN_3;
			break;
		case 3:
			this.initPosition = DungeonPosition.P_BARBARIAN_4;
			break;

		default:
			this.initPosition = null;
		}
	}

	/** Retourne la valeur de name
	 * @return le nom du joueur
	 */
	public String getName() {

		return name;
	}

	/** Retourne la valeur de color
	 * @return la couleur du joueur
	 */
	public BarbarianColor getColor() {

		return color;
	}

	/** Retourne la valeur de initPosition
	 * @return la position initiale du joueur
	 */
	public DungeonPosition getInitPosition() {

		return initPosition;
	}
	
	/** Retourne l’id du joueur
	 * @return l’id du joueur
	 */
	public int getId() {

		return n;
	}
	
	@Override
	public String toString() {

		return "Player [n=" + n + ", name=" + name + ", color=" + color + 
				", initPosition=" + initPosition + "]";
	}

	public static void main(String[] args) {
		Player player = new Player("Paul");
		System.out.println(player);
		Player player0 = new Player("Jean");
		System.out.println(player0);
	}
	
}
