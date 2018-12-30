
import java.awt.geom.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;

public abstract class Piece {
	
	/*** Private member variables ***/
	private boolean moved;
	private boolean white;
	private Image img;
	private Tile loc;
	
	/*** CONSTRUCTOR ***/
	public Piece(String img, Tile loc, boolean white) {
		this.white = white;
		this.moved = false;
		this.loc = loc;
		loadImage("resources/" + img);
	}
	public Piece(){this("", null, true);}
	
	/*** ACCESSORS ***/
	public boolean hasMoved(){ return moved; }
	public boolean isWhite(){ return white; }
	public Image getImage(){ return img; }
	public Tile getLoc(){ return loc; }
	
	/*** MUTATORS ***/
	public void setWhite(boolean white){ this.white = white; }
	public void setImage(Image img){ this.img = img; }
	public void setSquare(Tile loc){ this.loc = loc; }
	public void setMoved(){ moved = true; }
	
	/*** MOVE ***/
	public Piece move(Tile l) {
		if (loc != null)
			loc.setPiece(null);
		loc = l;
		if (loc != null)
			loc.setPiece(this);
		return this;
	}
	
	/*** DRAWING ***/
	// Loads image
	void loadImage(String im) {
		try {
			URL url = Piece.class.getResource(im);
			img = (new ImageIcon(url)).getImage();
			//img = ImageIO.read(new File(im));
		}
		catch (Exception e) { JOptionPane.showMessageDialog(null, "Error reading file"); }
	}
	// Draws piece
	public void draw(Graphics g) {
		AffineTransform at = new AffineTransform();
		//at.scale(1, (double)img.getWidth(null)/img.getHeight(null));
		double mxh, mxw, dx, dy, th = 0;
		if (loc == loc.getBoard().getOrigin()) {
			mxh = loc.getBoard().getBoard().getRPix() * GameBoard.O_PROP * Math.sqrt(2);
			dx = loc.getBoard().getBoard().getCenterX();
			dy = loc.getBoard().getBoard().getCenterY();
			mxw = mxh;
		}
		else {
			dx = loc.getBoard().getBoard().getCenterX() + 
					((loc.getLoc()[0] + GameBoard.O_PROP - 0.5) * loc.getBoard().getBoard().getRPix()) *
					Math.cos((loc.bounds()[0] + loc.bounds()[1]) / 2);
			dy = loc.getBoard().getBoard().getCenterY() - 
					((loc.getLoc()[0] + GameBoard.O_PROP - 0.5) * loc.getBoard().getBoard().getRPix()) *
					Math.sin((loc.bounds()[0] + loc.bounds()[1]) / 2);
			th = -(loc.getLoc()[1] + 0.5 + loc.getBoard().deltaTheta() * GameRules.ROT[loc.getLoc()[0]]) * 2 * Math.PI / GameRules.THETA - Math.PI / 2;
			mxw = loc.getBoard().getBoard().getRPix() * (loc.getLoc()[0] + GameBoard.O_PROP - 0.5) * Math.PI * 2 / GameRules.THETA;
			mxh = loc.getBoard().getBoard().getRPix();
		}
		mxw *= GameBoard.PIECE_PAD;
		mxh *= GameBoard.PIECE_PAD;
		double s = GameBoard.scaleUtil(img, mxw, mxh);
		at.translate(dx, dy);
		at.scale(s, s);
		at.rotate(th);//, img.getWidth(null) / 2, img.getWidth(null) / 2);
		at.translate(-img.getWidth(null) / 2, -img.getHeight(null) / 2);
		((Graphics2D)g).drawImage(img, at, null);
	}
	
	/*** toString ***/
	public String toString() {
		String color = (white) ? "White" : "Black";
		return color + name() + "@" + getLoc().toString();
	}
	
	/*** ABSTRACT METHODS ***/
	public abstract Piece copy();
	public abstract boolean isLegal(Tile dest); 
	public abstract String name();

}
