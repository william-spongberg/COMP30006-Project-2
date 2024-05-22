/**
 * DiscardPile.java
 * 
 * the DiscardPile class is used to store the cards that have been discarded by the players.
 * it has a list of cards that have been discarded
 * this is a singleton!
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.dealer;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;


// singleton
public class DiscardPile {

    // defines the only instance
    private static DiscardPile instance;

    // the discardCards this instance can contain
    private List<Card> discardCards;

    // the instance has an arrayList of discarded cards
    private DiscardPile() {
        discardCards = new ArrayList<>();
    }

    // ensures one instance can exist, as this is the single point of entry
    public static synchronized DiscardPile getInstance()
    {
        if (instance == null)
            instance = new DiscardPile();

        return instance;
    }


    public List<Card> getDiscardCards() {
        return this.discardCards; // Return a copy to preserve encapsulation
    }

    public void addDiscardCard(Card discardCard) {
        discardCards.add(discardCard);
    }
}