package student_player;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;

public class MyTools 
{
	private static final int SWEDE     			= 1;
	private static final int MUSCOVITE 			= 0;
	private static final int INF	   			= Integer.MAX_VALUE;
	private static final int NEG_INF 			= Integer.MIN_VALUE;
	private static 		 int MAX_PLAYER;
	private static 	  	 Random rand 			= new Random(1919);
	
	public MyTools(int maxPlayer)
	{
		MAX_PLAYER = maxPlayer;
	}
    
    /**
	 * 
	 * @param state
	 * @return the action corresponding to the best possible move
	 * Theoretically this should return the argmax of minValue()
	 */
	public static TablutMove minimaxDecision(TablutBoardState state) 
	{
		TablutMove bestMove;
		int minValue, maximumMinValue, depth;
		List<TablutMove> options;
		
		maximumMinValue = NEG_INF;
		options = state.getAllLegalMoves();
		// Set a random move as the initial best move
		bestMove = options.get(rand.nextInt(options.size()));
		depth = 2;
		for (TablutMove move : options){
			minValue = minValue(result(state, move), depth);
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
	private static int maxValue(TablutBoardState state, int depth) 
	{
		int v, bestValue;

		if (terminal(state) || depth == 0){
			return utility(state);
		}
		bestValue = NEG_INF;
		for (TablutMove move : state.getAllLegalMoves()){
			v = minValue(result(state, move), depth-1);
			bestValue = Math.max(bestValue, v);
		}
		return bestValue;
	}

	/**
	 * Goes through the whole game tree, all the way to the leaves,
	 * to determine the backed-up value of a state
	 * @param state
	 * @return
	 */
	private static int minValue(TablutBoardState state, int depth) 
	{
		int v, bestValue;
		
		if (terminal(state) || depth == 0){
			return utility(state);
		}
		bestValue = INF;
		for (TablutMove move : state.getAllLegalMoves()){
			v = maxValue(result(state, move), depth-1);
			bestValue = Math.min(bestValue, v);
		}
		return bestValue;
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
		// TODO: improve the utiliy function
		Coord kingPos;
		HashSet<Coord> muscovCoords;
		int totalDistanceToKing, distToClosestCorner;
		
		// utility function for Muscovites
		if (MAX_PLAYER == MUSCOVITE){
			if (state.getWinner() == MUSCOVITE)
				return INF;
			else if (state.getWinner() == SWEDE)
				return NEG_INF;
			else{
				kingPos = state.getKingPosition();
				muscovCoords = state.getPlayerPieceCoordinates();
				totalDistanceToKing = 0;
				for (Coord muscovCoord : muscovCoords)
					totalDistanceToKing += kingPos.distance(muscovCoord);
				return -totalDistanceToKing;
			}
		// FIXME utility function for Swedes
		}else{ // if MAX_PLAYER is SWEDE
			if (state.getWinner() == SWEDE)
				return INF;
			else if (state.getWinner() == MUSCOVITE)
				return NEG_INF;
			else{
				/*
				 *  Swede King is trying to escape through the corners,
				 *  so their utility increases the closer the king gets to corner 
				 */
				kingPos = state.getKingPosition();
				distToClosestCorner = Coordinates.distanceToClosestCorner(kingPos);
				return -distToClosestCorner;
			}
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
		clone.processMove(move);
		return clone;
	}
}