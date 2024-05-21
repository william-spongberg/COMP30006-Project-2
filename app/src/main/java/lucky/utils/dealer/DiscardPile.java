/**
 * DiscardPile.java
 * 
 * the DiscardPile class is used to store the cards that have been discarded by the players.
 * it has a list of cards that have been discarded.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.dealer;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;
public class DiscardPile {

    public static List<Card> discardCards = new ArrayList<>();

    public static List<Card> getDiscardCards() {
        return discardCards;
    }

    public static void addDiscardCards(Card discardCard) {
        discardCards.add(discardCard);
    }
}
