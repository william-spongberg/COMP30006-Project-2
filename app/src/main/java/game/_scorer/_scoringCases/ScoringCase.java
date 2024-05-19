package game._scorer._scoringCases;

import ch.aplu.jcardgame.Card;

import java.util.List;

public abstract class ScoringCase {
    /**
     * generates the scoringCase objects
     * @return a List containing each scoringCase
     */
    public static List<ScoringCase> getScoringCases()
    {
        return null;
    }

    /**
     * finds the score using a particular scoringCase
     * @param privateCards the player's private cards
     * @param publicCards the game's public cards
     * @return the number of points given the cards for a particular scoringCase
     */
    public abstract int score(List<Card> privateCards, List<Card> publicCards);
}
