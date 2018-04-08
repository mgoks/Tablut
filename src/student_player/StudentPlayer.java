package student_player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class StudentPlayer extends TablutPlayer 
{
	private static final int MUSC	= TablutBoardState.MUSCOVITE;
	private static final int SWEDE	= TablutBoardState.SWEDE;
	
	private static final double W1 = 1.0;
	private static final double W2 = 1.25;
	
	private static final int MAX_SEARCH_DEPTH 				= 2;
	private static final Random rand						= new Random();
//	private static 		 int n_initialMuscoviteNeighbours	= 8;
	private static final int BLOCKADE						= 10;
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
    	int counter;
    	
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
    	counter = 0;
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
//    	System.out.println(boardState.getTurnNumber());
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
    	// h(node) = w1*f1 + w2*f2
    	
    	/*
    	 * TODO
    	 * 	- enforce diagonal neighbors for M
    	 * 	- add another heuristic function: proximity to King (?)
    	 */

    	double f1 = 0d, f2 = 0d, f3 = 0d, kingsDistanceToClosestCorner;
    	int opponent, n_muscovitesNeighbours, totalDistanceToKing;
    	Coord kingPos;
    	Set<Coord> myPieceCoords;
    	List<Coord>blockadeCoords;
    	
//    	if (node.getTurnNumber() > 44)
    	opponent = 1 - player_id;
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
    	
    	// f3: For the first few turns enforce a Muscovite blockade
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
    		
    		for (Coord blockadeCoord : blockadeCoords){
    			if (node.getPieceAt(blockadeCoord) == TablutBoardState.Piece.BLACK){
    				f3 += 2.5;
//    				for (Coord neighbour : Coordinates.getNeighbors(blockadeCoord)){
//    					if(node.getPieceAt(neighbour) == TablutBoardState.Piece.BLACK){
//    						f3 += 10;
//    					}
//    				}
    			}
    		}
    	}
//    	System.out.println("turn #" + node.getTurnNumber() + ", f3: " + f3);
    	return W1*f1 + W2*f2 + f3;
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
    
    /**
     * Considering all possible moves all the time is too expensive.
     * Why don't we add some randomness to the game and consider a random subset of 
     * all possible moves?
     * 
     * @param s Board state to get moves for
     * @param n number of legal moves we want
     * @return
     */
//    private List<TablutMove> getSomeLegalMoves(TablutBoardState s, int n)
//    {
//    	List<TablutMove> allPossibleMoves, somePossibleMoves;
//    	int i;
//    	
//    	allPossibleMoves = s.getAllLegalMoves();
//    	for (i = 0; i < allPossibleMoves.size()/n)
//    }
}