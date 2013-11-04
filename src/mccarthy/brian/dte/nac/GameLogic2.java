package mccarthy.brian.dte.nac;

/**
 * GameLogic second version, thinks about its move
 * 
 * @author Brian McCarthy
 */
public class GameLogic2 extends GameLogic {

	private int[][] winLines = new int[][] {{0, 1, 2}, {0, 3, 6}, {0, 4, 8}, {1, 4, 7}, {2, 5, 8}, {2, 4, 6}, {3, 4, 5}, {6, 7, 8}};
	private GameLogic fallback = new GameLogic1();

	@Override
	public void placeComputerPiece() {
		for (int[] triple : winLines) {
			StringBuilder line = new StringBuilder();
			for (int i : triple) {
				line.append(GameRunner.getGameBoard().getPieceAt(i));
			}
			if (!line.toString().contains(" ")) {
				// There are no space in the line, skip it
				continue;
			}

			if (count('o', line.toString()) == 2) {
				// We can make a line of 3
				int index = triple[line.indexOf(" ")];
				GameRunner.getGameBoard().setPieceAt(index, 'o');
				GameRunner.repaint();
				System.out.println("r1");
				return;
			}
			if (count('x', line.toString()) == 2) {
				// Block x from getting a line of 3
				int index = triple[line.indexOf(" ")];
				GameRunner.getGameBoard().setPieceAt(index, 'o');
				GameRunner.repaint();
				System.out.println("r2");
				return;
			}
			if (count('x', GameRunner.getGameBoard().toString()) == 1) {
				// First move, or second if o started
				for (int i : triple) {
					if (i == 0 || i == 2 || i == 6 || i == 8) {
						if (GameRunner.getGameBoard().getPieceAt(i) == 'x') {
							if (GameRunner.getGameBoard().getPieceAt(4) == ' ') {
								GameRunner.getGameBoard().setPieceAt(4, 'o');
								GameRunner.repaint();
								System.out.println("r3");
								return;
							}
						}
					}
					/*
					 * The next three rules stop the user from getting a fork
					 * by making an L shape starting in the middle outside
					 * cells.
					 */
					if (i == 5) {
						if (GameRunner.getGameBoard().getPieceAt(i) == 'x') {
							GameRunner.getGameBoard().setPieceAt(2, 'o');
							GameRunner.repaint();
							System.out.println("r4");
							return;
						}
					}
					if (i == 7) {
						if (GameRunner.getGameBoard().getPieceAt(i) == 'x') {
							GameRunner.getGameBoard().setPieceAt(8, 'o');
							GameRunner.repaint();
							System.out.println("r5");
							return;
						}
					}
					if (i == 3) {
						if (GameRunner.getGameBoard().getPieceAt(i) == 'x') {
							GameRunner.getGameBoard().setPieceAt(6, 'o');
							GameRunner.repaint();
							System.out.println("r6");
							return;
						}
					}
				}
			}
		}
		System.out.println("r7");
		// Fall back for any unhandled moves
		fallback.placeComputerPiece();
	}

	@Override
	public String getHumanName() {
		return "Game Logic Type 2";
	}

	public int count(char piece, String line) {
		int count = 0;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == piece) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	public String getId() {
		return "Native_2";
	}
	
	public String toString() {
		return getHumanName();
	}

}
