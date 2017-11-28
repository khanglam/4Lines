import java.util.ArrayList;
import java.util.List;


public class Successors {
	private char[] state;
	public List<char[]> successors = new ArrayList<char[]>();
	
	
	public Successors(char[] state)
	{
		this.state = state;
	}
	
	public void generateSuccessors(int turn)
	{
		Successors child;
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
	public List<char[]> getSuccessor()
	{
		return successors;
	}
}
