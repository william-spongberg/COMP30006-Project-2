/**
 * DiscardPile.java
 * 
 * Used to store the cards that have been discarded by the players in a list.
 * Is a singleton class, so only one instance can exist.
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

    // singleton instance
    private static DiscardPile instance;

    // attributes
    private List<Card> discardCards;

    // constructor
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

    // getters
    public List<Card> getDiscardCards() {
        return this.discardCards;
    }

    // setters
    public void addDiscardCard(Card discardCard) {
        discardCards.add(discardCard);
    }
}