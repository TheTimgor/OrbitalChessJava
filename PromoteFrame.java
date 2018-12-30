
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.*;

public class PromoteFrame extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/*** COMPONENT PROPORTIONS ***/
	public static final double MY_SHORT_DIM = 1.0 / 4;
	public static final double PAD = 0.6;
	
	/*** COMPONENTS ***/
	private class PromoteTile extends JPanel implements MouseListener {
		private static final long serialVersionUID = 1L;
		private PromoteFrame par;
		private Piece p;
		public PromoteTile(PromoteFrame par, Piece p) {
			super();
			this.p = p;
			this.par = par;
			addMouseListener(this);
			this.setOpaque(true);
			this.setBackground(BoardTheme.BLACK);
			//this.setVisible(true);
		}
		public void mouseClicked(MouseEvent arg0) { par.select(p); }
		public void mouseEntered(MouseEvent arg0) { setBackground(BoardTheme.B_MOUSE); }
		public void mouseExited(MouseEvent arg0) { setBackground(BoardTheme.BLACK); }
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		public void paintComponent(Graphics g) {
			System.out.println(this);
			if (getWidth() == 0 || getHeight() == 0)
				return;
			super.paintComponent(g);
			//g = getGraphics();
			AffineTransform at = new AffineTransform();
			at.translate(getWidth() / 2, getHeight() / 2);
			Image img = p.getImage();
			double s = GameBoard.scaleUtil(img, getWidth() * PAD, getHeight() * PAD);
			at.scale(s, s);
			at.translate(-img.getWidth(null) / 2, -img.getHeight(null) / 2);
			((Graphics2D)g).drawImage(img, at, null);
		}
		public void paint(Graphics g) { paintComponent(g); }
	}
	private LocalPlayer lp;
	private Tile t;
	
	/*** CONSTRUCTOR ***/
	public PromoteFrame(LocalPlayer lp, Tile t) {
		super();
		this.t = t;
		this.lp = lp;
		this.setOpaque(true);
		setBorder(BorderFactory.createLineBorder(BoardTheme.B_MOVE, 3));
		boolean w = this.t.getPiece() != null ? this.t.getPiece().isWhite() : false;
		this.setLayout(new GridLayout(2, 2));
		this.add(new PromoteTile(this, new Knight(null, w)));
		this.add(new PromoteTile(this, new Bishop(null, w)));
		this.add(new PromoteTile(this, new Rook(null, w)));
		this.add(new PromoteTile(this, new Queen(null, w)));
		this.revalidate();
		this.setVisible(true);
	}
	public void autoresize(int x, int y, int w, int h) {
		int d = (int)(Math.min(w, h) * MY_SHORT_DIM);
		d -= d % 2;
		this.setBounds(x - d / 2, y - d / 2, d, d);
		//this.setLocation(new Point(x - this.getWidth() / 2, y - this.getHeight() / 2));
	}
	public void paint(Graphics g) {
		this.revalidate();
		for (Component c : getComponents()) {
			c.revalidate();
			if (c.getWidth() == 0 || c.getHeight() == 0)
				return;
		}
		System.out.println(this);
		super.paint(g);
		for (Component c : getComponents()) {
			c.setVisible(isVisible());
			c.setBackground(c.getBackground());
		}
	}
	
	/*** SELECT PIECE ***/
	public void select(Piece p) {
		lp.promoteResponse(p, t);
	}

}