package TicTacToeProject;

public abstract interface Player {
	
	public abstract int[] makeAMove(Board boardCopy) throws IsADummyException;
	
	public abstract String getName() throws NoNameException;
	public abstract void setName(String newName) throws ResettingNameException,InvalidNameException;
}
