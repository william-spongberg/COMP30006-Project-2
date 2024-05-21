/**
 * Case3.java
 * 
 * Represents Case 2 scoring case in the game.
 * Determines the score if multiple players have 13
 * Inherits from the ScoringCase class.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.scorer.scoringCases;

import ch.aplu.jcardgame.Card;
import lucky.utils.scorer.summingOptions.SummingOption;

import java.util.Arrays;
import java.util.List;

// Case 3: multiple players have thirteen
public class Case3 extends ScoringCase {
    /**
     * finds the score using case 3
     *
     * @param privateCards the player's private cards
     * @param publicCards  the game's public cards
     * @return the number of points given the cards for case 3
     */
    @Override
    public int score(List<Card> privateCards, List<Card> publicCards) {
        // find score using each summing option
        List<SummingOption> summingOptions = SummingOption.getSummingOptions();
        int[] scores = new int[summingOptions.size()];
        for (int i = 0; i < summingOptions.size(); i++) {
            scores[i] = summingOptions.get(i).case3Score(privateCards, publicCards);
        }
        // return highest
        return Arrays.stream(scores).max().getAsInt();
    }
}
