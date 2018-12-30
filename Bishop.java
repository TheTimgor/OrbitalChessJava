
public class Bishop extends Piece {
	
	/*** MOVES ***/
	private static int[][] moves = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
	
	/*** CONSTRUCTOR ***/
	public Bishop(Tile loc, boolean white) { super((white ? "White" : "Black") + "Shuttle.png", loc, white); }

	/*** MOVES ***/
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
		for (int[] v : moves)
			if (getLoc().getBoard().inLegalStep(getLoc(), t, v[0], v[1]))
				if (t == getLoc().getBoard().getOrigin())
					return GameRules.BISHOPS_TAKE_ORIGIN;
				else
					return true;
		return false;
	}
	
	public Piece copy() {
		Bishop p = new Bishop(null, isWhite());
		if (hasMoved())
			p.setMoved();
		return p;
	}
	
	public String name() { return "Shuttle"; }

}
