
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;


import java.awt.*;

public class GameBoard extends JFrame implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
		
	/*** GRAPHICS INFO ***/
	public static final String TITLE = "Orbital Chess";
	public static final double ROT_FRAMES = 50;
	public static final double PIECE_PAD = 0.8;
	public static final double O_PROP = 0.5;
	public static final double PAD = 0.025;
	private int rpix;
	private int cx;
	private int cy;
	
	/*** ASSORTED PRIVATES ***/
	private BufferedImage buff;
	private PromoteFrame promo;
	private Player[] players;
	private BoardState state;
	private Piece check;
	private Tile moused;
	
	/*** COSTRUCTOR ***/
	public GameBoard(){
				
		// Create window
		super(TITLE + " - White to Move");
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		
		// Listeners
		addMouseMotionListener(this);
		addMouseListener(this);
		
		// Initialize privates
		players = new Player[2];
		players[0] = new LocalPlayer(this);
		players[1] = new LocalPlayer(this);
		state = new BoardState(this);
		paint(getGraphics());
		moused = null;		
		
	}
	
	/*** ACCESSORS ***/
	public BoardState getBoardState() { return state; }
	public BufferedImage getBuffer() { return buff; }
	public Player[] getPlayers() { return players; }
	public int getCenterY() { return cy; }
	public int getCenterX() { return cx; }
	public int getRPix() { return rpix; }
	
	/*** MUTATORS ***/
	public void setBoardState(BoardState state) { this.state = state; }
	public void setPromo(PromoteFrame promo) {
		if (this.promo != null) {
			//this.promo.setVisible(false);
			this.remove(this.promo);
		}
		this.promo = promo;
		if (promo != null) {
			add(promo);
			promo.setVisible(true);	
			promo.autoresize(cx, cy, getWidth(), getHeight());
		}
		//pack();
		draw();
	}
	
	/*** MAKE MOVE ***/
	public void makeMove(Tile t1, Tile t2) {
		System.out.println(state.toString());
		state.move(t1, t2);
		draw();
		if (t2.getPiece() != null && t2.getPiece() instanceof Pawn)
			if (GameRules.PAWNS_PROMOTE_AT_ORIGIN && t2 == state.getOrigin())
				players[(state.getMove() - 1) % 2].promote(t2);
			else if (GameRules.PAWNS_PROMOTE_AT_R1 && t2.getLoc()[0] == 1)
				players[(state.getMove() - 1) % 2].promote(t2);
			else
				finishMove(t2, null);
		else
			finishMove(t2, null);
	}
	public void finishMove(Tile t2, Piece p) {
		if (p != null)
			p.move(t2);
		if (state.noMoves()) {
			if (state.checkMate(true) != null) {
				setTitle("Checkmate - " + (state.checkMate(true).isWhite() ? "Black" : "White") + " wins");
				JOptionPane.showMessageDialog(this, "Checkmate: " + (state.checkMate(true).isWhite() ? "Black" : "White") + " wins");
			}
			else {
				setTitle("Stalemate - Draw");
				JOptionPane.showMessageDialog(this, "Stalemate: Draw");
			}
			return;
		}
		setTitle(TITLE + " - " + (state.getMove() % 2 == 0 ? "White" : "Black") + " to Move");
		if (state.getMove() % 2 == 0)
			rotate();
		if (check != null) {
			check.getLoc().uncheck();
			check = null;
		}
		if (state.inCheck() != null) {
			check = state.inCheck();
			check.getLoc().check();
		}
		players[state.getMove() % 2].moveQuery();
	}
	
	/*** GRAPHICS ***/
	public static double scaleUtil(Image img, double w, double h) {
		if (img.getHeight(null) == 0 || img.getWidth(null) == 0)
			return 0;
		double s = w / img.getWidth(null);
		if (img.getHeight(null) * s > h)
			s = h / img.getHeight(null);
		return s;
	}
	private void resize() {
		buff = new BufferedImage(getWidth() - getInsets().right - getInsets().left, getHeight() - getInsets().top - getInsets().bottom, BufferedImage.TYPE_INT_ARGB);
		rpix = (int)(Math.min(buff.getHeight(), buff.getWidth()) * (1 - 2 * PAD) / (GameRules.RAD + O_PROP)) / 2;
		((Graphics2D)buff.getGraphics()).setBackground(BoardTheme.BKG);
		buff.getGraphics().clearRect(0, 0, buff.getWidth(), buff.getHeight());
		cy = buff.getHeight() / 2;
		cx = buff.getWidth() / 2;
	}
	public void paint(Graphics g) {
		super.paint(g);
		try{ resize(); }
		catch (Exception e) { return; }
		draw(g);
	}
	public void draw() { draw(getGraphics()); }
	public void draw(Graphics g) {
		if (state != null)
			for (int r = GameRules.RAD; r >= 1; r--)
				for (int th = 0; th < GameRules.THETA; th++)
					state.getTiles()[r][th].repaint(buff.getGraphics());
		g.drawImage(buff, getInsets().left, getInsets().top, null);
		if (promo != null) {
			promo.autoresize(cx, cy, getWidth(), getHeight());
			//promo.paint(buff.getGraphics());
			promo.paint(g);
		}
	}
	public void rotate() {
		state.setDeltaTheta(0);
		for (int i = 0; i < ROT_FRAMES; i++) {
			state.setDeltaTheta(state.deltaTheta() + 1 / ROT_FRAMES);
			draw();
		}
		state.setDeltaTheta(0);
		state.rotate();
		draw();
	}
	
	/*** MOUSE INPUT ***/
	private Tile mouseAt() { return mouseAt(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY()); }
	private Tile mouseAt(double x, double y) {
		if (state == null || buff == null)
			return null;
		x -= cx + getInsets().right + getLocationOnScreen().getX();
		y -= cy + getInsets().top + getLocationOnScreen().getY();
		y = -y;
		if (x == 0)
			x = 0.01;
		double th = Math.atan(y / x);
		if (x < 0)
			th += Math.PI;
		if (th < 0)
			th += 2 * Math.PI;
		int ith = (int)(th / (2 * Math.PI / GameRules.THETA));
		double r = Math.sqrt(x * x + y * y);
		int ir = r < rpix * O_PROP ? 0 : (int)((r - rpix * O_PROP) / rpix + 1);
		return ir <= GameRules.RAD ? state.getTiles()[ir][ith] : null;
	}
	public void mouseClicked(MouseEvent arg0) {
		if (promo != null)
			return;
		Tile t = mouseAt();
		if (t != null)
			players[state.getMove() % 2].select(t);
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void mouseDragged(MouseEvent arg0) {}
	public void mouseMoved(MouseEvent arg0) {
		if (moused == mouseAt() || promo != null)
			return;
		if (moused != null)
			moused.unmouse();
		moused = mouseAt();
		if (moused != null)
			moused.mouse();
		draw();
	}
	
	public static void main(String[] args) {
		new GameBoard();
		}

}
