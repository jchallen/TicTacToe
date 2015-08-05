package TicTacToeProject;

import java.util.*;

public class StandardBrain extends Brain implements Thoughts {
	
	private Map <Integer,Set<Integer>> laneMap = new HashMap <Integer,Set<Integer>>();
	private Map <Integer,int[]> squareMap = new HashMap <Integer, int[]>();
	private final String mark;
	
	public StandardBrain(String mark){
		setupLaneMap();
		setupSquareMap();
		this.mark = mark;
	}
	
	@Override
	public int[] makeAMove(Board boardCopy) {
		int[] move = new int[2];
		move[0]=-1;
		move[1]=-1;
		
		String opposingMark = "";
		if (this.mark.equals("X"))
			opposingMark = "O";
		else
			opposingMark = "X";
		

		move=checkWin(move, boardCopy, this.mark);
		if(move[0] != -1)
			return move;
		move=checkBlock(move,boardCopy,opposingMark);
		if(move[0] != -1)
			return move;
		move=checkFork(move,boardCopy,this.mark);
		if(move[0] != -1)
			return move;
		move=checkBlockFork(move,boardCopy,opposingMark);
		if(move[0] != -1)
			return move;
		move=checkCenter(move,boardCopy);
		if(move[0] != -1)
			return move;
		move=checkOppositeCorner(move,boardCopy);
		if(move[0] != -1)
			return move;
		move=checkEmptyCorner(move,boardCopy);
		if(move[0] != -1)
			return move;
		move=checkEmptySide(move,boardCopy);
		if(move[0] != -1)
			return move;
		else
			throw new RuntimeException();
	}
	
	private int[] checkWin(int[] move, Board boardCopy, String newMark){
		Board tempBoard = boardCopy.getBoardCopy();
		
		for(int lane=1;lane<9;lane++){
			if(doesLaneContainOpposingMark(lane, tempBoard, newMark))
				continue;
			else if(!doesLaneContainWinningSquare(lane, tempBoard, newMark))
				continue;
			else{
				Set<Integer> laneToCheck = laneMap.get(lane);
				int[] squareToCheck = new int[2];
				for (int square : laneToCheck){
					squareToCheck = squareMap.get(square);
					try{
						if(tempBoard.getSquare(squareToCheck[0], squareToCheck[1]).equals(" "))
							return squareToCheck;
					}catch (SquareOutOfBoundsException e){
						System.out.println("THIS SHOULD NOT HAPPEN");
					}	
				}
			}
		}
		
		return move;
	}
	
	private int[] checkBlock(int[] move, Board boardCopy, String newMark){
		return checkWin(move,boardCopy,newMark);
	}
	
	private int[] checkFork(int[] move, Board boardCopy, String newMark){
		for (int lane1=1; lane1<8; lane1++){
			for (int lane2 = lane1+1; lane2<9; lane2++){
				if(checkIfLanesIntersect(lane1,lane2)){
					if(checkIfLanesContainFork(lane1,lane2,boardCopy,newMark)){
						return findIntersectingSquare(lane1,lane2);
					}
				}
			}
		}
		
		return move;
	}
	
	private int[] checkBlockFork(int[] move, Board boardCopy, String newMark){
		int numberOfForks = 0;
		int[] potentialBlockingMove1 = new int[2];
		int[] potentialBlockingMove2 = new int[2];
		for (int lane1=1; lane1<8; lane1++){
			for (int lane2 = lane1+1; lane2<9; lane2++){
				if(checkIfLanesIntersect(lane1,lane2)){
					if(checkIfLanesContainFork(lane1,lane2,boardCopy,newMark)){
						numberOfForks += 1;
						if(numberOfForks==1)
							potentialBlockingMove1 = findIntersectingSquare(lane1,lane2);
						if(numberOfForks==2)
							potentialBlockingMove2 = findIntersectingSquare(lane1,lane2);
					}
				}
			}
		}
		try{
			if(numberOfForks == 2){
				for(int[] square : squareMap.values()){
					if(!squaresAreTheSame(square,potentialBlockingMove1))
						if(!squaresAreTheSame(square,potentialBlockingMove2))
							if(boardCopy.getSquare(square[0], square[1]).equals(" "))
								return square;
				}
			} else if (numberOfForks == 1)
				return potentialBlockingMove1;
		}catch (SquareOutOfBoundsException e){
			System.out.println("THIS SHOULD NOT HAPPEN");
		}
		
		return move;
	}
	
