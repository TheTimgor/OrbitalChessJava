
public class Pawn extends Piece {
	
	/*** CONSTRUCTOR ***/
	public Pawn(Tile loc, boolean white) { super((white ? "White" : "Black") + "Sputnik.png", loc, white); }

	public boolean isLegal(Tile t) {
		if (t.getPiece() != null && t.getPiece().isWhite() == isWhite())
			return false;
		if (getLoc() == getLoc().getBoard().getOrigin()) {
			if (t.getPiece() != null && !GameRules.CAPTURE_FROM_ORIGIN)
				return false;
			if (t.getLoc()[0] != 1)
				return false;
			return true;
		}
		if (t == getLoc().getBoard().getOrigin())
			return getLoc().getLoc()[0] == 1 && (GameRules.ALL_PIECES_TAKE_ORIGIN || GameRules.PAWNS_PROMOTE_AT_ORIGIN);
		if (t.getLoc()[0] + 1 == getLoc().getLoc()[0] && t.getPiece() == null && t.getLoc()[1] == getLoc().getLoc()[1])
			return true;
		if (GameRules.DOUBLE_MOVE && !hasMoved() && t.getLoc()[0] + 2 == getLoc().getLoc()[0] && t.getPiece() == null && t.getLoc()[1] == getLoc().getLoc()[1] && getLoc().getBoard().inLegalStep(getLoc(), t, -1, 0))
			return true;
		int dth = Math.abs(t.getLoc()[1] - getLoc().getLoc()[1]);
		dth = Math.min(dth, GameRules.THETA - dth);
		if (t.getLoc()[0] + 1 == getLoc().getLoc()[0] && t.getPiece() != null && dth == 1)
			return true;
		return false;
	}
	
	public Piece copy() {
		Pawn p = new Pawn(null, isWhite());
		if (hasMoved())
			p.setMoved();
		return p;
	}
	
	public String name() { return "Sputnik"; }

}
