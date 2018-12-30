
import java.awt.*;

public class Tile {

/*** Private member variables ***/
	
	// Location on board
	private BoardState board;
	private int[] loc;
	
	// Chess piece
	private Piece piece;
	
	// Colors
	private boolean checked;
	private boolean clicked;
	private boolean moused;
	private boolean white;
	private boolean legal;
	
	/*** Constructor ***/
	// Standard
	public Tile(BoardState board, Piece piece, int r, int th, boolean white) {
		
		//Board
		this.board = board;
		
		//Color
		this.checked = false;
		this.clicked = false;
		this.moused = false;
		this.legal = false;
		this.white = white;
		
		//Location
		this.loc = new int[2];
		this.loc[1] = th;
		this.loc[0] = r;
		
		//Piece
		this.piece = piece;
		
	}
	// Simple copy
	public Tile(Tile t) { this(t.board, null, t.loc[0], t.loc[1], t.white); }
	
	/*** ACCESSORS ***/
	public BoardState getBoard() { return board; }
	public boolean isWhite() { return white; }
	public Piece getPiece() { return piece; }
	public int[] getLoc() { return loc; }
	public String toString() {return "(" + loc[0] + "," + loc[1] + ")";}
	
	/*** MUTATORS ***/
	public void setBoard(BoardState board) { this.board = board; }
	public void setWhite(boolean white) { this.white = white; }
	public void setPiece(Piece piece) { this.piece = piece; }
	public void setLoc(int[] loc) { this.loc =loc; }
	
	/*** MOUSE RESPONSE ***/
	public void mouse() { moused = true; }
	public void unmouse() {	moused = false; }
	public void select() { clicked = true; }
	public void deselect() { clicked = false; }
	public void legalize() { legal = true; }
	public void illegalize() { legal = false; }
	public void check() { checked = true; }
	public void uncheck() { checked = false; }
	
	/*** PAINT ***/
	public double[] bounds() {
		double[] b = { 0, 0 };
		b[0] = (loc[1] + board.deltaTheta() * GameRules.ROT[loc[0]]) * 2 * Math.PI / GameRules.THETA;
		b[1] = b[0] + 2 * Math.PI / GameRules.THETA;
		return b;
	}
	private Color getColor() {
		if (clicked)
			return BoardTheme.CLICKED;
		if (checked)
			return BoardTheme.CHECK;
		if (moused)
			return white ? BoardTheme.W_MOUSE : BoardTheme.B_MOUSE;
		if (legal)
			return white ? BoardTheme.W_MOVE : BoardTheme.B_MOVE;
		return this == board.getOrigin() ? BoardTheme.ORIGIN : white ? BoardTheme.WHITE : BoardTheme.BLACK; 
	}
	public void repaint() { repaint(board.getBoard().getBuffer().getGraphics()); }
	public void repaint(Graphics g) {
		
		// Color and arguments
		g.setColor(getColor());
		int args[] = {
				(int)(board.getBoard().getCenterX() - board.getBoard().getRPix() * (loc[0] + GameBoard.O_PROP)),
				(int)(board.getBoard().getCenterY() - board.getBoard().getRPix() * (loc[0] + GameBoard.O_PROP)),
				(int)(board.getBoard().getRPix() * 2 * (loc[0] + GameBoard.O_PROP)),
				(int)(board.getBoard().getRPix() * 2 * (loc[0] + GameBoard.O_PROP))
		};
		
		// Draw origin
		if (board.getOrigin() == this)
			g.fillOval(args[0], args[1], args[2], args[3]);
		
		// Draw Sector
		else {
			g.fillArc(
				args[0], args[1], args[2], args[3],
				(int)(bounds()[0] * 180 / Math.PI),
				(int)((bounds()[1] - bounds()[0]) * 180 / Math.PI) + 1);
			// Draw lower sectors
			board.getTiles()[loc[0] - 1][loc[1]].repaint(g);
		}
		
		// Draw piece
		if (piece != null)
			piece.draw(g);
		
	}
	
}
