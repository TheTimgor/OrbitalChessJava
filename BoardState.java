import java.util.ArrayList;

public class BoardState {
	
	/*** BOARD FRAME ***/
	private GameBoard board;
	
	/*** PRIVATE BOARD INFO ***/
	private Tile[][] tiles;
	private King[] kings;
	private Tile origin;
	private double dth;
	private int move;
	
	/*** CONSTRUCTOR ***/
	public BoardState(GameBoard board) {
		
		// Initialize variables
		this.tiles = new Tile[GameRules.RAD + 1][GameRules.THETA];
		this.kings = new King[2];
		this.board = board;
		this.move = 0;
		this.dth = 0;
		
		// Create Board
		// Origin
		this.origin = new Tile(this, null, 0, 0, false);
		for (int th = 0; th < GameRules.THETA; th++)
			this.tiles[0][th] = origin;
		// Normal tiles
		for (int th = 0; th < GameRules.THETA; th++)
			for (int r = 1; r <= GameRules.RAD; r++) {
				this.tiles[r][th] = new Tile(this, null, r, th, (r + th) % 2 != 0);
			}
		setup();
		
	}
	
	/*** COPY ***/
	public BoardState copy() {
		BoardState bs = new BoardState(null);
		bs.move = move;
		for (int r = 0; r <= GameRules.RAD; r++)
			for (int th = 0; th < GameRules.THETA; th++)
				if (tiles[r][th].getPiece() != null)
					tiles[r][th].getPiece().copy().move(bs.tiles[r][th]);
		for (int i = 0; i < 2; i++)
			bs.kings[i] = ((King) bs.tiles[kings[i].getLoc().getLoc()[0]][kings[i].getLoc().getLoc()[1]].getPiece());
		return bs;
	}
	
	/*** BOARD SETUP ***/
	public void setup() {
		if (GameRules.RAD == 4) {
			(new Pawn(null, false)).move(tiles[4][0]);
			(new Pawn(null, false)).move(tiles[3][1]);
			(new Pawn(null, false)).move(tiles[3][2]);
			(new Pawn(null, false)).move(tiles[2][3]);
			(new Pawn(null, false)).move(tiles[2][4]);
			(new Pawn(null, false)).move(tiles[3][5]);
			(new Pawn(null, false)).move(tiles[3][6]);
			(new Pawn(null, false)).move(tiles[4][7]);
			(new Pawn(null, true)).move(tiles[4][8]);
			(new Pawn(null, true)).move(tiles[3][9]);
			(new Pawn(null, true)).move(tiles[3][10]);
			(new Pawn(null, true)).move(tiles[2][11]);
			(new Pawn(null, true)).move(tiles[2][12]);
			(new Pawn(null, true)).move(tiles[3][13]);
			(new Pawn(null, true)).move(tiles[3][14]);
			(new Pawn(null, true)).move(tiles[4][15]);
			(new Rook(null, false)).move(tiles[4][1]);
			(new Bishop(null, false)).move(tiles[4][2]);
			(new King(null, false)).move(tiles[4][3]);
			(new Knight(null, false)).move(tiles[3][3]);
			(new Queen(null, false)).move(tiles[4][4]);
			(new Knight(null, false)).move(tiles[3][4]);
			(new Bishop(null, false)).move(tiles[4][5]);
			(new Rook(null, false)).move(tiles[4][6]);
			(new Rook(null, true)).move(tiles[4][9]);
			(new Bishop(null, true)).move(tiles[4][10]);
			(new King(null, true)).move(tiles[4][11]);
			(new Knight(null, true)).move(tiles[3][11]);
			(new Queen(null, true)).move(tiles[4][12]);
			(new Knight(null, true)).move(tiles[3][12]);
			(new Bishop(null, true)).move(tiles[4][13]);
			(new Rook(null, true)).move(tiles[4][14]);
		}
		if (GameRules.RAD == 8) {
			kings[0] = new King(null, false);
			kings[0].move(tiles[8][3]);
			(new Queen(null, false)).move(tiles[8][4]);
			(new Rook(null, false)).move(tiles[8][5]);
			(new Rook(null, false)).move(tiles[8][2]);
			(new Bishop(null, false)).move(tiles[7][3]);
			(new Bishop(null, false)).move(tiles[7][4]);
			(new Knight(null, false)).move(tiles[6][3]);
			(new Knight(null, false)).move(tiles[6][4]);
			(new Queen(null, true)).move(tiles[8][11]);
			kings[1] = new King(null, true);
			kings[1].move(tiles[8][12]);
			(new Rook(null, true)).move(tiles[8][10]);
			(new Rook(null, true)).move(tiles[8][13]);
			(new Bishop(null, true)).move(tiles[7][11]);
			(new Bishop(null, true)).move(tiles[7][12]);
			(new Knight(null, true)).move(tiles[6][11]);
			(new Knight(null, true)).move(tiles[6][12]);
			(new Pawn(null, false)).move(tiles[8][1]);
			(new Pawn(null, false)).move(tiles[7][2]);
			(new Pawn(null, false)).move(tiles[6][2]);
			(new Pawn(null, false)).move(tiles[5][3]);
			(new Pawn(null, false)).move(tiles[5][4]);
			(new Pawn(null, false)).move(tiles[6][5]);
			(new Pawn(null, false)).move(tiles[7][5]);
			(new Pawn(null, false)).move(tiles[8][6]);
			(new Pawn(null, true)).move(tiles[8][9]);
			(new Pawn(null, true)).move(tiles[7][10]);
			(new Pawn(null, true)).move(tiles[6][10]);
			(new Pawn(null, true)).move(tiles[5][11]);
			(new Pawn(null, true)).move(tiles[5][12]);
			(new Pawn(null, true)).move(tiles[6][13]);
			(new Pawn(null, true)).move(tiles[7][13]);
			(new Pawn(null, true)).move(tiles[8][14]);
		}
	}
	
