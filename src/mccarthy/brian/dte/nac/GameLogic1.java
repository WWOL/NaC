package mccarthy.brian.dte.nac;

import javax.swing.JOptionPane;

/**
 * GameLogic first version, simple
 * 
 * @author Brian McCarthy
 */
public class GameLogic1 extends GameLogic {

	@Override
	public void placeComputerPiece() {
		for (int i = 0; i < 9; i++) {
			char piece = GameRunner.getGameBoard().getPieceAt(i);
			if (piece == ' ') {
				GameRunner.getGameBoard().setPieceAt(i, 'o');
				GameRunner.repaint();
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "No more moves available.\nThis is an error.", "No more moves.", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public String getId() {
		return "Native_1";
	}

}