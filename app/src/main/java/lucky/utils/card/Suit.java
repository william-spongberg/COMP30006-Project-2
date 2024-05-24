/**
 * Suit.java
 * Contains enum Suit, which allows us to obtain the multiplication factor when scoring cards
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.card;

public enum Suit {
    SPADES("S", 4), HEARTS("H", 3),
    DIAMONDS("D", 2), CLUBS("C", 1);

    private final String suitShortHand;
    private final int multiplicationFactor;

    Suit(String shortHand, int multiplicationFactor) {
        this.suitShortHand = shortHand;
        this.multiplicationFactor = multiplicationFactor;
    }

    public String getSuitShortHand() {
        return suitShortHand;
    }

    public int getMultiplicationFactor() {
        return multiplicationFactor;
    }
}