package starter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseListener {

	// sizes
	public final static int WIDTH = 640;
	public final static int HEIGHT = 480;
	public final static int SQUARE = 50;
	private final static int PADDING = 40;
	
	// colors
	public final static Color WHITE = new Color(250, 250, 250);
	public final static Color BLACK = new Color(80, 80, 80);
	public final static Color TEXT = new Color(240, 240, 240);
	public final static Color BACKGROUND = new Color(20, 20, 20);
	
	// fonts
	private final static Font NORMALFONT = new Font("Trebuchet MS", Font.BOLD, 16);
	
	// image
	private Graphics2D g;
	
	// board
	//TODO Create chessboard and pieces

	// scores
	private int playerScore;
	private int enemyScore;
	
	public GamePanel() {
		super();
		
		//Set up panel
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addMouseListener(this);

		initBoard();
		
		//Set up drawing
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		paintComponent(g);
	}
	
	private void initBoard() {
		//TODO Add pieces to the board, if you haven't already
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
		// Border
		g.drawRect(PADDING, PADDING, SQUARE * 8, SQUARE * 8);
		// White Squares
		for(int i = 0; i < 64; i+=2) {
			g.fillRect(i/8 * SQUARE + PADDING, (i+i/8)%8 * SQUARE + PADDING, SQUARE, SQUARE);
		}
		// Black Squares
		g.setColor(BLACK);
		for(int i = 1; i < 64; i+=2) {
			g.fillRect(i/8 * SQUARE + PADDING, (i+i/8)%8 * SQUARE + PADDING, SQUARE, SQUARE);
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
		//TODO draw the pieces
		
		//Scores
		g.drawString("Your Score: " + playerScore, SQUARE * 8 + PADDING * 2, PADDING * 2);
		g.drawString("Opponent Score: " + enemyScore, SQUARE * 8 + PADDING * 2, PADDING * 4);
	}
	
	//This is what MouseListener is used for
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getX() < PADDING) { return; }
		int x = (e.getX() - PADDING) / SQUARE + 1;
		int y = (e.getY() - PADDING) / SQUARE + 1;

		if(x > 8 || y > 8) {
			//Did not click on board
		} else {
			//TODO Something Happens
			
			//Check click location
			System.out.println("Clicked on square " + (char)(x + 'a' - 1) + y);			

			//Update to Screen
			paintComponent(g);
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
	public void mouseReleased(MouseEvent e) {}
}
