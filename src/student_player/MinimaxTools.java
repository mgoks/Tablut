package student_player;

/**
 * An interface of methods needed for calculating minimax decisions
 * @author Halil Murat Goksel
 */
public interface MinimaxTools 
{
//	/**
//	 * 
//	 * @param state
//	 * @return the action corresponding to the best possible move
//	 * Theoretically this should return the argmax of minValue()
//	 */
//	TablutMove minimaxDecision(TablutBoardState state);
//	
//	/**
//	 * Goes through the whole game tree, all the way to the leaves,
//	 * to determine the backed-up value of a state
//	 * @param state
//	 * @return
//	 */
//	int maxValue(TablutBoardState state);
//	
//	/**
//	 * Goes through the whole game tree, all the way to the leaves,
//	 * to determine the backed-up value of a state
//	 * @param state
//	 * @return
//	 */
//	int minValue(TablutBoardState state);
//	
//	/**
//	 * Basically a wrapper for TBS.gameOver()
//	 * Added it in case if I want to check for conditions other than 
//	 * the game being over to determine if the state is terminal
//	 * @param state
//	 * @return
//	 */
//	boolean terminal(TablutBoardState state);
//	
//	/**
//	 * Utility/evaluation function
//	 * @param state
//	 * @return
//	 */
//	int utility(TablutBoardState state);
//	
//	/**
//	 * Wrapper for TBS.processMove()
//	 * @param state
//	 * @param move
//	 * @return The state after processing the move
//	 */
//	TablutBoardState result(TablutBoardState state, TablutMove move);
}