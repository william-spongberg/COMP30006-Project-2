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

    private static DiscardPile instance;
    private List<Card> discardCards;

    private DiscardPile() {
        discardCards = new ArrayList<>();
    }

    public static synchronized DiscardPile getInstance()
    {
        if (instance == null)
            instance = new DiscardPile();

        return instance;
    }


    public List<Card> getDiscardCards() {
        return new ArrayList<>(discardCards); // Return a copy to preserve encapsulation
    }

    public void addDiscardCard(Card discardCard) {
        discardCards.add(discardCard);
    }
}