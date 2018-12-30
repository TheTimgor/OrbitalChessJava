
public class King extends Piece {
	
	/*** CONSTRUCTOR ***/
	public King(Tile loc, boolean white) { super((white ? "White" : "Black") + "CommandCenter.png", loc, white); }

	public boolean isLegal(Tile t) {
		if (t.getPiece() != null && t.getPiece().isWhite() == isWhite())
			return false;
		if (getLoc() == getLoc().getBoard().getOrigin())
			if (t.getPiece() != null)
				return GameRules.CAPTURE_FROM_ORIGIN;
			else
				return true;
		if (t == getLoc().getBoard().getOrigin() && getLoc().getLoc()[0] == 1)
			return GameRules.ALL_PIECES_TAKE_ORIGIN;
		if (Math.abs(getLoc().getLoc()[0] - t.getLoc()[0]) <= 1 && Math.abs(getLoc().getLoc()[1] - t.getLoc()[1]) <= 1)
			return true;
		return false;
	}
	
	public Piece copy() {
		King p = new King(null, isWhite());
		if (hasMoved())
			p.setMoved();
		return p;
	}
	
	public String name() { return "Command Center"; }

}
