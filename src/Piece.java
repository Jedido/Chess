import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Piece {
	public static final int KING = 0;
	public static final int QUEEN = 1;
	public static final int BISHOP = 2;
	public static final int KNIGHT = 3;
	public static final int ROOK = 4;
	public static final int PAWN = 5;
	
	public static BufferedImage[][] sprite = new BufferedImage[2][6];
	{
		//This parses in the sprite image under Resources
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Chesspieces.png"));
			for(int i  = 0; i < 2; i++) {
				for(int j = 0; j < 6; j++) {
					sprite[i][j] = spritesheet.getSubimage(j * 50, i * 50, 50, 50);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
	
	private int value;
	private int type;
	private int side;
	
	public Piece(int type, int side) {
		this.type = type;
		this.side = side;
		switch(type) {
		case(PAWN) : value = 1; break;
		case(KNIGHT) : value = 3; break;
		case(BISHOP) : value = 3; break;
		case(ROOK) : value = 5; break;
		case(QUEEN) : value = 9; break;
//		case(KING) : value = 0; break;
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean isSide(int side) {
		return this.side == side;
	}
	
	public static Iterable<Point> possibleMoves(int x, int y, Piece[][] board) {
		ArrayList<Point> moves = new ArrayList<Point>();
		if(board[x][y] == null) {
			return null;
		}
		Piece p = board[x][y];
		switch(p.type) {
		case(PAWN) : {
			int displace = p.side == 1 ? 1 : -1;
			//Special case
			if((y - displace) % 7 == 0 && board[x][y + displace * 2] == null && board[x][y + displace] == null) {
				moves.add(new Point(x, y + displace * 2));
			}
			if(board[x][y + displace] == null) {
				moves.add(new Point(x, y + displace));
			}
			if(onBoard(x + 1, y + displace) && board[x + 1][y + displace] != null) {
				p.addSpace(x + 1, y + displace, board, moves);
			}
			if(onBoard(x - 1, y + displace) && board[x - 1][y + displace] != null) {
				p.addSpace(x - 1, y + displace, board, moves);
			}
			break;
		}
		case(KNIGHT) : {
			p.addSpace(x + 1, y + 2, board, moves);
			p.addSpace(x + 1, y - 2, board, moves);
			p.addSpace(x - 1, y + 2, board, moves);
			p.addSpace(x - 1, y - 2, board, moves);
			p.addSpace(x + 2, y + 1, board, moves);
			p.addSpace(x + 2, y - 1, board, moves);
			p.addSpace(x - 2, y + 1, board, moves);
			p.addSpace(x - 2, y - 1, board, moves);
			break;
		}
		case(BISHOP) : {
			int i = x;
			int j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i++;
				j++;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i--;
				j++;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i++;
				j--;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i--;
				j--;
				p.addSpace(i, j, board, moves);
			}
			break;
		}
		case(ROOK) : {
			int i = x;
			int j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i++;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i--;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			while(onBoard(i, j) && (j == y || board[i][j] == null)) {
				j++;
				p.addSpace(i, j, board, moves);
			}
			j = y;
			while(onBoard(i, j) && (j == y || board[i][j] == null)) {
				j--;
				p.addSpace(i, j, board, moves);
			}
			break;
		}
		case(QUEEN) : {
			int i = x;
			int j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i++;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i--;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			while(onBoard(i, j) && (j == y || board[i][j] == null)) {
				j++;
				p.addSpace(i, j, board, moves);
			}
			j = y;
			while(onBoard(i, j) && (j == y || board[i][j] == null)) {
				j--;
				p.addSpace(i, j, board, moves);
			}
			j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i++;
				j++;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i--;
				j++;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i++;
				j--;
				p.addSpace(i, j, board, moves);
			}
			i = x;
			j = y;
			while(onBoard(i, j) && (i == x || board[i][j] == null)) {
				i--;
				j--;
				p.addSpace(i, j, board, moves);
			}
			break;
		}
		case(KING) : {
			//TODO Castling
			p.addSpace(x + 1, y, board, moves);
			p.addSpace(x + 1, y + 1, board, moves);
			p.addSpace(x + 1, y - 1, board, moves);
			p.addSpace(x - 1, y - 1, board, moves);
			p.addSpace(x - 1, y, board, moves);
			p.addSpace(x - 1, y + 1, board, moves);
			p.addSpace(x, y + 1, board, moves);
			p.addSpace(x, y - 1, board, moves);
		}
		}
		return moves;
	}
	
	//Do not use for pawns
	private void addSpace(int x, int y, Piece[][] board, ArrayList<Point> moves) {
		if(onBoard(x, y) && (board[x][y] == null || side != board[x][y].side))
			moves.add(new Point(x, y));
	}
	
	private static boolean onBoard(int x, int y) {
		return x < 8 && x >=0 && y < 8 && y >= 0;
	}
	
	public boolean validMove(int originalX, int originalY, int toX, int toY, Piece[][] chessboard) {
		Piece p = chessboard[originalX][originalY];
//		if(onBoard(toX, toY) && p.side == chessboard[toX][toY].side) {
//			return false;
//		}
		for(Point point : possibleMoves(originalX, originalY, chessboard)) {
			if(point.x == toX && point.y == toY) {
				return true;
			}
		}
		return false;
	}
	
	public void draw(Graphics g, int x, int y){
		g.drawImage(sprite[side][type], x, y, null);
	}
}
