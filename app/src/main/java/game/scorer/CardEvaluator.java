package game.scorer;

import ch.aplu.jcardgame.Card;
import game.card.Rank;
import game.card.Suit;

public class CardEvaluator {
    private static final int PUBLIC_MULTIPLIER = 2;

    /**
     * finds the score a card contributes to scoring cases 2 and 3
     *
     * @param card     the card to be scored
     * @param isPublic whether the card is a public card
     * @return the score of the card
     */
    public static int getCardScore(Card card, boolean isPublic) {
        // find card value
        Rank rank = (Rank) card.getRank();
        int score = rank.getScoreCardValue();

        // find multiplier
        int multiplier;
        if (isPublic) {
            multiplier = PUBLIC_MULTIPLIER;
        } else {
            Suit suit = (Suit) card.getSuit();
            multiplier = suit.getMultiplicationFactor();
        }

        return score * multiplier;
    }

    /**
     * finds the summing values of a card
     *
     * @param card the card being summed
     * @return an array of possible summing values
     */
    public static int[] getSumValues(Card card) {
        Rank rank = (Rank) card.getRank();
        return rank.getPossibleSumValues();
    }
}
