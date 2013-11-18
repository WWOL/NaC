package mccarthy.brian.dte.nac;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Stores the data of the board
 *
 * @author Brian McCarthy
 */
public class GameBoard {

	private char[] data;
	private int gamesPlayed;
	private int gamesWon;
	private int gamesDrawn;
	Properties props;
	private GameLogic logic;
	private boolean playersTurn;
	private boolean autoTurn;
	private List<GameLogic> logicList;
	
	// The triples along which you can place three pieces to win
	private int[][] winLines;

	private long computerTurnNanos;
	private int computerTurns;
	
	public GameBoard() {
		// 9 cells in a 3x3 board
		data = new char[9];
		/* 
		 * Make sure the board is empty, also fills array with ' ' rather than
		 * nothing, this represents nothing
		 */
		clear();
		// Setup scores to 0
		gamesPlayed = 0;
		gamesWon = 0;
		gamesDrawn = 0;
		props = new Properties();
		// The default logic, can be changed later from props of game
		logic = new GameLogic2();
		// Start with players turn
		playersTurn = true;
		autoTurn = false;
		logicList = new ArrayList<GameLogic>();
		winLines = new int[][] {{0, 1, 2}, {0, 3, 6}, {0, 4, 8}, {1, 4, 7}, {2, 5, 8}, {2, 4, 6}, {3, 4, 5}, {6, 7, 8}};
		computerTurnNanos = 0;
		computerTurns = 0;
		try {
			props.load(new FileInputStream("data.txt"));
		} catch (Exception e) {
			System.out.println("Error while loading data!");
			e.printStackTrace();
			return;
		}
		load();
	}

	public void clear() {
		Arrays.fill(data, ' ');
	}

	/**
	 * Resets scores and saves them
	 * This is used when clicking the Reset button and also clearing scores 
	 */
	public void resetScores() {
		gamesPlayed = 0;
		gamesWon = 0;
		gamesDrawn = 0;
		save();
	}

	/**
	 * Get piece from cell
	 * @param Index cell to get piece from
	 * @return Char at index 
	 * @throws IllegalArgumentException if index out of bounds
	 */
	public char getPieceAt(int index) {
		if (index < 0 || index > 8) {
			throw new IllegalArgumentException("Index must be between 0 and 8");
		}
		return data[index];
	}

	/**
	 * Set a piece at a certain cell
	 * @param index Cell to set piece at, must be between 0 and 8
	 * @param piece The char piece to set, 'x' or 'o'
	 * @throws IllegalArgumentException if index out of bounds or piece invalid
	 */
	public void setPieceAt(int index, char piece) {
		if (index < 0 || index > 8) {
			throw new IllegalArgumentException("Index must be between 0 and 8 inclusive");
		}
		if (piece != 'x' && piece != 'o') {
			throw new IllegalArgumentException("Piece must be 'x' or 'o'. Was " + piece);
		}
		data[index] = piece;
	}
	
	/**
	 * Place the computers piece, using the set logic type
	 */
	public void placeComputerPiece() {
		long startTime = System.nanoTime();
		logic.placeComputerPiece();
		playersTurn = true;
		long totalTime = System.nanoTime() - startTime;
		computerTurnNanos += totalTime;
		computerTurns++;
	}
	
	/**
	 * Check the current board for a win and handle it
	 */
	public void checkForWin() {
		handleWinState(getWinState());
	}
	
	/**
	 * Get a {@link WinState} for the board
	 * @return WinState of current board
	 */
	public WinState getWinState() {
		for (int[] triple : winLines) {
			StringBuilder line = new StringBuilder();
			for (int i : triple) {
				line.append(getPieceAt(i));
			}
			if (line.toString().equals("xxx")) {
				System.out.println("Computer Turn Time: " + computerTurnNanos);
				return WinState.PLAYER_WIN;
			} else if (line.toString().equals("ooo")) {
				System.out.println("Computer Turn Time: " + computerTurnNanos);
				return WinState.COMPUTER_WIN;
			}
		}
		if (isBoardFull()) {
			System.out.println("Computer Turn Time: " + computerTurnNanos);
			return WinState.DRAW;
		}
		return WinState.NO_RESULT;
	}
	
