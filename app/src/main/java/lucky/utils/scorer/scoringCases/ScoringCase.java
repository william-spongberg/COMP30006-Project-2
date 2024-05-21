/**
 * ScoringCase.java
 * 
 * An abstract class that represents scoring cases in the game.
 * contains a static list for all scoring methods. 
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.scorer.scoringCases;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;

public abstract class ScoringCase {
    /**
     * generates the scoringCase objects
     *
     * @return a List containing each scoringCase
     */
    public static List<ScoringCase> getScoringCases() {
        List<ScoringCase> scoringCases = new ArrayList<>();
        scoringCases.add(new Case1());
        scoringCases.add(new Case2());
        scoringCases.add(new Case3());
        return scoringCases;
    }

    /**
     * finds the score using a particular scoringCase
     *
     * @param privateCards the player's private cards
     * @param publicCards  the game's public cards
     * @return the number of points given the cards for a particular scoringCase
     */
    public abstract int score(List<Card> privateCards, List<Card> publicCards);
}
