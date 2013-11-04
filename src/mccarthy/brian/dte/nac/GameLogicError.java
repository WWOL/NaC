package mccarthy.brian.dte.nac;

import javax.swing.JOptionPane;

/**
 * This class is used to represent that an error occurred while setting game logic
 * It does nothing to player the game, just reports there is an error
 * 
 * @author Brian McCarthy
 */
public class GameLogicError extends GameLogic {

	@Override
	public void placeComputerPiece() {
		JOptionPane.showMessageDialog(null, "There was an error setting the game logic class.\nCheck the data.txt file.");
	}

	@Override
	public String getHumanName() {
		return "GameLogic Error!";
	}

	@Override
	public String getId() {
		return "Native_Error";
	}

}
