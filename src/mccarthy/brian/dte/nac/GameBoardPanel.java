package mccarthy.brian.dte.nac;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JPanel;

public class GameBoardPanel extends JPanel implements MouseListener, WindowListener {

	private static final long serialVersionUID = 1L;

	public GameBoardPanel() {
		setLocation(0, 0);
		setSize(600, 600);
		addMouseListener(this);
	}
	
	protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.RED);
        for (int i = 0; i < 9; i++) {
        	char piece = GameRunner.getGameBoard().getPieceAt(i);
        	if (piece == 'x') {
        		drawX(i, g2);
        	} else if (piece == 'o') {
        		drawO(i, g2);
        	} else if (piece == ' ') {
        		continue;
        	} else {
        		// Draw a grey cross to symbolise an error
        		g2.setStroke(new BasicStroke(5));
                g2.setColor(Color.DARK_GRAY);
                drawX(i, g2);
                g2.setStroke(new BasicStroke(3));
                g2.setColor(Color.RED);
        	}
        }
        g2.setPaint(Color.BLUE);
        g2.drawLine(0, 200, 600, 200);
		g2.drawLine(0, 400, 600, 400);
        g2.drawLine(200, 0, 200, 600);
		g2.drawLine(400, 0, 400, 600);
	}
	
	public Point getPointFromIndex(int index) {
		switch(index) {
		case 0:
			return new Point(0, 0);
		case 1:
			return new Point(200, 0);
		case 2:
			return new Point(400, 0);
		case 3:
			return new Point(0, 200);
		case 4:
			return new Point(200, 200);
		case 5:
			return new Point(400, 200);
		case 6:
			return new Point(0, 400);
		case 7:
			return new Point(200, 400);
		case 8:
			return new Point(400, 400);
		default:
			return new Point(0, 0);	
		}
	}
	
	public void drawX(int index, Graphics graphics) {
		Point origin = getPointFromIndex(index);
		graphics.drawLine(origin.x, origin.y, origin.x + 200, origin.y + 200);
		graphics.drawLine(origin.x + 200, origin.y, origin.x, origin.y + 200);
	}
	
	public void drawO(int index, Graphics graphics) {
		Point origin = getPointFromIndex(index);
		graphics.drawOval(origin.x, origin.y, 200, 200);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!GameRunner.getGameBoard().isPlayersTurn()) {
			
			return;
		}
		int xCol = e.getX() / 200;
		int yCol = e.getY() / 200;
		//System.out.println("x: " + e.getX() + ", y: " + e.getY());
		//System.out.println("xCol: " + xCol + ", yCol: " + yCol);
		int index = indexFromColumns(xCol, yCol);
		char piece = GameRunner.getGameBoard().getPieceAt(index);
		if (piece != ' ') {
			return;
		}
		GameRunner.getGameBoard().setPieceAt(index, 'x');
		GameRunner.repaint();
		GameRunner.getGameBoard().setPlayersTurn(false);
		GameRunner.getGameBoard().checkForWin();
		if(GameRunner.getGameBoard().isAutoTurn()) {
			GameRunner.getGameBoard().placeComputerPiece();
		}
	}
	
	public int indexFromColumns(int xCol, int yCol) {
		int index = 0;
		index += yCol * 3;
		index += xCol;
		return index;
	}

	// The empty methods are required because of the implemented interfaces
	
	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	// NOTE: These window listener methods are for the frame, not the panel
	
	@Override
	public void windowActivated(WindowEvent e) { }

	@Override
	public void windowClosed(WindowEvent e) { }

	@Override
	public void windowClosing(WindowEvent e) {
		GameRunner.exit();
	}

	@Override
	public void windowDeactivated(WindowEvent e) { }

	@Override
	public void windowDeiconified(WindowEvent e) { }

	@Override
	public void windowIconified(WindowEvent e) { }

	@Override
	public void windowOpened(WindowEvent e) { }
	
}
