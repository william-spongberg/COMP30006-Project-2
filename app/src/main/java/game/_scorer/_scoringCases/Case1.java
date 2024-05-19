package game._scorer._scoringCases;

import ch.aplu.jcardgame.Card;

import java.util.List;

public class Case1 extends ScoringCase {
    /**
     * finds the score using case 1
     * @param privateCards the player's private cards
     * @param publicCards the game's public cards
     * @return the number of points given the cards for case 1
     */
    @Override
    public int score(List<Card> privateCards, List<Card> publicCards) {
        return 0;
    }
}
