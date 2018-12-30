
public class Rook extends Piece {
	
	/*** MOVES ***/
	private int[][] moves = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
	
	/*** CONSTRUCTOR ***/
	public Rook(Tile loc, boolean white) { super((white ? "White" : "Black") + "Turret.png", loc, white); }

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
					return GameRules.ALL_PIECES_TAKE_ORIGIN;
				else
					return true;
		return false;
	}
	
	public Piece copy() {
		Rook p = new Rook(null, isWhite());
		if (hasMoved())
			p.setMoved();
		return p;
	}
	
	public String name() { return "Turret"; }
	
}