	private int[] checkCenter(int[] move, Board boardCopy){
		int numEmptySquares = 0;
		for(int[] square : squareMap.values()){
			try{
				if(boardCopy.getSquare(square[0], square[1]).equals(" "))
					numEmptySquares += 1;
			}catch(SquareOutOfBoundsException e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}
		}
		if(numEmptySquares == 9)
			return squareMap.get(1);
		
		int[] center = squareMap.get(5);
		try{
			if(boardCopy.getSquare(center[0], center[1]).equals(" "))
				return center;
		}catch(SquareOutOfBoundsException e){
			System.out.println("THIS SHOULD NOT HAPPEN");
		}
		
		return move;
	}
	
	private int[] checkOppositeCorner(int[] move, Board boardCopy){
		int[] upperLeftCorner, upperRightCorner, lowerLeftCorner, lowerRightCorner;
		upperLeftCorner = squareMap.get(1);
		upperRightCorner = squareMap.get(3);
		lowerLeftCorner = squareMap.get(7);
		lowerRightCorner = squareMap.get(9);
		String upperLeftContents ="", upperRightContents="", lowerLeftContents="", lowerRightContents="";
		try{
			upperLeftContents = boardCopy.getSquare(upperLeftCorner[0], upperLeftCorner[1]);
			upperRightContents = boardCopy.getSquare(upperRightCorner[0], upperRightCorner[1]);
			lowerLeftContents = boardCopy.getSquare(lowerLeftCorner[0], lowerLeftCorner[1]);
			lowerRightContents = boardCopy.getSquare(lowerRightCorner[0], lowerRightCorner[1]);
		}catch(SquareOutOfBoundsException e){
			System.out.println("THIS SHOULD NOT HAPPEN");
		}
		
		if(upperLeftContents.equals(" ") && !lowerRightContents.equals(" "))
			return upperLeftCorner;
		if(!upperLeftContents.equals(" ") && lowerRightContents.equals(" "))
			return lowerRightCorner;
		if(upperRightContents.equals(" ") && !lowerLeftContents.equals(" "))
			return upperRightCorner;
		if(!upperRightContents.equals(" ") && lowerLeftContents.equals(" "))
			return lowerLeftCorner;
		
		return move;
	}
	
	private int[] checkEmptyCorner (int[] move, Board boardCopy){
		int[] upperLeftCorner, upperRightCorner, lowerLeftCorner, lowerRightCorner;
		upperLeftCorner = squareMap.get(1);
		upperRightCorner = squareMap.get(3);
		lowerLeftCorner = squareMap.get(7);
		lowerRightCorner = squareMap.get(9);
		String upperLeftContents ="", upperRightContents="", lowerLeftContents="", lowerRightContents="";
		try{
			upperLeftContents = boardCopy.getSquare(upperLeftCorner[0], upperLeftCorner[1]);
			upperRightContents = boardCopy.getSquare(upperRightCorner[0], upperRightCorner[1]);
			lowerLeftContents = boardCopy.getSquare(lowerLeftCorner[0], lowerLeftCorner[1]);
			lowerRightContents = boardCopy.getSquare(lowerRightCorner[0], lowerRightCorner[1]);
		}catch(SquareOutOfBoundsException e){
			System.out.println("THIS SHOULD NOT HAPPEN");
		}
		
		if(upperLeftContents.equals(" "))
			return upperLeftCorner;
		if(lowerRightContents.equals(" "))
			return lowerRightCorner;
		if(upperRightContents.equals(" "))
			return upperRightCorner;
		if(lowerLeftContents.equals(" "))
			return lowerLeftCorner;	
		
		return move;
	}
	
	private int[] checkEmptySide (int[] move, Board boardCopy){
		int[] upSide, rightSide, downSide, leftSide;
		upSide = squareMap.get(2);
		rightSide = squareMap.get(6);
		downSide = squareMap.get(8);
		leftSide = squareMap.get(4);
		String upSideContents ="", rightSideContents="", downSideContents="", leftSideContents="";
		try{
			upSideContents = boardCopy.getSquare(upSide[0], upSide[1]);
			rightSideContents = boardCopy.getSquare(rightSide[0], rightSide[1]);
			downSideContents = boardCopy.getSquare(downSide[0], downSide[1]);
			leftSideContents = boardCopy.getSquare(leftSide[0], leftSide[1]);
		}catch(SquareOutOfBoundsException e){
			System.out.println("THIS SHOULD NOT HAPPEN");
		}
		
		if(upSideContents.equals(" "))
			return upSide;
		if(rightSideContents.equals(" "))
			return rightSide;
		if(downSideContents.equals(" "))
			return downSide;
		if(leftSideContents.equals(" "))
			return leftSide;	
		
		return move;
	}
	
	private boolean squaresAreTheSame(int[] square1, int[] square2){
		if(square1[0] == square2[0])
			if(square1[1] == square2[1])
				return true;
		
		return false;
	}
	
