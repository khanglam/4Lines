import java.util.ArrayList;
import java.util.Scanner;


public class FourLines {

	public static void main(String[] args) {
//		char[] board = {' ',' ',' ',' ',' ',' ',' ',' ',
//						' ',' ','O',' ',' ',' ',' ',' ',
//						' ',' ','O',' ',' ',' ',' ',' ',
//						' ','X','O','O','O','X',' ',' ',
//						' ',' ','O','X','X','X','O',' ',
//						' ',' ','X','X',' ',' ',' ',' ',
//						' ',' ',' ','X',' ',' ',' ',' ',
//						' ',' ',' ','O',' ',' ',' ',' '
//				};
		char[] board = {'O','O','O','X','O','O','X','O',
						'O','X','O','X','O','O','X','O',
						'O','X','X','O','X','X','O','X',
						'X','O','O','X','O','O','X','X',
						'O','O','O','X','O','O','O','X',
						'O','X','O','X','X','X','O','O',
						'X','O','X','X','O','O','X','O',
						'X','O','O','X','O','O','X','O'
						};
		Opponent AI = new Opponent(board, 0, 0);
		AI.utility(board);
		System.out.print(AI.TerminalTest(board));
//		promptUser();
	}
	
	public static void promptUser() {
		int secInput = 0;
		int turnInput = 0;
		Scanner kb = new Scanner(System.in);
		
		while(true) {
			System.out.println("How long should the Opponent calculate its moves?");
			System.out.print("Enter (min 5 sec, max 30 sec): ");
			secInput = kb.nextInt();
			if (secInput < 5 || secInput > 30) {
				System.out.println("Invalid time input, please try again.\n");
			} else 
				break;
		}	
		
		while(true) {
			System.out.println("Now, decide who goes first, you or opponent.");
			System.out.println("Input options: 1 = You go first.");
			System.out.println("               2 = Opponent goes first.");
			System.out.print("Enter 1 or 2: ");
			turnInput = kb.nextInt();
			if (turnInput != 1 && turnInput != 2) {
				System.out.println("Invalid input, please try again.\n");
			} else 
				break;
		}
		
		loadGame(secInput, turnInput);
		kb.close();
		
		
		
	}

	/**
	 * This will load the game's initial state, define the pieces for player
	 * and opponent, and execute the playGame function.
	 * @param secInput @param turnInput
	 */
	public static void loadGame(int secInput, int turnInput) {
		
		final int BOARD_SIZE = 64;
		char[] board = new char[BOARD_SIZE]; //board is initially empty
		char playerPiece;
		char opponentPiece;
		

		if (turnInput == 1) {
			//input = playerInput();
			//index = translateInputToIndex(input);
			playerPiece = 'X';
			opponentPiece = 'O';
			playGame(board, playerPiece, opponentPiece, secInput);
			//board[index] = playerPiece;
		} else {
			playerPiece = 'O';
			opponentPiece = 'X';
			playGame(board, playerPiece, opponentPiece, secInput);
		}
				
		
		
	}

	/**
	 * Function where the game states and printBoard functions are executed. 
	 * @param board @param playerPiece @param opponentPiece @param secInput
	 */
	public static void playGame(char[] board, char playerPiece, char opponentPiece, int secInput) {

		ArrayList<String> turnHistory = new ArrayList<String>();
		
		if (playerPiece == 'X') {
			while (true) {
				playerMovesNext(board, playerPiece, secInput, turnHistory);
				opponentMovesNext(board, opponentPiece, secInput, turnHistory); //work in progress. 
			}
		} else {
			while (true) {
				opponentMovesNext(board, opponentPiece, secInput, turnHistory); //work in progress. 
				playerMovesNext(board, playerPiece, secInput, turnHistory);
			}
		}
		
	}
	
	/**
	 * Player functions to place a piece on board. 
	 * @param board @param opponentPiece @param secInput @param turnHistory
	 */
	public static void playerMovesNext(char[] board, char playerPiece, int secInput, ArrayList<String> turnHistory) {
		String input;
		int index;
		input = playerInput(board, turnHistory, playerPiece);
		printBoard(board, turnHistory);
		System.out.println("\nPlayer's move is: " + input);
		checkTerminalState(board); //work in progress. 
		
	}

	private static void checkTerminalState(char[] board) {
		
		
	}

	/**
	 * Opponent moves next, using the alpha-beta prunning algorithm to determine its placement on the board.
	 * WORK IN PROGRESS. 
	 * @param board @param opponentPiece @param secInput
	 * @param turnHistory 
	 */
	public static void opponentMovesNext(char[] board, char opponentPiece, int secInput, 
										 ArrayList<String> turnHistory) {
		board[0] = opponentPiece; //testing
		turnHistory.add("a1");    // testing
		printBoard(board, turnHistory);
		System.out.println("\nOpponent's move is: " + turnHistory.get(turnHistory.size() - 1));
		checkTerminalState(board); //work in progress.
		
	}

	/**
	 * Translate the string input into a number so the board has an int index 
	 * to place the piece.  
	 * @param input @return
	 */
	public static int translateInputToIndex(String input) {
		int indexNumber = getLetterValue(input.charAt(0));
		
		int secondDigit = Character.getNumericValue(input.charAt(1)) - 1;
		indexNumber = indexNumber | secondDigit;
		return indexNumber;
	}

