package game;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;
// info expert for use by clever.
public class DiscardPile {

    public static List<Card> discardCards = new ArrayList<>();

    public static List<Card> getDiscardCards() {
        return discardCards;
    }

    public static void addDiscardCards(Card discardCard) {
        discardCards.add(discardCard);
    }
}
