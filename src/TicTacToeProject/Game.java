package TicTacToeProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Game {
	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true){
			System.out.print("Hello, Welcome to John's unbeatable tic-tac-toe.\n"
					+ "=================================================\n"
					+ "The first thing I'll ask you to do is choose who will be playing.\n"
					+ "--If you would like to play a single player game (you versus the computer),\n"
					+ "  please type: human1\n"
					+ "--If you would like to play against a friend,\n"
					+ "  pleast type: human2\n"
					+ "--If instead you'd like to see two of our expertly trained computers duke it out,\n"
					+ "  please type: computer\n"
					+ "Then press enter.\n");
			String gameType = br.readLine();
			while(!isValidGameType(gameType)){
				System.out.println("I'm sorry, I don't understand that word.\n"
						+ "please enter in human1 (to play against the computer),\n"
						+ "                human2 (to play against another person),\n"
						+ "                computer (to watch two computers play against each other).\n");
				gameType = br.readLine();
			}
			System.out.println();
			
			System.out.print("Wonderful! Now I'll set your game up, but in the mean time\n"
					+ "let me explain how this game works.\n"
					+ "All the normal rules of tic-tac-toe still apply.\n"
					+ "When it is your turn, enter in the number square that you would like to play.\n"
					+ "[ 1 ][ 2 ][ 3 ]\n[ 4 ][ 5 ][ 6 ]\n[ 7 ][ 8 ][ 9 ]\n"
					+ "For example, if you wanted to play in the upper left corner, you would type in: 1\n"
					+ "After every turn I will show you the current board.\n"
					+ "I have set up the game for you, so please enter any key and we'll begin.\n");
			br.readLine();
			Board board = new Board();
			if(gameType.equals("human1")){
				while(true){
					playSinglePlayer(board);
					System.out.print("would you like to play against the computer again?\nEnter no if you do not\n");
					if(br.readLine().equals("no"))
						break;
					board = new Board();
				}
			}else if(gameType.equals("human2")){
				while(true){
					playAgainstFriend(board);
					System.out.print("would you like to play against a friend again?\nEnter no if you do not\n");
					if(br.readLine().equals("no"))
						break;
					board = new Board();
				}
			}else if(gameType.equals("computer")){
				while(true){
					computerDuel(board);
					System.out.print("would you like to watch the computers duel again?\nEnter no if you do not\n");
					if(br.readLine().equals("no"))
						break;
					board = new Board();
				}
			}else{throw new RuntimeException();}
			
			System.out.println("Thank you for playing!\n Please enter quit to end the program\n"
					+ "enter any other key to restart the program.");
			String exitCommand = br.readLine();
			if(exitCommand.equals("quit"))
				break;
			board = new Board();
        }
        br.close();
        System.exit(0);
	}
	
	public static boolean isValidGameType(String gameType){
		if(gameType.equals("human1") || gameType.equals("human2") || gameType.equals("computer"))
			return true;
		else
			return false;
	}
	
	//Returns "tie" if it's a tie, "not over" if game is not over,  else returns mark of winner
	public static String checkGameOver(Board board, Map <Integer,Set<Integer>> laneMap, Map <Integer,int[]> squareMap){
		int a=0,b=0,c=0;
		for(Set<Integer> lane : laneMap.values()){
			Iterator<Integer> iter = lane.iterator();
			a=iter.next();
			b=iter.next();
			c=iter.next();
		
			int[] aSquare,bSquare,cSquare;
			aSquare = squareMap.get(a);
			bSquare = squareMap.get(b);
			cSquare = squareMap.get(c);
		
			String aSquareContents="",bSquareContents="",cSquareContents="";
			try{
				aSquareContents = board.getSquare(aSquare[0],aSquare[1]);
				bSquareContents = board.getSquare(bSquare[0],bSquare[1]);
				cSquareContents = board.getSquare(cSquare[0],cSquare[1]);
			}catch(SquareOutOfBoundsException e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			} 
			
			if(!aSquareContents.equals(" "))
				if(aSquareContents.equals(bSquareContents) && aSquareContents.equals(cSquareContents))
					return aSquareContents;
		}
		
		for(int[] square : squareMap.values()){
			try{
				if(board.getSquare(square[0], square[1]).equals(" "))
					return "not over";
			}catch(SquareOutOfBoundsException e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}
		}
		
		return "tie";
	}
	
	//returns false if failed to make a move, or true if it successfully made a move.
	public static boolean makeAMove(String move, Board board, Map <Integer,int[]> squareMap, String marker){
		try{
			Integer.parseInt(move);
		}catch (Exception e){return false;}
		
		if(Integer.parseInt(move)<1 || Integer.parseInt(move) > 9)
			return false;
		try{
			int[] square = squareMap.get(Integer.parseInt(move));
			board.setSquare(square[0], square[1], marker);
		}catch (InvalidSquareMarkerException e){
			return false;
		}catch (ResettingSquareException e){
			return false;
		}catch (SquareOutOfBoundsException e){
			return false;
		}
		
		return true;
	}
	
	public static boolean makeAMove(Board board, int[] move, String marker){
		try{
			board.setSquare(move[0], move[1], marker);
		}catch (InvalidSquareMarkerException e){
			return false;
		}catch (ResettingSquareException e){
			return false;
		}catch (SquareOutOfBoundsException e){
			return false;
		}
		
		return true;
	}
	
	
	public static String stringifyBoard(Board board){
		String boardString = "";
		for(int i = 0; i<3; i++){
			for(int j=0; j<3; j++){
				try{
					boardString = boardString + "[ " + board.getSquare(i, j) + " ]";
				}catch(SquareOutOfBoundsException e){
					System.out.println("THIS SHOULD NOT HAPPEN");
				}
			}
			boardString = boardString + "\n";
		}
		
		return boardString;
	}
	
	public static void playSinglePlayer(Board board) throws IOException{
		Map <Integer,Set<Integer>> laneMap = new HashMap <Integer,Set<Integer>>();
		Map <Integer,int[]> squareMap = new HashMap <Integer, int[]>();
		setupLaneMap(laneMap);
		setupSquareMap(squareMap);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Oh, last little bit of business, but who is going first?\n"
				+ "If you would like to go first,\n"
				+ "please type: player1\n"
				+ "If you'd like the computer to go first,\n"
				+ "please type: computerplayer1\n");
		
		String firstPlayer="",secondPlayer="";
		firstPlayer = br.readLine();
		while(!firstPlayer.equals("player1") && !firstPlayer.equals("computerplayer1")){
			System.out.println("I'm sorry, I don't understand that word.\n"
					+ "to choose first player,\n"
					+ "pleast type player1 or computerplayer1\n");
			firstPlayer=br.readLine();
		}
		
		Player computerplayer1 = new BasicComputerPlayer();
		if(firstPlayer.equals("player1")){
			secondPlayer = "computerplayer1";
			try{
				computerplayer1 = new BasicComputerPlayer("standard","computerplayer2");
			}catch(Exception e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}
		}else{
			secondPlayer = "player1";
			try{
				computerplayer1 = new BasicComputerPlayer("standard","computerplayer1");
			}catch(Exception e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}
		}
		
		System.out.print("Alright! Lets play!\n");
		String move = "";
		String gameStatus = "";
		while(true){
			if(firstPlayer.equals("player1")){
				System.out.println(stringifyBoard(board));
				System.out.print("Please choose a square " + firstPlayer + ".\n");
				move = br.readLine();
				while(!makeAMove(move,board, squareMap, "X")){
					System.out.println("I'm sorry, that was not a valid move. Please try again.");
					move = br.readLine();
				}
				System.out.println(stringifyBoard(board));
			}else{
				try{
					Thread.sleep(1500);
				}catch(InterruptedException e){
					System.out.println("Program was interrupted while sleeping between computer turns.\nThis should not happen");
					throw new RuntimeException();
				}
				try{
					int[] square = computerplayer1.makeAMove(board);
					board.setSquare(square[0], square[1], "X");
				}catch (Exception e){
					throw new RuntimeException();
				}
				System.out.println(firstPlayer + " made a move.");
			}
			gameStatus = checkGameOver(board,laneMap,squareMap);
			if(!gameStatus.equals("not over"))
				break;
			
			if(secondPlayer.equals("player1")){
				System.out.println(stringifyBoard(board));
				System.out.print("Please choose a square " + secondPlayer + ".\n");
				move = br.readLine();
				while(!makeAMove(move,board, squareMap, "O")){
					System.out.println("I'm sorry, that was not a valid move. Please try again.");
					move = br.readLine();
				}
				System.out.println(stringifyBoard(board));
			}else{
				try{
					Thread.sleep(1500);
				}catch(InterruptedException e){
					System.out.println("Program was interrupted while sleeping between computer turns.\nThis should not happen");
					throw new RuntimeException();
				}
				try{
					int[] square = computerplayer1.makeAMove(board);
					board.setSquare(square[0], square[1], "O");
				}catch (Exception e){
					throw new RuntimeException();
				}
				System.out.println(secondPlayer + " made a move.");
			}
			gameStatus = checkGameOver(board,laneMap,squareMap);
			if(!gameStatus.equals("not over"))
				break;
			
		}
		
		System.out.println(stringifyBoard(board));
		
		if(gameStatus.equals("tie"))
			System.out.println("Too bad! Nobody won. It was a tie.");
		else if(gameStatus.equals("X"))
			System.out.println(firstPlayer + " won!");
		else
			System.out.println(secondPlayer + " won!");
	}
	
	public static void playAgainstFriend(Board board) throws IOException {
		Map <Integer,Set<Integer>> laneMap = new HashMap <Integer,Set<Integer>>();
		Map <Integer,int[]> squareMap = new HashMap <Integer, int[]>();
		setupLaneMap(laneMap);
		setupSquareMap(squareMap);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Oh, last little bit of business, but who is going first?\n"
				+ "If you would like to go first,\n"
				+ "please type: player1\n"
				+ "If you'd like your friend to go first,\n"
				+ "please type: player2\n");
		String firstPlayer="",secondPlayer="";
		firstPlayer = br.readLine();
		while(!firstPlayer.equals("player1") && !firstPlayer.equals("player2")){
			System.out.println("I'm sorry, I don't understand that word.\n"
					+ "to choose first player,\n"
					+ "pleast type player1 or player2\n");
			firstPlayer=br.readLine();
		}
		if(firstPlayer.equals("player1"))
			secondPlayer = "player2";
		else
			secondPlayer = "player1";
		
		System.out.print("Alright! Lets play!\n");
		
		String move = "";
		String gameStatus = "";
		while(true){
			System.out.println(stringifyBoard(board));
			System.out.print("Please choose a square " + firstPlayer + ".\n");
			move = br.readLine();
			while(!makeAMove(move,board, squareMap, "X")){
				System.out.println("I'm sorry, that was not a valid move. Please try again.");
				move = br.readLine();
			}
			gameStatus = checkGameOver(board,laneMap,squareMap);
			if(!gameStatus.equals("not over"))
				break;
			
			System.out.println(stringifyBoard(board));
			System.out.print("Please choose a square " + secondPlayer + ".\n");
			move = br.readLine();
			while(!makeAMove(move,board, squareMap, "O")){
				System.out.println("I'm sorry, that was not a valid move. Please try again.");
				move = br.readLine();
			}
			gameStatus = checkGameOver(board,laneMap,squareMap);
			if(!gameStatus.equals("not over"))
				break;
			
		}
		
		System.out.println(stringifyBoard(board));
		
		if(gameStatus.equals("tie"))
			System.out.println("Too bad! Nobody won. It was a tie.");
		else if(gameStatus.equals("X"))
			System.out.println(firstPlayer + " won!");
		else
			System.out.println(secondPlayer + " won!");
		
	}
	
	public static void computerDuel(Board board) throws IOException{
		Map <Integer,Set<Integer>> laneMap = new HashMap <Integer,Set<Integer>>();
		Map <Integer,int[]> squareMap = new HashMap <Integer, int[]>();
		setupLaneMap(laneMap);
		setupSquareMap(squareMap);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Oh, last little bit of business, but who is going first?\n"
				+ "If you would like computerplayer1 to go first,\n"
				+ "please type: computerplayer1\n"
				+ "If you'd like computerplayer2 to go first,\n"
				+ "please type: computerplayer2\n");
		
		String firstPlayer="",secondPlayer="";
		firstPlayer = br.readLine();
		while(!firstPlayer.equals("computerplayer1") && !firstPlayer.equals("computerplayer2")){
			System.out.println("I'm sorry, I don't understand that word.\n"
					+ "to choose first player,\n"
					+ "pleast type computerplayer1 or computerplayer2\n");
			firstPlayer=br.readLine();
		}
		
		Player computerplayer1 = new BasicComputerPlayer();
		Player computerplayer2 = new BasicComputerPlayer();
		if(firstPlayer.equals("computerplayer1")){
			secondPlayer = "computerplayer2";
			try{
				computerplayer1 = new BasicComputerPlayer("standard","computerplayer1");
				computerplayer2 = new BasicComputerPlayer("standard","computerplayer2");
			}catch(Exception e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}
		}else{
			secondPlayer = "computerplayer1";
			try{
				computerplayer1 = new BasicComputerPlayer("standard","computerplayer2");
				computerplayer2 = new BasicComputerPlayer("standard","computerplayer1");
			}catch(Exception e){
				System.out.println("THIS SHOULD NOT HAPPEN");
			}
		}
		
		System.out.print("Alright! Lets play!\n");
		String move = "";
		String gameStatus = "";
		while(true){
			try{
				int[] square = new int[2];
				if(firstPlayer.equals("computerplayer1"))
					square = computerplayer1.makeAMove(board);
				else
					square = computerplayer1.makeAMove(board);
				board.setSquare(square[0], square[1], "X");
			}catch (Exception e){
				throw new RuntimeException();
			}
			System.out.println(firstPlayer + " made a move.");
			System.out.println(stringifyBoard(board));
				
			gameStatus = checkGameOver(board,laneMap,squareMap);
			if(!gameStatus.equals("not over"))
				break;
			
			try{
				Thread.sleep(1500);
			}catch(InterruptedException e){
				System.out.println("Program was interrupted while sleeping between computer turns.\nThis should not happen");
				throw new RuntimeException();
			}
			
			try{
				int[] square = new int[2];
				if(secondPlayer.equals("computerplayer1"))
					square = computerplayer1.makeAMove(board);
				else
					square = computerplayer2.makeAMove(board);
				board.setSquare(square[0], square[1], "O");
			}catch (Exception e){
				throw new RuntimeException();
			}
			System.out.println(secondPlayer + " made a move.");
			System.out.println(stringifyBoard(board));
				
			gameStatus = checkGameOver(board,laneMap,squareMap);
			if(!gameStatus.equals("not over"))
				break;
			
			try{
				Thread.sleep(1500);
			}catch(InterruptedException e){
				System.out.println("Program was interrupted while sleeping between computer turns.\nThis should not happen");
				throw new RuntimeException();
			}
			
		}
		
		System.out.println(stringifyBoard(board));
		
		if(gameStatus.equals("tie"))
			System.out.println("Too bad! Nobody won. It was a tie.");
		else if(gameStatus.equals("X"))
			System.out.println(firstPlayer + " won!");
		else
			System.out.println(secondPlayer + " won!");
	}
	
	public static void setupLaneMap(Map<Integer,Set<Integer>> laneMap){
		setupLaneMapHelper(laneMap, 1, 1,2,3);
		setupLaneMapHelper(laneMap, 2, 4,5,6);
		setupLaneMapHelper(laneMap, 3, 7,8,9);
		setupLaneMapHelper(laneMap, 4, 1,5,9);
		setupLaneMapHelper(laneMap, 5, 1,4,7);
		setupLaneMapHelper(laneMap, 6, 2,5,8);
		setupLaneMapHelper(laneMap, 7, 3,6,9);
		setupLaneMapHelper(laneMap, 8, 3,5,7);
	}
	public static void setupLaneMapHelper(Map<Integer,Set<Integer>> laneMap, int lane, int squareA, int squareB, int squareC ){
		Set<Integer> value = new HashSet<Integer>();
		value.add(squareA);
		value.add(squareB);
		value.add(squareC);
		laneMap.put(lane, value);
	}
	
	public static void setupSquareMap(Map <Integer,int[]> squareMap){
		setupSquareMapHelper(squareMap, 1, 0,0);
		setupSquareMapHelper(squareMap, 2, 0,1);
		setupSquareMapHelper(squareMap, 3, 0,2);
		setupSquareMapHelper(squareMap, 4, 1,0);
		setupSquareMapHelper(squareMap, 5, 1,1);
		setupSquareMapHelper(squareMap, 6, 1,2);
		setupSquareMapHelper(squareMap, 7, 2,0);
		setupSquareMapHelper(squareMap, 8, 2,1);
		setupSquareMapHelper(squareMap, 9, 2,2);
	}
	
	public static void setupSquareMapHelper(Map <Integer,int[]> squareMap, int square, int x, int y){
		int[] value = new int[2];
		value[0] = x;
		value[1] = y;
		squareMap.put(square, value);
	}
}
