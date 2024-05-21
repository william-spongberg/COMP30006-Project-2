/**
 * Option1.java
 * 
 * Represents Option 1 summing option in the game - two private cards.
 * contains Option1 class which extends SummingOption.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.scorer.summingOptions;

import ch.aplu.jcardgame.Card;
import lucky.utils.scorer.CardEvaluator;

import java.util.List;

// Option 1: Two private cards
public class Option1 extends SummingOption {
    /**
     * finds if a player can make thirteen using option 1
     *
     * @param privateCards a player's private cards
     * @param publicCards  the game's public cards
     * @return true if the player can make thirteen with option 1, false otherwise
     */
    @Override
    public boolean containsThirteen(List<Card> privateCards, List<Card> publicCards) {
        // assumes there are two and only two private cards
        return isThirteen(privateCards.get(FIRST_CARD), privateCards.get(SECOND_CARD));
    }

    /**
     * finds the score of the given cards if scoring case 3 is used
     *
     * @param privateCards a player's private cards
     * @param publicCards  the game's public cards
     * @return the number of points for these particular cards if scoring case 3 and
     *         option 1 is used
     */
    @Override
    public int case3Score(List<Card> privateCards, List<Card> publicCards) {
        if (!containsThirteen(privateCards, publicCards)) {
            return NOT_THIRTEEN;
        } else {
            int score = 0;
            for (Card card : privateCards) {
                score += CardEvaluator.getCardScore(card, false);
            }
            return score;
        }
    }
}
