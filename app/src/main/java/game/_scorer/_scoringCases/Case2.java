package game._scorer._scoringCases;

import ch.aplu.jcardgame.Card;

import java.util.List;

public class Case2 extends ScoringCase {
    /**
     * finds the score using case 2
     * @param privateCards the player's private cards
     * @param publicCards the game's public cards
     * @return the number of points given the cards for case 2
     */
    @Override
    public int score(List<Card> privateCards, List<Card> publicCards) {
        return 0;
    }
}
