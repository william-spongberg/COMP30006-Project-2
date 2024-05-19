package game._scorer._scoringCases;

import ch.aplu.jcardgame.Card;
import game._scorer.CardEvaluator;

import java.util.List;

// No-one has thirteen
public class Case2 extends ScoringCase {
    private static final int FIRST_CARD = 0;
    private static final int SECOND_CARD = 1;

    /**
     * finds the score using case 2
     *
     * @param privateCards the player's private cards
     * @param publicCards  the game's public cards
     * @return the number of points given the cards for case 2
     */
    @Override
    public int score(List<Card> privateCards, List<Card> publicCards) {
        return CardEvaluator.getCardScore(privateCards.get(FIRST_CARD), false) + CardEvaluator.getCardScore(privateCards.get(SECOND_CARD), false);
    }
}
