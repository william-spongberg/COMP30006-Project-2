/**
 * Suit.java
 * Contains enum Suit, which allwows us to obtain the mutlitplication factor when scoring cards
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package game.card;
public enum Suit {
    SPADES("S", 4), HEARTS("H", 3),
    DIAMONDS("D", 2), CLUBS("C", 1);

    private String suitShortHand = "";
    private int multiplicationFactor = 1;
    public static final int PUBLIC_CARD_MULTIPLICATION_FACTOR = 2;

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