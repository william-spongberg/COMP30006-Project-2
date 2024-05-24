/**
 * Case2.java
 * 
 * Represents Case 2 scoring case in the game.
 * Determines the score if no players have 13
 * Inherits from the ScoringCase class.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.scorer.scoring_cases;

import ch.aplu.jcardgame.Card;
import lucky.utils.scorer.CardEvaluator;

import java.util.List;

// No-one has thirteen
public class Case2 extends ScoringCase {
    private static final int FIRST_CARD = 0;
    private static final int SECOND_CARD = 1;

    /**
     * finds the score using case 2
     *
     * @param privateCards the player's private cards
     * @param publicCards  the game's public cards
     * @return the number of points given the cards for case 2
     */
    @Override
    public int score(List<Card> privateCards, List<Card> publicCards) {
        return CardEvaluator.getCardScore(privateCards.get(FIRST_CARD), false)
                + CardEvaluator.getCardScore(privateCards.get(SECOND_CARD), false);
    }
}
