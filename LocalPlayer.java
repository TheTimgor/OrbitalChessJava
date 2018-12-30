
import java.util.*;

public class LocalPlayer extends Player {
	
	/*** UI INFO ***/
	private ArrayList<Tile> highlights;
	private Tile move;
	
	/*** CONSTRUCTOR ***/
	public LocalPlayer(GameBoard board) {
		super(board);
		this.highlights = new ArrayList<Tile>();
	}

	/*** PLAY ***/
	public void moveQuery() { setMoving(true); }
	public void select(Tile t) {
		
		// Null check
		if(t == null)
			return;
		
		// New square clicked
		if(move == null) {
			if(t.getPiece() != null && getBoard().getBoardState().pieceTurn(t.getPiece())){
				
				//Select square
				move = t;
				t.select();
				
				//Highlight moves
				if(move.getPiece() != null)
					for(int r = 1; r <= GameRules.RAD; r++)
						for(int th = 0; th < GameRules.THETA; th++)
							if(getBoard().getBoardState().isLegal(move, getBoard().getBoardState().getTiles()[r][th]))
								highlights.add(getBoard().getBoardState().getTiles()[r][th]);
				if (getBoard().getBoardState().isLegal(move, getBoard().getBoardState().getOrigin()))
					highlights.add(getBoard().getBoardState().getOrigin());
				for(Tile t2: highlights)
					t2.legalize();
				getBoard().draw();
				
			}
		}
		
		//Move completed
		else {
			//boolean bMove = false;
			/*if(move != s && move.getPiece() != null && move.getPiece().isLegal(s))
				makeMove(move, s);*/
			for (Tile t2: highlights)
				t2.illegalize();
			highlights = new ArrayList<Tile>();
			move.deselect();
			if (getBoard().getBoardState().isLegal(move, t)){
				getBoard().makeMove(move, t);
				//System.out.println("--- MOVE " + (state.getTurns() - 1) + ": " + state.getLead() + "---");
				//Black move
				//bMove = true;
			}
			//Check for mate
			/*if (.noMoves()){
				if(state.checkMate())
					JOptionPane.showMessageDialog(this, "Checkmate: Game Over");
				else
					JOptionPane.showMessageDialog(this, "Stalemate: Game Over");
				mated = true;
			}*/
			move = null;
			getBoard().draw();
			//if(bMove)
				//AI.makeMove(state, false);
		}
	}
	
	// Launch promoter
	public void promote(Tile t) {
		PromoteFrame p = new PromoteFrame(this, t);
		getBoard().setPromo(p);
	}
	public void promoteResponse(Piece p, Tile t) {
		getBoard().setPromo(null);
		getBoard().finishMove(t, p);
	}

}