package student_player;

import boardgame.Move;
import tablut.TablutBoardState;

public class MyTools implements MinimaxTools
{
	public static final int SWEDE     = 1;
    public static final int MUSCOVITE = 0;
    public static final int INF		  = Integer.MAX_VALUE;
    public static final int NEG_INF	  = Integer.MIN_VALUE;
    
    public static double getSomething() 
    {
        return Math.random();
    }

	@Override
	public Move minimaxDecision(TablutBoardState state) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int maxValue(TablutBoardState state) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int minValue(TablutBoardState state) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isTerminal(TablutBoardState state) 
	{
		return state.gameOver();
	}

	@Override
	public int utility(TablutBoardState state) 
	{
		// For now, assume we are Muscovites
		if (state.getWinner() == MUSCOVITE)
			return INF;
		else if (state.getWinner() == SWEDE)
			return NEG_INF;
		else
			return 0;
	}
}