	private boolean checkIfLanesIntersect(int lane1, int lane2){
		Set<Integer> laneA = laneMap.get(lane1);
		Set<Integer> laneB = laneMap.get(lane2);
		int intersectingSquare = -1;
		for(int squareFromA : laneA){
			for(int squareFromB : laneB){
				if(squareFromA == squareFromB)
					intersectingSquare = squareFromA;
			}
		}
		
		if(intersectingSquare == -1)
			return false;
		else 
			return true;
	}
	
	private boolean checkIfLanesContainFork(int lane1, int lane2, Board boardCopy, String newMark){
		if(doesLaneContainOpposingMark(lane1,boardCopy,newMark))
			return false;
		if(doesLaneContainOpposingMark(lane2,boardCopy,newMark))
			return false;
		Set<Integer> squaresInLanes = new HashSet<Integer>();
		squaresInLanes.addAll(laneMap.get(lane1));
		squaresInLanes.addAll(laneMap.get(lane2));
		
		int count=0;
		
		for(int square : squaresInLanes){
			int[] squareToCheck = squareMap.get(square);
			try{
				if (boardCopy.getSquare(squareToCheck[0], squareToCheck[1]).equals(newMark))
					count+=1;
			}catch(SquareOutOfBoundsException e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}
		}
		
		if (count > 1)
			return true;
		else
			return false;
		
	}
	
	private int[] findIntersectingSquare(int lane1, int lane2){
		Set<Integer> laneA = laneMap.get(lane1);
		Set<Integer> laneB = laneMap.get(lane2);
		
		int intersectingSquare = -1;
		for(int squareFromA : laneA){
			for(int squareFromB : laneB){
				if(squareFromA == squareFromB)
					intersectingSquare = squareFromA;
			}
		}
		int[] squareToCheck = squareMap.get(intersectingSquare);
		
		return squareToCheck;
		
	}
	
	
	private boolean doesLaneContainWinningSquare(int lane, Board tempBoard, String newMark){
		Set<Integer> laneToCheck = laneMap.get(lane);
		int[] squareToCheck = new int[2];
		int markCount = 0;
		for (int square : laneToCheck){
			squareToCheck = squareMap.get(square);
			try{
				if(tempBoard.getSquare(squareToCheck[0], squareToCheck[1]).equals(newMark))
					markCount += 1;
			}catch (SquareOutOfBoundsException e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}	
		}
		if (markCount == 2)
			return true;
		else return false;
	}
	
	
	//newMark is the players mark. Not the opposing player's mark.
	private boolean doesLaneContainOpposingMark(int lane, Board tempBoard, String newMark){
		Set<Integer> laneToCheck = laneMap.get(lane);
		int[] squareToCheck = new int[2];
		for (int square : laneToCheck){
			squareToCheck = squareMap.get(square);
			try{
				if(!tempBoard.getSquare(squareToCheck[0], squareToCheck[1]).equals(newMark) &&
				   !tempBoard.getSquare(squareToCheck[0], squareToCheck[1]).equals(" "))
					return true;
			}catch (SquareOutOfBoundsException e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}	
		}
		return false;
	}
	
	private void setupLaneMap(){
		setupLaneMapHelper(1, 1,2,3);
		setupLaneMapHelper(2, 4,5,6);
		setupLaneMapHelper(3, 7,8,9);
		setupLaneMapHelper(4, 1,5,9);
		setupLaneMapHelper(5, 1,4,7);
		setupLaneMapHelper(6, 2,5,8);
		setupLaneMapHelper(7, 3,6,9);
		setupLaneMapHelper(8, 3,5,7);
	}
	private void setupLaneMapHelper(int lane, int squareA, int squareB, int squareC ){
		Set<Integer> value = new HashSet<Integer>();
		value.add(squareA);
		value.add(squareB);
		value.add(squareC);
		laneMap.put(lane, value);
	}
	
	private void setupSquareMap(){
		setupSquareMapHelper(1, 0,0);
		setupSquareMapHelper(2, 0,1);
		setupSquareMapHelper(3, 0,2);
		setupSquareMapHelper(4, 1,0);
		setupSquareMapHelper(5, 1,1);
		setupSquareMapHelper(6, 1,2);
		setupSquareMapHelper(7, 2,0);
		setupSquareMapHelper(8, 2,1);
		setupSquareMapHelper(9, 2,2);
	}
	
	private void setupSquareMapHelper(int square, int x, int y){
		int[] value = new int[2];
		value[0] = x;
		value[1] = y;
		squareMap.put(square, value);
	}
	
}