	/*** PATH OBSTRUCTION ***/
	public boolean inLegalStep(Tile t1, Tile t2, int dr, int dth) {
		boolean vis = false, flew = false;
		Tile t = t1;
		while (t != t2) {
			if (vis && (t.getPiece() != null || t == t1))
				return false;
			vis = true;
			try {
				if (dr != 0 && dth != 0 && GameRules.BOUNCE_AT_PERIHELION && t.getLoc()[0] == GameRules.RAD)
					dr = -dr;
				if (!flew && dr < 0 && dth != 0 && GameRules.ORIGIN_FLYBY && t.getLoc()[0] == 1 && t2 != t.getBoard().getOrigin()) {
					t = tiles[1][(GameRules.THETA / 2 + t.getLoc()[1]) % GameRules.THETA];
					flew = true;
					dr = -dr;
					continue;
				}
				t = tiles[t.getLoc()[0] + dr][(GameRules.THETA + t.getLoc()[1] + dth) % GameRules.THETA];
			}
			catch (Exception e) { return false; }
		}
		return true;
	}
	
	/*** MAKE MOVE ***/
	public boolean pieceTurn(Piece p) { return !((move % 2 == 0)^p.isWhite()); }
	public boolean basicLegal(Tile t1, Tile t2) {
		if (t1.getPiece() == null)
			return false;
		return t1.getPiece().isLegal(t2);
	}
	public boolean isLegal(Tile t1, Tile t2) {
		if (t1.getPiece() == null || t1.getPiece().isWhite() != (move % 2 == 0))
			return false;
		if (!basicLegal(t1, t2))
			return false;
		BoardState bs = copy();
		bs.move(bs.getTiles()[t1.getLoc()[0]][t1.getLoc()[1]], bs.getTiles()[t2.getLoc()[0]][t2.getLoc()[1]], true);
		if (bs.inCheck() != null && bs.inCheck().isWhite() == (move % 2 != 1))
			return false;
		return true;
	}
	public boolean move(Tile t1, Tile t2) { return move(t1, t2, false); }
	public boolean move(Tile t1, Tile t2, boolean virt) {
		t1.getPiece().move(t2);
		t2.getPiece().setMoved();
		move++;
		if (virt && move % 2 == 0)
			rotate();
		return true;
	}
	public King inCheck() {
		BoardState bs = copy();
		for (Tile[] ts : bs.tiles)
			for (Tile t : ts)
				if (bs.basicLegal(t, bs.kings[0].getLoc()))
					return kings[0];
				else if (bs.basicLegal(t, bs.kings[1].getLoc()))
					return kings[1];
		return null;
	}
	public boolean noMoves() {
		for (Tile[] ts1 : tiles)
			for (Tile t1 : ts1)
				for (Tile[] ts2 : tiles)
					for (Tile t2 : ts2)
						if (isLegal(t1, t2))
							return false;
		return true;
	}
	public King checkMate() { return checkMate(false); }
	public King checkMate(boolean nom) {
		BoardState bs = copy();
		if (!nom && !noMoves())
			return null;
		System.out.println("Pass");
		if (move % 2 != 0) {
			System.out.println("ROT");
			bs.rotate();
		}
		if (bs.inCheck() == null)
			return null;
		System.out.println("Mate");
		return ((King) tiles[bs.inCheck().getLoc().getLoc()[0]][bs.inCheck().getLoc().getLoc()[1]].getPiece());
	}
	
	/*** ROTATE STATE ***/
	public void rotate() {
		Tile[][] nt = new Tile[GameRules.RAD + 1][GameRules.THETA];
		for (int r = 1; r <= GameRules.RAD; r++)
			for (int th = 0; th < GameRules.THETA; th++) {
				nt[r][th] = new Tile(tiles[r][th]);
				Piece p = tiles[r][(GameRules.THETA + (th - GameRules.ROT[r]) % GameRules.THETA) % GameRules.THETA].getPiece();
				if (p != null)
					p.move(nt[r][th]);
				nt[0][th] = origin;
			}
		tiles = nt;
	}
	
	/*** ACCESSORS ***/
	public GameBoard getBoard() { return board; }
	public Tile[][] getTiles() { return tiles; }
	public double deltaTheta() { return dth; }
	public Tile getOrigin() { return origin; }
	public int getMove() { return move; }
	public ArrayList<Piece> getPieces() {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for(Tile[] r: tiles) {
			for(Tile t: r) {
				if(t.getPiece() != null) {
					pieces.add(t.getPiece());
				}
			}
		}
		return pieces;		
	}
	public String toString() {
		ArrayList<Piece> pieces = getPieces();
		String string = "";
		for(Piece p: pieces) {
			string += p.toString()+";";
		}
		string += "move:" + move;
		return string;
		
	}
	
	/*** MUTATORS ***/
	public void setBoard(GameBoard board) { this.board = board; }
	public void setDeltaTheta(double dth) { this.dth = dth; }
	public void setMove(int move) { this.move = move; }	

}
