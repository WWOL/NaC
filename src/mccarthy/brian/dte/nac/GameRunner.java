package mccarthy.brian.dte.nac;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class sets up and runs the game
 * 
 * @author Brian McCarthy
 */
public class GameRunner {

	/**
	 * This is a static class variable because we need access to it 
	 * (via getGameBoard()) to do thing like place pieces
	 */
	private static GameBoard board;
	/**
	 * This is a static class variable because we need it to repaint the
	 * window when a piece has been placed
	 */
	private static GameBoardPanel gameBoardPanel;
	
	/**
	 * This is the entry point for the application
	 */
	public static void main(String[] args) {		
	    createDataFile();
	    board = new GameBoard();
		

		JFrame f = new JFrame();
		gameBoardPanel = new GameBoardPanel();
		JPanel controlsPanel = new ControlsPanel();
		// Exiting and saving are handled by the WindowListener
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Use absolute layout
		// This means every component must have a location and size set
		f.setLayout(null);
		// No border or close buttons
		f.setUndecorated(true);
		// Add the board and control area to the frame
		f.getContentPane().add(gameBoardPanel);
		f.getContentPane().add(controlsPanel);
		f.setSize(800, 600);
		// Because there is no title bar this only appears in the start bar
		f.setTitle("Noughts and Crosses");
		// Places the frame in the center
		f.setLocationRelativeTo(null);
		// Listen for closing events
		// This listener could be in this class but that would require an
		// instance of this class (everything is static now)
		// Also, the GameBoardPanel class is already a mouse listener for itself
		f.addWindowListener(gameBoardPanel);
		// Finally, show the frame
		f.setVisible(true);
	}

	/**
	 * Creates the data.txt file if it does not exist
	 */
	private static void createDataFile() {
		File file = new File("data.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.newLine();
				writer.write("logic=Native_2");
				writer.newLine();
				writer.write("autoTurn=false");
                writer.newLine();
				writer.flush();
				writer.close();
			} catch (Exception e) {
				System.out.println("Error creating the scores file!\nYou will not be able to save scores.");
				e.printStackTrace();
			}
		}
	}

	public static GameBoard getGameBoard() {
		return board;
	}

	/**
	 * Repaints the frame
	 * This needs to be done every time a piece is added
	 */
	public static void repaint() {
		gameBoardPanel.repaint();
	}

	/**
	 * Get the help text, shown in the Help / About dialogue
	 * @return Multiple lines of the help text 
	 */
	public static String getHelpText() {
		StringBuilder sb = new StringBuilder();
		sb.append("Help:\n");
		sb.append(" Welcome to Noughts and Crosses!\n");
		sb.append(" Thank you for flying Brian McCarthy Programmers.\n");
		sb.append(" We hope you enjoy your flight!\n\n");
		sb.append("Operation:\n");
		sb.append(" Click in a square to place you piece.\n");
		sb.append(" Click Step to make to computer take their turn.\n\n");
		sb.append("Data.txt:\n");
		sb.append(" Set autoTurn to true in the data.txt file to make the computer take their turn automatically,\n");
		sb.append(" Set logic to one of the options in the selector to change the default logic.\n\n");
		return sb.toString();
	}

	/**
	 * Get the about text, shown in the Help / About dialogue
	 * @return Multiple lines of the about text
	 */
	public static String getAboutText() {
		StringBuilder sb = new StringBuilder();
		sb.append("About:\n");
		sb.append(" Version: ");
		sb.append(getVersion());
		sb.append("\n");
		sb.append(" Made by Brian McCarthy!\n\n");
		return sb.toString();
	}

	/**
	 * Get the scores text, shown in the scores dialogue
	 * Data includes Won, Drawn, Lost and Total
	 * @return Multiple lines of the scores text
	 */
	public static String getScoresInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Scores:\n");
		sb.append(" Won: ");
		sb.append(getGameBoard().getGamesWon());
		sb.append("\n");
		sb.append(" Drawn: ");
		sb.append(getGameBoard().getGamesDrawn());
		sb.append("\n");
		sb.append(" Lost: ");
		sb.append(getGameBoard().getGamesPlayed() - getGameBoard().getGamesWon() - getGameBoard().getGamesDrawn());
		sb.append("\n\n");
		sb.append(" Total: ");
		sb.append(getGameBoard().getGamesPlayed());
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Get the current version, this can be incremented for later releases
	 * Uses Semantic Versioning 2.0.0
	 * @see http://semver.org/
	 * @return The current version
	 */
	public static String getVersion() {
		return "0.0.1";
	}

	/**
	 * Save scores and exit
	 */
	public static void exit() {
		getGameBoard().save();
		System.out.println("Exiting...");
		System.exit(0);
	}

}
