import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

	// sizes
	public final static int WIDTH = 640;
	public final static int HEIGHT = 480;
	public final static int SQUARE = 50;
	private final static int PADDING = 40;
	
	// colors
	public final static Color WHITE = new Color(250, 250, 250);
	public final static Color BLACK = new Color(80, 80, 80);
	public final static Color SELECTED = new Color(80, 80, 250);
	public final static Color POSSIBLE = new Color(80, 200, 130);
	public final static Color ATTACK = new Color(250, 80, 80);
	public final static Color CAUTION = new Color(220, 170, 80);
	public final static Color PROTECT = new Color(230, 210, 50);
	public final static Color TEXT = new Color(240, 240, 240);
	public final static Color BACKGROUND = new Color(20, 20, 20);
	
	// fonts
	private final static Font NORMALFONT = new Font("Trebuchet MS", Font.BOLD, 16);
	
	// image
	private Graphics2D g;
	
	// board
	private Piece[][] chessboard = new Piece[8][8];

	// scores
	private int playerScore, enemyScore;
	
	// turns
	private int turn;
	
	// selected
	private int x, y;
	private boolean toggleCaution, togglePossible;
	private ArrayList<Point> hoverList;
	
	public GamePanel() {
		super();
		
		//Set up panel
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addMouseListener(this);
		addMouseMotionListener(this);

		initBoard();
		
		//Set up drawing
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		paintComponent(g);
	}
	
	private void initBoard() {
		for(int i = 0; i < 8; i++) {
			mirror(i, 1, Piece.PAWN);
		}
		mirror(0, 0, Piece.ROOK);
		mirror(7, 0, Piece.ROOK);
		mirror(1, 0, Piece.KNIGHT);
		mirror(6, 0, Piece.KNIGHT);
		mirror(2, 0, Piece.BISHOP);
		mirror(5, 0, Piece.BISHOP);
		mirror(3, 0, Piece.KING);
		mirror(4, 0, Piece.QUEEN);
		x = -1;
	}
	
	private void mirror(int x, int y, int type) {
		chessboard[x][y] = new Piece(type, 1);
		chessboard[x][7 - y] = new Piece(type, 0);
	}
	
	//This is the display method. 
	//Everything is redrawn each time the mouse is clicked.
	@Override
	public void paintComponent(Graphics g) {
		
		g.setFont(NORMALFONT);
		
		//Background
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Board
		g.setColor(WHITE);
		//Border
		g.drawRect(PADDING - 1, PADDING - 1, SQUARE * 8 + 1, SQUARE * 8 + 1);
		//White Squares
		for(int i = 0; i < 64; i+=2) {
			g.fillRect(i/8 * SQUARE + PADDING, (i+i/8)%8 * SQUARE + PADDING, SQUARE, SQUARE);
		}
		//Black Squares
		g.setColor(BLACK);
		for(int i = 1; i < 64; i+=2) {
			g.fillRect(i/8 * SQUARE + PADDING, (i+i/8)%8 * SQUARE + PADDING, SQUARE, SQUARE);
		}
		
		//Selected Square
		if(x != -1) {
			g.setColor(SELECTED);
			g.fillRect(x * SQUARE + PADDING, y * SQUARE + PADDING, SQUARE, SQUARE);
			
			//Possible moves (Helper)
			for(Point p : Piece.possibleMoves(x, y, chessboard)) {
				g.setColor(chessboard[p.x][p.y] != null ? ATTACK : POSSIBLE);
				g.fillRect(p.x * SQUARE + PADDING, p.y * SQUARE + PADDING, SQUARE, SQUARE);	
			}
		}
		
		//Hover moves (Helper)
		if((toggleCaution || togglePossible) && hoverList != null) {
			for(Point p : hoverList) {
				g.setColor(chessboard[p.x][p.y].isSide(turn) ? PROTECT : CAUTION);
				g.fillRect(p.x * SQUARE + PADDING, p.y * SQUARE + PADDING, SQUARE, SQUARE);				
			}
		}
		
		//This makes text look a lot smoother
		((Graphics2D) g).setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		//Coordinates
		//Offset for Horizontal: 14
		//Offset for Vertical: 5 (values change with font)
		g.setColor(TEXT);
		for(int i = 1; i < 9; i++) {
			g.drawString((char)('a' + i - 1) + "", i * SQUARE + SQUARE / 2 - 14, PADDING * 2 / 3);
			g.drawString(i + "", PADDING / 2, i * SQUARE + SQUARE / 2 - 5);
		}
		
		//Pieces
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(chessboard[i][j] != null) {
					chessboard[i][j].draw(g, i * SQUARE + PADDING, j * SQUARE + PADDING);
				}
			}
		}
		
		//Right Panel
		g.drawString("Your Score: " + playerScore, SQUARE * 8 + PADDING * 2, PADDING * 2);
		g.drawString("Opponent Score: " + enemyScore, SQUARE * 8 + PADDING * 2, PADDING * 3);
		g.drawString("Turn: " + (turn == 0 ? "white" : "black"), SQUARE * 8 + PADDING * 2, PADDING * 5);
		g.drawRect(SQUARE * 8 + PADDING * 2, PADDING * 7, PADDING * 3, SQUARE);
		g.drawString("Hover: " + (toggleCaution ? "on" : "off"), SQUARE * 8 + PADDING * 2 + 20, PADDING * 7 + 30);
	}
	
	//This is what MouseListener is used for
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	//For hover effects
	@Override
	public void mouseMoved(MouseEvent e) {
		if(toggleCaution) {
			if(e.getX() < PADDING) { repaint(); hoverList = null; return; }
			int selX = (e.getX() - PADDING) / SQUARE;
			int selY = (e.getY() - PADDING) / SQUARE;
			if(selX >= 8 || selY >= 8) {
				hoverList = null;
			} else if(x == -1) {
				hoverList = new ArrayList<Point>();
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						if(chessboard[i][j] != null) {
							for(Point p : Piece.possibleMoves(i, j, chessboard)) {
								if(p.x == selX && p.y == selY) {
									hoverList.add(new Point(i, j));
									break;
								}
							}
						}
					}
				}
				revalidate();
				repaint();
			}
		} else if(togglePossible) { //Not Working
			if(e.getX() < PADDING) { repaint(); hoverList = null; return; }
			int selX = (e.getX() - PADDING) / SQUARE;
			int selY = (e.getY() - PADDING) / SQUARE;
			if(selX >= 8 || selY >= 8) {
				hoverList = null;
			} else if(x == -1) {
				for(Point p : Piece.possibleMoves(selX, selY, chessboard)) {
					hoverList.add(p);
				}
				revalidate();
				repaint();
			}
		}
	}
	
	//You don't really need these
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {

		//Preference
		hoverList = null;
		
		if(e.getX() < PADDING) { x = -1; repaint(); return; }
		int selX = (e.getX() - PADDING) / SQUARE;
		int selY = (e.getY() - PADDING) / SQUARE;

		if(selX >= 8 || selY >= 8) {

			//Clicked on Toggle?
			Point p = new Point(e.getX(), e.getY());
			Rectangle r = new Rectangle(SQUARE * 8 + PADDING * 2, PADDING * 7, PADDING * 3, SQUARE);
			if(r.contains(p)) {
				toggleCaution = !toggleCaution;
			}
			
			//Did not click on board
			x = -1;
		} else {
			if(x == -1) {
				if(chessboard[selX][selY] == null || !chessboard[selX][selY].isSide(turn)) {
					return;
				}
				x = selX;
				y = selY;
			} else if(x == selX && y == selY) {
				x = -1;
			} else if(chessboard[x][y].validMove(x, y, selX, selY, chessboard)) {
				if(chessboard[selX][selY] != null) {
					playerScore += chessboard[selX][selY].getValue();
				}
				chessboard[selX][selY] = chessboard[x][y];
				chessboard[x][y] = null;
				x = -1;
				turn = turn == 0 ? 1 : 0;
			} else {
				x = -1;
			}
		}

		//Update to Screen
		revalidate();
		repaint();
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {}

}
