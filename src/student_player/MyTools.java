package student_player;

import java.util.HashSet;
import java.util.List;

import coordinates.Coord;
import tablut.TablutBoardState;
import tablut.TablutMove;

public class MyTools 
{
	private static final int SWEDE     			= 1;
	private static final int MUSCOVITE 			= 0;
	private static final int INF	   			= Integer.MAX_VALUE;
	private static final int NEG_INF 			= Integer.MIN_VALUE;
	private static final int MAX_SEARCH_DEPTH 	= 4500;
	private static int searchDepth;
    
    /**
	 * 
	 * @param state
	 * @return the action corresponding to the best possible move
	 * Theoretically this should return the argmax of minValue()
	 */
	public static TablutMove minimaxDecision(TablutBoardState state) 
	{
		TablutMove bestMove;
		int minValue, maximumMinValue;
		
		searchDepth = 0;
		maximumMinValue = NEG_INF;
		bestMove = null;
		for (TablutMove move: state.getAllLegalMoves()){
			minValue = minValue(result(state, move));
			if (minValue > maximumMinValue){
				bestMove = move;
				maximumMinValue = minValue;
			}
		}
		return bestMove;
	}

	/**
	 * Goes through the whole game tree, all the way to the leaves,
	 * to determine the backed-up value of a state
	 * @param state
	 * @return
	 */
	private static int maxValue(TablutBoardState state) 
	{
		int v;

		if (terminal(state) || searchDepth >= MAX_SEARCH_DEPTH)
			return utility(state);
		++searchDepth;
//		System.out.println("Search depth: " + searchDepth);
		v = NEG_INF;
		for (TablutMove move : state.getAllLegalMoves())
			v = Math.max(v, minValue(result(state, move)));
		return v;
	}

	/**
	 * Goes through the whole game tree, all the way to the leaves,
	 * to determine the backed-up value of a state
	 * @param state
	 * @return
	 */
	private static int minValue(TablutBoardState state) 
	{
		int v;
		
		if (terminal(state) || searchDepth >= MAX_SEARCH_DEPTH)
			return utility(state);
		++searchDepth;
//		System.out.println("Search depth: " + searchDepth);
		v = INF;
		for (TablutMove move : state.getAllLegalMoves())
			v = Math.min(v, maxValue(result(state, move)));
		return v;
	}

	/**
	 * Basically a wrapper for TBS.gameOver()
	 * Added it in case if I want to check for conditions other than 
	 * the game being over to determine if the state is terminal
	 * @param state
	 * @return
	 */
	private static boolean terminal(TablutBoardState state) 
	{
		return state.gameOver();
	}

	/**
	 * Utility/evaluation function
	 * @param state
	 * @return A utility value of the state
	 */
	private static int utility(TablutBoardState state) 
	{
		// TODO: improve this later
		// For now, assume we are always Muscovites
		// and assume our utility increases as we get closer to the King
		Coord kingPos;
		HashSet<Coord> muscovCoords;
		int totalDistanceToKing;
		
		if (state.getWinner() == MUSCOVITE)
			return INF;
		else if (state.getWinner() == SWEDE)
			return NEG_INF;
		else{
//			kingPos = state.getKingPosition();
//			muscovCoords = state.getPlayerPieceCoordinates();
//			totalDistanceToKing = 0;
//			for (Coord muscovCoord : muscovCoords)
//				totalDistanceToKing += kingPos.distance(muscovCoord);
//			return totalDistanceToKing;
			return 0;
		}
	}

	/**
	 * Wrapper for TBS.processMove()
	 * @param state
	 * @param move
	 * @return The state after processing the move
	 */
	private static TablutBoardState result(TablutBoardState state, TablutMove move) 
	{
		TablutBoardState clone;
		
		clone = (TablutBoardState) state.clone();
		return clone;
	}
}