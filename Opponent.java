import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Opponent {
	private char[] state;
	private int turn;
	private char piece;
	private char opposingPiece;
	private final int INFINITY = Integer.MAX_VALUE;
	private int timeLimit;
//	private List<Successors> successors = new ArrayList<Successors>();
	private Queue<Successors> successors = new LinkedList<Successors>();
	private boolean isOver = false;
	private Successors child;
	private char[] chosenPath;
	
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

	
	public char[] AlphaBetaSearch(char[] state)
	{
		int v = MaxMove(state, -INFINITY, INFINITY);
		return chosenPath;
	}
	
	public int MaxMove(char[] state, int a, int B)
	{
		child = new Successors(state);
		if(TerminalTest(state)) return utility(state);
		int v = -INFINITY;
		child.generateSuccessors(turn);
//		successors.add(child);
		for(int i = 0; i < child.successors.size(); i++)
		{
			v = Math.max(v, MinMove(child.successors.get(i), a, B));
			if(v >= B) return v; //for pruning
//			a = Math.max(a, v);
			if(v >= a)
			{
				a = v;
				chosenPath = child.successors.get(i); //To retrace chosen path
			}
		}
		return v;
	}
	
	private int MinMove(char[] state, int a, int B) 
	{
		child = new Successors(state);
		if(TerminalTest(state)) return utility(state);
		int v = INFINITY;
		child.generateSuccessors(turn);
		for(int i = 0; i < child.successors.size(); i++)
		{
			v = Math.min(v, MaxMove(child.successors.get(i), a, B));
			if(v <= a) return v; //for pruning
			B = Math.min(B, v);
		}
		return v;
	}

	public boolean TerminalTest(char[] state)
	{
		boolean board_filled = true;
		for(int i = 0; i < state.length; i++)
		{
			if(state[i] == ' ') 
			{
				board_filled = false;
				continue;
			}
			if(( (i+1) % 8 != 0 ) && ((i+2) % 8 != 0) && 
					((i+3) % 8 != 0))  //check borders
			{
				if((state[i] == piece) && (state[i+1] == piece) && 
						(state[i+2] == piece) && (state[i+3] == piece))
					return true;
				if((state[i] == opposingPiece) && (state[i+1] == opposingPiece) && 
						(state[i+2] == opposingPiece) && (state[i+3] == opposingPiece))
					return true;
			}
			if(( (i< 40) && (i+8) % 8 != 0 ) && ((i+16) % 8 != 0) && 
					((i+24) % 8 != 0))  //i < 40 because out of bounds
			{			//Check bottom borders
				if((state[i] == piece) && (state[i+8] == piece) && 
						(state[i+16] == piece) && (state[i+24] == piece))
					return true;
				if((state[i] == opposingPiece) && (state[i+8] == opposingPiece) && 
						(state[i+16] == opposingPiece) && (state[i+24] == opposingPiece))
					return true;
			}
		}
		return board_filled;
	}
	
	
	public int utility(char[] state) {
		int score = 0;
		int opposingScore = 0;
		boolean spaceBetween = false;
		for(int i = 0; i < state.length; i++)
		{
			score = verticalCheck(i, score, piece, opposingPiece, spaceBetween);//,

			if (score == INFINITY) return score;
			
			score = horizontalCheck(i, score, piece, opposingPiece, spaceBetween);
			
			if (score == INFINITY) return score;
			
			opposingScore = verticalCheck(i, opposingScore, opposingPiece, piece, spaceBetween);//,
			
			if (opposingScore == INFINITY) return -opposingScore;
			
			opposingScore = horizontalCheck(i, opposingScore, opposingPiece, piece, spaceBetween);
			
			if (opposingScore == INFINITY) return -opposingScore;
			
		}
		return score - opposingScore;
	}
	
	public int verticalCheck(int i, int score, char piece, char opposingPiece, boolean spaceBetween)
	{

		if (state[i] == piece) {
			int addScore = 0;
			if (i + 24 < state.length) {//upper rows
				if (state[i + 8] != opposingPiece  && 
					state[i + 16] != opposingPiece && 
					state[i + 24] != opposingPiece) {
					
					if ((state[i + 8] == piece) && 
						(state[i +16] == piece) && 
						(state[i+ 24] == piece))
						return INFINITY;
					
					if (state[i + 8] == piece) addScore++;
					else spaceBetween = true;
 
					if (state[i + 16] == piece && !spaceBetween) addScore++;
					else spaceBetween = true;
					
					if (state[i + 24] == piece && !spaceBetween) addScore++;
					
					addScore++;
				}
			}
			
			spaceBetween = false;
			if (i - 24 >= 0) {
				if (state[i - 8]  != opposingPiece && 
					state[i - 16] != opposingPiece && 
					state[i - 24] != opposingPiece) {
					
					if ((state[i - 8] == piece) && 
							(state[i -16] == piece) && 
							(state[i -24] == piece))
							return INFINITY;
					
					if (state[i - 8] == piece) addScore++;
					else spaceBetween = true;
 
					if (state[i - 16] == piece && !spaceBetween)  addScore++;
					else spaceBetween = true;
					
					if (state[i - 24] == piece && !spaceBetween) addScore++;
					
					addScore++;
				}
			}
			
			score += addScore;
			
		}
		
		return score;
	}
	
	public int horizontalCheck(int i, int score, char piece, char opposingPiece, boolean spaceBetween) {
		char hole = ' ';
		
		if(state[i] == piece) {
			int addScore = 0;
			//Check right horizontal
			if(( (i+1) % 8 != 0 ) && ((i+2) % 8 != 0) && ((i+3) % 8 != 0))  {
				if(state[i+1] != opposingPiece && 
				   state[i+2] != opposingPiece && 
				   state[i+3] != opposingPiece) {
					
					if ((state[i + 1] == piece) && 
						(state[i + 2] == piece) && 
						(state[i + 3] == piece))
							return INFINITY;
					
					if (state[i + 1] == piece) addScore++;
					else spaceBetween = true;
 
					if (state[i + 2] == piece && !spaceBetween) addScore++;
					else spaceBetween = true;
					
					if (state[i + 3] == piece && !spaceBetween) addScore++;
					
					addScore++;
				
				}
			}	
			spaceBetween = false;
			//Check left horizontal
			if((i % 8) >= 3) {
				if(state[i-1] != opposingPiece && 
				   state[i-2] != opposingPiece && 
				   state[i-3] != opposingPiece) {
					
					if ((state[i - 1] == piece) && 
						(state[i - 2] == piece) && 
						(state[i - 3] == piece))
							return INFINITY;
					
					if (state[i - 1] == piece) addScore++;
					else spaceBetween = true;
 
					if (state[i - 2] == piece && !spaceBetween) addScore++;
					else spaceBetween = true;
					
					if (state[i - 3] == piece && !spaceBetween) addScore++;
					
					addScore++;
					
					
				}
					//if((state[i-1] == hole || state[i-1] == piece) &&
					//	(state[i-2] == hole || state[i-2] == piece) &&
					//	(state[i-3] == hole || state[i-3] == piece))
					//score += 1;
			}
			score += addScore;
		}
		
		return score;
	}
}

