import java.util.ArrayList;
import java.util.List;


public class Opponent {
	private char[] state;
	private int turn;
	private char piece;
	private char opposingPiece;
	private int timeLimit;
	private List<char[]> successors = new ArrayList<char[]>();
	private boolean isOver = false;
	
	public Opponent(char[] state, int turn, int timeLimit)
	{
		this.state = state;
		this.turn = turn; //0 for O, 1 for X
		this.timeLimit = timeLimit;
		if(turn == 0) {
			piece = 'O';
			opposingPiece = 'X';
		}else { 
			piece = 'X';
			opposingPiece = 'O';
		}
	}
	
	public void generateSuccessors()
	{
		if(turn == 0) //if opponent is O
		{
			for(int i = 0; i < state.length; i++)
			{
				if(state[i] != 'X')
				{
					state[i] = 'O';
					successors.add(state);
					state[i] = ' ';
				}
			}
		}
		else //if Opponent is X
		{
			for(int i = 0; i < state.length; i++)
			{
				if(state[i] != 'O')
				{
					state[i] = 'X';
					successors.add(state);
					state[i] = ' ';
				}
			}
		}
	}
	
	public char[] AlphaBetaSearch(char[] state)
	{
		int v = MaxMove(state, -Integer.MAX_VALUE, Integer.MAX_VALUE);
		return state; //this is wrong but idk how to do this yet
	}
	
	public int MaxMove(char[] state, int a, int B)
	{
		if(TerminalTest(state)) return evaluate(state);
		int v = -Integer.MAX_VALUE;
		for(int i = 0; i < successors.size(); i++)
		{
			v = Math.max(v, MinMove(successors.get(i), a, B));
			if(v >= B) return v;
			a = Math.max(a, v);
		}
		return v;
	}
	
	private int MinMove(char[] state, int a, int B) 
	{
		if(TerminalTest(state)) return evaluate(state);
		int v = Integer.MAX_VALUE;
		for(int i = 0; i < successors.size(); i++)
		{
			v = Math.min(v, MaxMove(successors.get(i), a, B));
			if(v <= a) return v;
			B = Math.min(B, v);
		}
		return v;
	}

	public boolean TerminalTest(char[] state)
	{
		if(turn == 0) {
			piece = 'O';
			opposingPiece = 'X';
		}else { 
			piece = 'X';
			opposingPiece = 'O';
		}
		for(int i = 0; i < state.length; i++)
		{
			
		}
		return false;
	}
	
	public int evaluate(char[] state)
	{
		int score = 0;
		if(turn == 0) //if AI is O
		{
			for(int i = 0; i < state.length; i++)
			{
				if(state[i] == 'O')
				{
					score += 1;
				}
				else if(state[i] == 'X')
					score -= 1;
			}
		}
		return 0;
	}
	
	public int heuristicCheck(char[] state)
	{
		int score = 0;
		int opposingScore = 0;
		char piece;
		char opposingPiece;
		if(turn == 0) {
			piece = 'O';
			opposingPiece = 'X';
		}else { 
			piece = 'X';
			opposingPiece = 'O';
		}
		
		final int SPACE_ONE = 8;
		final int SPACE_TWO = 16;
		final int SPACE_THREE = 24;
		boolean verticalUp = true, verticalDown = true;
		
		for(int i = 0; i < state.length; i++)
		{

			score = verticalCheck(i, score, opposingScore, piece, opposingPiece,
								  SPACE_ONE, SPACE_TWO, SPACE_THREE,
								  verticalUp, verticalDown);
			score = horizontalCheck(i, score, opposingScore, piece, opposingPiece);
		}
		return score;
	}
	
	public int verticalCheck(int i, int score, int opposingScore, char piece, char opposingPiece, 
							 int SPACE_ONE, int SPACE_TWO, int SPACE_THREE, 
							 boolean verticalUp, boolean verticalDown)
	{
		if (state[i] == piece) {
			verticalDown = true;
			verticalUp = true;
			if (i + SPACE_THREE < state.length) {//upper bound 
				if (state[i + SPACE_ONE] == opposingPiece) {
					verticalDown = false;
					} else if (state[i + SPACE_TWO] == opposingPiece) {
						verticalDown = false;
					} else if (state[i + SPACE_THREE] == opposingPiece) {
						verticalDown = false;
					}
			} else {
				verticalDown = false;
			}
			
			if (i - SPACE_THREE >= 0) {
				if (state[i - SPACE_ONE] == opposingPiece) {
					verticalUp = false;
					} else if (state[i - SPACE_TWO] == opposingPiece) {
						verticalUp = false;
					} else if (state[i - SPACE_THREE] == opposingPiece) {
						verticalUp = false;
					}
			} else {
				verticalUp = false;
			}
			
			if (verticalDown)
				score++;
			
			if (verticalUp)
				score++;
		}
		return score;
	}
	
	public int horizontalCheck(int i, int score, int opposingScore, char piece, char opposingPiece)
	{
		char hole = ' ';
		
		if(state[i] == piece)
		{
			
			//Check right horizontal
			if(( (i+1) % 8 != 0 ) && ((i+2) % 8 != 0) && 
					((i+3) % 8 != 0))  //move on to next row
			{
				if((state[i+1] == hole || state[i+1] == piece) &&
						(state[i+2] == hole || state[i+2] == piece) &&
						(state[i+3] == hole || state[i+3] == piece))
					score += 1;
				else if((state[i+1] == hole || state[i+1] == opposingPiece) &&
						(state[i+2] == hole || state[i+2] == opposingPiece) &&
						(state[i+3] == hole || state[i+3] == opposingPiece))
					opposingScore += 1;
			}
			if((i % 8) >= 3)//Check left horizontal
				{
					if((state[i-1] == hole || state[i-1] == piece) &&
						(state[i-2] == hole || state[i-2] == piece) &&
						(state[i-3] == hole || state[i-3] == piece))
					score += 1;
				}
		}
		return score;
	}
}

