
public class Queen extends Piece {
	
	/*** CONSTRUCTOR ***/
	public Queen(Tile loc, boolean white) { super((white ? "White" : "Black") + "Mothership.png", loc, white); }

	public boolean isLegal(Tile t) { return (new Rook(getLoc(), isWhite())).isLegal(t) || (new Bishop(getLoc(), isWhite())).isLegal(t); }
	
	public Piece copy() {
		Queen p = new Queen(null, isWhite());
		if (hasMoved())
			p.setMoved();
		return p;
	}
	
	public String name() { return "Mothership"; }

}
