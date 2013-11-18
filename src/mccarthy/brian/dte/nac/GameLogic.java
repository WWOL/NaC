package mccarthy.brian.dte.nac;

/**
 * Interface for GameLogic, the can be extended by 3rd parties to provide
 * custom logic
 * @author Brian McCarthy
 */
public abstract class GameLogic {

	public abstract void placeComputerPiece();
	
	/**
	 * This should return a unique string for each logic type.
	 * @return id
	 */
	public abstract String getId();
	
	public String toString() {
		return getId();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof GameLogic)) {
			return false;
		}
		GameLogic other = (GameLogic) o;
		return getId().equals(other.getId());
	}
	
}