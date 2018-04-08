package student_player;

import java.util.ArrayList;
import java.util.List;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class StudentPlayer extends TablutPlayer 
{
	private static final double W1 = 1.0;
	private static final double W2 = 1.25;

	private static final int MUSC				= TablutBoardState.MUSCOVITE;
	private static final int MAX_SEARCH_DEPTH 	= 2;
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
    	depth = MAX_SEARCH_DEPTH;
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
    	return bestMove;
    }
    
    /**
     * Minimax-search algorithm with alpha-beta pruning
     * @param node
     * 		The node of state-tree to evaluate
     * @param depth
     * 		The number of level to recursively call the search algorithm
     * @param a
     * 		alpha value
     * @param b
     * 		beta value
     * @param maximizingPlayer
     * 		True if the method is called on a max-node, false otherwise
     * @return
     * 		the minimax value of node
     */
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
    
    /**
     * Evaluation function
     * @param node 
     * 		The game state to evaluate
     * @return the heuristic value of the state
     */
    private double heuristicValue(TablutBoardState node)
    {
    	double f1 = 0.0, f2 = 0.0, f3 = 0.0, kingsDistanceToClosestCorner;
    	int opponent;
    	Coord kingPos;
    	List<Coord>blockadeCoords;
    	
    	opponent = 1 - player_id; // get opponent
    	if(node.getWinner() == player_id)
    		return Double.POSITIVE_INFINITY;
    	else if (node.getWinner() == opponent)
    		return Double.NEGATIVE_INFINITY;

    	// f1 = (# my pieces) - (# opponent pieces)
    	f1 = node.getNumberPlayerPieces(player_id) - node.getNumberPlayerPieces(opponent);
    	
    	// f2: King's Manhattan-distance to the closest corner;
    	kingPos = node.getKingPosition();
    	kingsDistanceToClosestCorner = Coordinates.distanceToClosestCorner(kingPos);
    	f2 = player_id == MUSC ? kingsDistanceToClosestCorner : -kingsDistanceToClosestCorner;
    	
    	// f3: For the first 25 turns enforce a Muscovite blockade
    	// Note: Coordinates start at zero.
    	if (player_id == MUSC && node.getTurnNumber() < 25){
    		// Make the list of coordinates to place the blockade around
    		blockadeCoords = new ArrayList<Coord>(4);
    		blockadeCoords.add(Coordinates.get(1,3));
    		blockadeCoords.add(Coordinates.get(1,4));
    		blockadeCoords.add(Coordinates.get(1,5));
    		blockadeCoords.add(Coordinates.get(2,6));
    		blockadeCoords.add(Coordinates.get(3,7));
    		blockadeCoords.add(Coordinates.get(4,7));
    		blockadeCoords.add(Coordinates.get(5,7));
    		blockadeCoords.add(Coordinates.get(6,6));
    		blockadeCoords.add(Coordinates.get(7,5));
    		blockadeCoords.add(Coordinates.get(7,4));
    		blockadeCoords.add(Coordinates.get(7,3));
    		blockadeCoords.add(Coordinates.get(6,2));
    		blockadeCoords.add(Coordinates.get(5,1));
    		blockadeCoords.add(Coordinates.get(4,1));
    		blockadeCoords.add(Coordinates.get(3,1));
    		blockadeCoords.add(Coordinates.get(2,2));

    		for (Coord blockadeCoord : blockadeCoords)
    			if (node.getPieceAt(blockadeCoord) == TablutBoardState.Piece.BLACK)
    				f3 += 1.5;
    	}
    	return W1*f1 + W2*f2 + f3;
    }
    
    private boolean isTerminal(TablutBoardState s) 
	{
		return s.gameOver();
	}
    
    /**
     * 
     * @param parent
     * @param moves
     * @return all child nodes of parent state after applying the moves
     */
    private List<TablutBoardState> getChildren(TablutBoardState parent, List<TablutMove> moves)
    {
    	List<TablutBoardState> children;
    	
    	children = new ArrayList<TablutBoardState>();
    	for (TablutMove move : moves)
    		children.add(getChild(parent, move));
    	return children;
    }
    
    /**
     * 
     * @param parent
     * @param move
     * @return the child node of the parent state after applying the move
     */
    private TablutBoardState getChild(TablutBoardState parent, TablutMove move)
    {
    	TablutBoardState child;
    	
    	child = (TablutBoardState) parent.clone();
    	child.processMove(move);
    	return child;
    }
}