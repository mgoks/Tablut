package student_player;

import boardgame.Board;
import tablut.TablutBoardState;
import tablut.TablutMove;

public class MyTools2 
{
	private static final int INF	   			= Integer.MAX_VALUE;
	private static final int NEG_INF 			= Integer.MIN_VALUE;
	
	private static boolean isTerminal(TablutBoardState node)
	{
		return node.gameOver();
	}
	
	private static int heuristicValue(TablutBoardState node)
	{
		if (node.getWinner() == node.getOpponent())
			return NEG_INF;
		else if (node.getWinner() == Board.NOBODY)
			return 0;
		else // if winner is me
			return INF;
	}
	
	private static TablutBoardState getChild(TablutBoardState node, TablutMove move)
	{
		TablutBoardState clone;
		
		clone = (TablutBoardState) node.clone();
		clone.processMove(move);
		return clone;
	}
	
//	public static TablutMove minimaxDecision(TablutBoardState state)
//	{
//		TablutMove bestMove;
//		int minValue, maximumMinValue, depth;
//		
//		depth = 5;
//		maximumMinValue = NEG_INF;
//		bestMove = null;
//		for (TablutMove move : state.getAllLegalMoves()){
//			
//		}
//	}
	
	public static int minimax(TablutBoardState node, int depth, boolean maximizingPlayer)
	{
		int bestValue, v;
		TablutBoardState child;
		
		if (depth == 0 || isTerminal(node))
			return heuristicValue(node);
		if (maximizingPlayer){
			bestValue = NEG_INF;
			for (TablutMove move : node.getAllLegalMoves()){
				child = getChild(node, move);
				v = minimax(child, depth-1, false);
				bestValue = Math.max(bestValue, v);
			}
			return bestValue;
		}else{ // minimizing player
			bestValue = INF;
			for (TablutMove move : node.getAllLegalMoves()){
				child = getChild(node, move);
				v = minimax(child, depth-1, true);
				bestValue = Math.min(bestValue, v);
			}
			return bestValue;
		}
	}
}