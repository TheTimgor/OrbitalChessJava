
public class Knight extends Piece {
	
	/*** CONSTRUCTOR ***/
	public Knight(Tile loc, boolean white) { super((white ? "White" : "Black") + "Hopper.png", loc, white); }

	public boolean isLegal(Tile t) {
		if (t.getPiece() != null && t.getPiece().isWhite() == isWhite())
			return false;
		if (getLoc() == getLoc().getBoard().getOrigin())
			if (t.getPiece() != null)
				return GameRules.CAPTURE_FROM_ORIGIN;
			else
				return true;
		if (t == getLoc().getBoard().getOrigin() && (getLoc().getLoc()[0] == 1 || getLoc().getLoc()[0] == 2))
			return GameRules.ALL_PIECES_TAKE_ORIGIN;
		int dr = Math.abs(getLoc().getLoc()[0] - t.getLoc()[0]), dth = Math.abs(getLoc().getLoc()[1] - t.getLoc()[1]);
		dth = Math.min(dth,  GameRules.THETA - dth);
		if ((dr == 2 && dth == 1) || (dr == 1 && dth == 2))
			return true;
		if (t.getLoc()[0] == 1 && getLoc().getLoc()[0] == 2)
			return GameRules.PRANKSTER_KNIGHT;
		return false;
	}
	
	public Piece copy() {
		Knight p = new Knight(null, isWhite());
		if (hasMoved())
			p.setMoved();
		return p;
	}
	
	public String name() { return "Hopper"; }

}
