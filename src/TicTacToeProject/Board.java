package TicTacToeProject;

public class Board {
	
	private String[][] board = new String[3][3];
	
	public Board(){
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				this.board[i][j]=" ";
			}
		}
	}
	
	
	public String getSquare(int i, int j) throws SquareOutOfBoundsException{
		if ( i < 0 ||
			 i > 2 ||
			 j < 0 ||
			 j > 2)
			throw new SquareOutOfBoundsException("Attempted to access an out of bounds square at: " + i +"," + j);
		return board[i][j];
	}
	
	public void setSquare(int i, int j, String marker) throws ResettingSquareException,
															  InvalidSquareMarkerException,
															  SquareOutOfBoundsException{
		if (!getSquare(i,j).equals(" "))
			throw new ResettingSquareException("Attempted to change marked square at: " + i +"," + j);
		if (!marker.equals("X") && !marker.equals("O"))
			throw new InvalidSquareMarkerException("Attemped to use an invalid marker: "+marker+" at "+ i +"," + j);
		if ( i < 0 ||
		     i > 2 ||
			 j < 0 ||
			 j > 2)
			throw new SquareOutOfBoundsException("Attempted to access an out of bounds square at: " + i +"," + j);
		this.board[i][j] = marker;
	}
	
	private Board(Board originalBoard){
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				try{
					this.board[i][j] = originalBoard.getSquare(i,j);
				} catch (Exception e){
					String message = "**********************\n" +
									 "THIS SHOULD NOT HAPPEN.\n" +
									 "**********************\n" + e;
					System.out.println(message);
					System.exit(1);
				}
			}
		}
	}
	
	public Board getBoardCopy(){
		Board boardCopy = new Board(this);
		
		return boardCopy;
	}
	
	public void ResetBoard(){
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				this.board[i][j]=" ";
			}
		}
	}
	
}
