
public abstract class Player {
	
	/*** ASSORTED PRIVATES ***/
	private GameBoard board;
	private boolean moving;
	
	/*** CONSTRUCTOR ***/
	public Player(GameBoard board) {
		this.moving = false;
		this.board = board;
	}
	
	/*** ACCESSORS ***/
	public GameBoard getBoard() { return board; }
	public boolean isMoving() { return moving; }
	
	/*** MUTATORS ***/
	public void setMoving(boolean moving) { this.moving = moving; }
	
	/*** PLAY ***/
	public abstract void moveQuery();
	public abstract void select(Tile t);
	public abstract void promote(Tile t);

}
