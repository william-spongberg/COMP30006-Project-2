/**
 * Option3.java
 * 
 * Represents Option 3 summing option in the game - 2 private and 2 public cards.
 * contains Option3 class which extends SummingOption.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.scorer.summingOptions;

import ch.aplu.jcardgame.Card;
import lucky.utils.scorer.CardEvaluator;

import java.util.List;

// Option 3: 2 private and 2 public cards
public class Option3 extends SummingOption {
    /**
     * finds if a player can make thirteen using option 3
     *
     * @param privateCards a player's private cards
     * @param publicCards  the game's public cards
     * @return true if the player can make thirteen with option 3, false otherwise
     */
    @Override
    public boolean containsThirteen(List<Card> privateCards, List<Card> publicCards) {
        // assumes there are 2 private cards and 2 public cards
        for (int privateCard1Value : CardEvaluator.getSumValues(privateCards.get(FIRST_CARD))) {
            for (int privateCard2Value : CardEvaluator.getSumValues(privateCards.get(SECOND_CARD))) {
                for (int publicCard1Value : CardEvaluator.getSumValues(publicCards.get(FIRST_CARD))) {
                    for (int publicCard2Value : CardEvaluator.getSumValues(publicCards.get(SECOND_CARD))) {
                        if (privateCard1Value + privateCard2Value + publicCard1Value + publicCard2Value == THIRTEEN) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * finds the score of the given cards if scoring case 3 is used
     *
     * @param privateCards a player's private cards
     * @param publicCards  the game's public cards
     * @return the number of points for these particular cards if scoring case 3 and option 3 is used
     */
    @Override
    public int case3Score(List<Card> privateCards, List<Card> publicCards) {
        if (!containsThirteen(privateCards, publicCards)) {
            return NOT_THIRTEEN;
        } else {
            return CardEvaluator.getCardScore(privateCards.get(FIRST_CARD), false) + CardEvaluator.getCardScore(privateCards.get(SECOND_CARD), false) + CardEvaluator.getCardScore(publicCards.get(FIRST_CARD), true) + CardEvaluator.getCardScore(publicCards.get(SECOND_CARD), true);
        }
    }
}