	/**
	 * Handle a WinState, if it isn't a win ignore it, else show message and
	 * clear board
	 * @param winState The WinState
	 * @see #getWinState()
	 */
	public void handleWinState(WinState winState) {
		if (winState != WinState.NO_RESULT) {
			String message = "";
			if (winState == WinState.DRAW) {
				gameDrawn();
				message = "You drew the game";
			} else if (winState == WinState.PLAYER_WIN) {
				gameWon();
				message = "You won the game!";
			} else if (winState == WinState.COMPUTER_WIN) {
				gamePlayed();
				message = "You lost the game!";
			}
			JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
			clear();
			GameRunner.repaint();
			System.out.println("Average computer turn time: " + computerTurnNanos / computerTurns);
			computerTurnNanos = 0;
			computerTurns = 0;
		}
	}
	
	/**
	 * Check if the board is full, IE no ' ' left
	 * @return true if there are no ' ' left
	 */
	public boolean isBoardFull() {
		for (char piece : data) {
			if (piece == ' ') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Saves the game data, scores and options
	 */
	public void save() {
		props.setProperty("gamesPlayed", String.valueOf(gamesPlayed));
		props.setProperty("gamesWon", String.valueOf(gamesWon));
		props.setProperty("gamesDrawn", String.valueOf(gamesDrawn));
		try {
			props.store(new FileOutputStream("data.txt"), "Noughts and Crosses saved scores.");
		} catch (Exception e) {
			System.out.println("Error while saving scores!");
			e.printStackTrace();
			return;
		}
		System.out.println("Saved scores.");
	}

	/**
	 * Loads scores and properties from the data file
	 */
	public void load() {
		// If the file is empty (or contains no valid properties) ignore it
		if (props.isEmpty()) {
			return;
		}
		try {
			gamesPlayed = Integer.parseInt(props.getProperty("gamesPlayed", "0"));
			gamesWon = Integer.parseInt(props.getProperty("gamesWon", "0"));
			gamesDrawn = Integer.parseInt(props.getProperty("gamesDrawn", "0"));
			autoTurn = Boolean.parseBoolean(props.getProperty("autoTurn", "false"));
			String selectedLogic = props.getProperty("logic", "GameLogic2");
			System.out.println("sL: " + selectedLogic);
			logicList.add(new GameLogic1());
			logicList.add(new GameLogic2());
			logicList.add(new GameLogicError()); // Not really needed.
			for (GameLogic logicName : logicList) {
			    if (logicName.getId().equals(selectedLogic)) {
			        System.out.println("lN: " + logicName);
			        logic = logicName;
			    }
			}
			if (logic == null) {
				System.out.println("Error loading game logic!");
				logic = new GameLogicError();
			}
			System.out.println("l: " + logic);
		} catch (Exception e) {
			System.out.println("Error while loading data!");
			e.printStackTrace();
		}
		System.out.println("lL: " + logic);
	}

	/**
	 * Get the board in a string representation
	 * This was mainly used in debugging
	 * @return String with ' ' represented by '*'
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (char piece : data) {
			if (piece == ' ') {
				piece = '*';
			}
			sb.append(piece);
		}
		return sb.toString();
	}
	
	public GameLogic getGameLogicById(String id) {
		for (GameLogic logic : logicList) {
			if (logic.getId().equals(id)) {
				return logic;
			}
		}
		return null;
	}
	
	public GameLogic getLogic() {
	    return logic;
	}
	
	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public int getGamesDrawn() {
		return gamesDrawn;
	}
	
	public boolean isPlayersTurn() {
		return playersTurn;
	}
	
	public void setPlayersTurn(boolean turn) {
		playersTurn = turn;
	}

	public void gameWon() {
		gamesPlayed++;
		gamesWon++;
	}

	public void gameDrawn() {
		gamesPlayed++;
		gamesDrawn++;
	}

	public void gamePlayed() {
		gamesPlayed++;
	}
	
	public void setLogicType(String id) {
	    System.out.println("Changing logic to " + id);
		for (GameLogic logic : logicList) {
			if (logic.getId().equals(id)) {
				System.out.println("Changed from logic " + this.logic.getId() + " to " + logic.getId());
				this.logic = logic;
			}
		}
	}
	
	public void setLogicType(GameLogic logic) {
		this.logic = logic;
	}

	/**
	 * Get if the computer should automatically place its piece after the
	 * player made their move
	 * @return If the computer should automatically play
	 */
	public boolean isAutoTurn() {
		return autoTurn;
	}
	
	public List<GameLogic> getLogicList() {
	    return logicList;
	}

}
