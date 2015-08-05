package TicTacToeProject;

public class BasicComputerPlayer implements Player {
	
	private String name = "";
	private Brain brain;
	private String mark = "";
	private boolean isADummy=false;
	
	public BasicComputerPlayer(String brainType, String name) throws ResettingNameException,
																	 InvalidNameException,
																	 InvalidBasicComputerNameException{
		if (name != "computerplayer1" &&
			name != "computerplayer2")
			throw new InvalidBasicComputerNameException("This should be either 'ComputerPlayer1' or 'ComputerPlayer2', not: "+name);
		setName(name);
		if(getName() == "computerplayer1")
			this.mark = "X";
		if(getName() == "computerplayer2")
			this.mark = "O";
		if (brainType == "standard")
			this.brain = new StandardBrain(this.mark);
	}
	
	public BasicComputerPlayer(){
		this.isADummy = true;
	}

	@Override
	public int[] makeAMove(Board boardCopy) throws IsADummyException {
		if(this.isADummy)
			throw new IsADummyException();
		return (this.brain.makeAMove(boardCopy));
		
	}

	@Override
	public String getName() throws NoNameException{
		if (this.name == "")
			throw new NoNameException("Player " + this.hashCode() + " does not have a name");
		
		return this.name;
	}

	@Override
	public void setName(String newName) throws ResettingNameException, InvalidNameException {
		if(this.name != "")
			throw new ResettingNameException("Player " + this.name + "attempted to change their name to " + newName);
		if(newName == "")
			throw new InvalidNameException("Player attempted to use an invalid name");
		
		this.name = newName;
	}
	

}