	/**
	 * Used to convert string input to int value index (for first char in input, which is a letter).
	 * translateInputToIndex() uses this function. 
	 * @param ch @return
	 */
	public static int getLetterValue(char ch) {
		if (ch == 'A' || ch == 'a') { 
			return 0;
		} else if (ch == 'B' || ch == 'b') {
			return 8;
		} else if (ch == 'C' || ch == 'c') {
			return 16;
		} else if (ch == 'D' || ch == 'd') {
			return 24;
		} else if (ch == 'E' || ch == 'e') {
			return 32;
		} else if (ch == 'F' || ch == 'f') {
			return 40;
		} else if (ch == 'G' || ch == 'g') {
			return 48;
		} else {
			return 56;
		}
	}

	/**
	 * Asks the user what position to place in board. Will return a valid input.
	 * If invalid input is given, it'll infinitely loop until user inputs correctly.
	 * If input already contains a piece, it'll also infinitely loop. 
	 * If input is valid and doesn't already have a piece in the position, it'll place the piece.
	 * @param board @param turnHistory @param playerPiece @return
	 */
	public static String playerInput(char[] board, ArrayList<String> turnHistory, char playerPiece) {
		
		Scanner kb = new Scanner(System.in);
		String input;
		
		while (true) {
			System.out.print("\nPlease enter a position to place the piece: ");
			input = kb.nextLine();
			if (validateInput(input) && validateBoardPiece(board, input, turnHistory, playerPiece)) {
				return input;
			}	
		}		
	}

	/**
	 * Validates if board piece already exists in position, given user input. 
	 * If not, places the piece in board and return true.
	 * @param board @param input @return @param turnHistory @param playerPiece 
	 */
	private static boolean validateBoardPiece(char[] board, String input, ArrayList<String> turnHistory, 
											  char playerPiece) {
		int index = translateInputToIndex(input);
		if (board[index] == 'X' || board[index] == 'O') {
			System.out.println("There's already a piece on the board at " + input + ".");
			return false;
		}	
		
		turnHistory.add(input);
		board[index] = playerPiece;
			return true;
	}

	/**
	 * Every time the player is asked for input, this function will validate the user's inputs 
	 * to see if it is valid. 
	 * @param input @return
	 */
	public static boolean validateInput(String input) {
		if (input.length() != 2 || !Character.isLetter(input.charAt(0))
			|| !Character.isDigit(input.charAt(1))) {
			System.out.println("This is not a valid input, please try again.");
		} else if ((input.charAt(0) < 65 || input.charAt(0) > 72) && 
			    (input.charAt(0)) < 97  || input.charAt(0) > 104) {
			System.out.println("This is out of bounds, please try again.");
		} else if (input.charAt(1) < 49 || input.charAt(1) > 56) {
			System.out.println("This is out of bounds, please try again.");
		} else 
			return true;
		return false;
	}

	/**
	 * Prints the current board state, with turn history made by player and opponent. 
	 * @param board @param turnHistory
	 */
	public static void printBoard(char[] board, ArrayList<String> turnHistory) {
		System.out.println("");
		System.out.print("   1  2  3  4  5  6  7  8");
		System.out.printf("%30s", "Player vs  Opponent");
		int turnHistoryIndex = 0;

		for(int i = 0; i < board.length; i++)
		{
			if (i % 8 == 0 ) {
				if (i !=0) 
					turnHistoryIndex = printOutTurnHistory(turnHistory, turnHistoryIndex);
				System.out.println();
			}
			if(i == 0) 
				System.out.print("A ");
			else if(i == 8)
				System.out.print("B ");
			else if(i == 16)
				System.out.print("C ");
			else if(i == 24)
				System.out.print("D ");
			else if(i == 32)
				System.out.print("E ");
			else if(i == 40)
				System.out.print("F ");
			else if(i == 48)
				System.out.print("G ");
			else if(i == 56)
				System.out.print("H ");


			if(board[i] == 'X') 
				System.out.print(" X ");
			else if (board[i] == 'O') 
				System.out.print(" O ");
			else 
				System.out.print(" - ");
		
		}
		// if there is more turn history to print out, this block of code will run. 
		while (true) {
			if (turnHistoryIndex < turnHistory.size()) 
				turnHistoryIndex = printOutTurnHistory(turnHistory, turnHistoryIndex);
			else 
				break;
		}			
		System.out.println();
	}

	/**
	 * Used by the printBoard function to print out the turn history.  
	 * @param turnHistory @param turnHistoryIndex @return
	 */
	public static int printOutTurnHistory(ArrayList<String> turnHistory, int turnHistoryIndex) {
		if (turnHistoryIndex < turnHistory.size()) {
			System.out.printf("%20s", turnHistoryIndex/2 + 1 + "). " +turnHistory.get(turnHistoryIndex));
			turnHistoryIndex++;
		}
		
		if (turnHistoryIndex < turnHistory.size()) {
			System.out.print("  " + turnHistory.get(turnHistoryIndex));
			turnHistoryIndex++;
		}
		
		return turnHistoryIndex;
	}


}
