package game._scorer._scoringCases;

import ch.aplu.jcardgame.Card;
import game._scorer._summingOptions.SummingOption;

import java.util.List;

public class Case3 extends ScoringCase {
    private List<SummingOption> summingOptions;
    /**
     * finds the score using case 3
     * @param privateCards the player's private cards
     * @param publicCards the game's public cards
     * @return the number of points given the cards for case 3
     */
    @Override
    public int score(List<Card> privateCards, List<Card> publicCards) {
        return 0;
    }
}
