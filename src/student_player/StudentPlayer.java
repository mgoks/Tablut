package student_player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import boardgame.Move;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class StudentPlayer extends TablutPlayer 
{
	private static final int SEARCH_DEPTH = 2;
	private static final double W1		  = 1.0;
	private static final Random rand = new Random(1919);
	
    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() 
    {
        super("260572202");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(TablutBoardState boardState) 
    {
    	List<TablutMove> possibleMoves;
    	double highestValue, value;
    	int depth;
    	TablutMove bestMove;
    	
    	bestMove = null;
    	highestValue = Double.NEGATIVE_INFINITY;
    	depth = SEARCH_DEPTH;
    	possibleMoves = boardState.getAllLegalMoves();
    	/*
    	 * For all possible moves, calculate their value.
    	 * If value of the move > highest value so far, make it the best move.
    	 * When finished, return the move with the highest value.
    	 * 
    	 * TODO optimize
    	 */
    	for (TablutMove move : possibleMoves){
    		value = alphaBeta(getChild(boardState, move), 
					  depth, 
					  Double.NEGATIVE_INFINITY, 
					  Double.POSITIVE_INFINITY, 
					  false);
    		
    		// If value of the move = max value, return that move
    		if (value == Double.POSITIVE_INFINITY)
    			return move;
    		
    		if (value > highestValue){
        		bestMove = move;
        		highestValue = value;
        	}
    	}
    	System.out.println(highestValue);
    	return bestMove;
    }
    
    private double alphaBeta(TablutBoardState node, int depth, double a, double b, boolean maximizingPlayer)
    {
    	double v;
    	List<TablutBoardState> childrenOfNode;
    	
    	if (depth == 0 || isTerminal(node))
    		return heuristicValue(node);
    	
    	// Get child nodes of node
    	childrenOfNode = getChildren(node, node.getAllLegalMoves());
    	
    	if (maximizingPlayer){
    		v = Double.NEGATIVE_INFINITY;
    		for (TablutBoardState child : childrenOfNode){
    			v = Math.max(v, alphaBeta(child, depth-1, a, b, false));
    			a = Math.max(a, v);
    			if (b <= a)
    				break; // beta cut-off
    		}
    		return v;
    	}else{
    		v = Double.POSITIVE_INFINITY;
    		for (TablutBoardState child : childrenOfNode){
    			v = Math.min(v,  alphaBeta(child, depth-1, a, b, true));
    			b = Math.min(b, v);
    			if (b <= a)
    				break; // alpha cut-off
    		}
    		return v;
    	}
    }
    
    private double heuristicValue(TablutBoardState node)
    {
    	/*
    	 * h(node) = w1*f1
    	 * 
    	 * f1 = (# my pieces) - (# opponent pieces)
    	 */
    	double f1;
    	int opponent;
    	
//    	if (node.getTurnNumber() > 44)
//    		System.out.println("winner = " + node.getWinner() + ", player id: " + player_id);
    	if(node.getWinner() == player_id)
    		return Double.POSITIVE_INFINITY;
    	
    	opponent = 1 - player_id;
    	f1 = node.getNumberPlayerPieces(player_id) - node.getNumberPlayerPieces(opponent);
    	return W1*f1;
    }
    
    private boolean isTerminal(TablutBoardState s) 
	{
		return s.gameOver();
	}
    
    private List<TablutBoardState> getChildren(TablutBoardState parent, List<TablutMove> moves)
    {
    	List<TablutBoardState> children;
    	
    	children = new ArrayList<TablutBoardState>();
    	for (TablutMove move : moves)
    		children.add(getChild(parent, move));
    	return children;
    }
    
    private TablutBoardState getChild(TablutBoardState parent, TablutMove move)
    {
    	TablutBoardState child;
    	
    	child = (TablutBoardState) parent.clone();
    	child.processMove(move);
    	return child;
    }
}