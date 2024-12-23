/**
 * Case1.java
 *
 * Represents Case 1 scoring case in the game.
 * Determines the score based on whether one player has thirteen.
 * Inherits from the ScoringCase class.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.scorer.scoring_cases;

import ch.aplu.jcardgame.Card;
import lucky.utils.scorer.summing_options.SummingOption;

import java.util.List;

// Case 1: one player has thirteen
public class Case1 extends ScoringCase {
    private static final int CASE_1_WIN_SCORE = 100;
    private static final int CASE_1_LOSS_SCORE = 0;

    /**
     * finds the score using case 1
     *
     * @param privateCards the player's private cards
     * @param publicCards  the game's public cards
     * @return the number of points given the cards for case 1
     */
    @Override
    public int score(List<Card> privateCards, List<Card> publicCards) {
        List<SummingOption> summingOptions = SummingOption.getSummingOptions();
        for (SummingOption summingOption : summingOptions) {
            if (summingOption.containsThirteen(privateCards, publicCards)) {
                return CASE_1_WIN_SCORE;
            }
        }
        return CASE_1_LOSS_SCORE;
    }
}
