/**
 * SummingOption.java
 * 
 * Represents the summing options in the game.
 * contains the abstract SummingOption class which is extended by Option1, Option2, and Option3.
 * has a list which contains all the summing options.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.scorer.summing_options;

import ch.aplu.jcardgame.Card;
import lucky.utils.scorer.CardEvaluator;

import java.util.ArrayList;
import java.util.List;

public abstract class SummingOption {
    // constants used by concrete SummingOption classes
    protected static final int THIRTEEN = 13;
    protected static final int FIRST_CARD = 0;
    protected static final int SECOND_CARD = 1;
    protected static final int NOT_THIRTEEN = 0;

    /**
     * generates the summingOption objects
     *
     * @return a List containing each summingOption
     */
    public static List<SummingOption> getSummingOptions() {
        List<SummingOption> summingOptions = new ArrayList<>();
        summingOptions.add(new Option1());
        summingOptions.add(new Option2());
        summingOptions.add(new Option3());
        return summingOptions;
    }

    /**
     * finds if two cards add to thirteen
     *
     * @param card1 the first card
     * @param card2 the second card
     * @return true if a thirteen can be made with these cards, false otherwise
     */
    public boolean isThirteen(Card card1, Card card2) {
        for (int card1Value : CardEvaluator.getSumValues(card1)) {
            for (int card2Value : CardEvaluator.getSumValues(card2)) {
                if (card1Value + card2Value == THIRTEEN) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * finds if a player can make thirteen using a particular scoringOption
     *
     * @param privateCards a player's private cards
     * @param publicCards  the game's public cards
     * @return true if the player can make thirteen with a particular scoring
     *         option, false otherwise
     */
    public abstract boolean containsThirteen(List<Card> privateCards, List<Card> publicCards);

    /**
     * finds the score of the given cards if scoring case 3 is used
     *
     * @param privateCards a player's private cards
     * @param publicCards  the game's public cards
     * @return the number of points for these particular cards if scoring case 3 and
     *         this summing option is used
     */
    public abstract int case3Score(List<Card> privateCards, List<Card> publicCards);
}
