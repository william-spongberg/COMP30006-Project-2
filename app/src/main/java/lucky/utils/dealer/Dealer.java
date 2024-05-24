/**
 * Dealer.java
 * 
 * The Dealer class is used to handle the deck of cards in the game.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.dealer;

import lucky.LuckyThirdteen;
import lucky.utils.card.Rank;
import lucky.utils.card.Suit;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class Dealer {
    // constants
    public static final Deck INITIAL_DECK = new Deck(Suit.values(), Rank.values(), "cover");

    // attributes
    private static final Deck deck = INITIAL_DECK;
    private final Hand pack = deck.toHand(false);

    public Dealer(boolean shuffle) {
        if (shuffle)
            shuffle();
    }

    /**
     * Shuffles the pack of cards.
     */
    public void shuffle() {
        pack.shuffle(false);
    }

    /**
     * Discards a card from the pack.
     *
     * @param card the card to be discarded
     */
    public void discardCard(Card card) {
        // remove card from pack
        pack.remove(card, false);
    }

    /**
     * Retrieves a card from the pack based on the given card name.
     * If the discard flag is set to true, the card will be discarded from the pack.
     *
     * @param cardName The name of the card to retrieve.
     * @param discard  A flag indicating whether to discard the card from the pack.
     * @return The card object matching the given card name, or null if the card is
     *         not found.
     */
    public Card getCard(String cardName, boolean discard) {
        // get card from pack based on card name
        Rank rank = getRankFromString(cardName);
        Suit suit = getSuitFromString(cardName);

        for (Card card : pack.getCardList()) {
            if (card.getRank() == rank && card.getSuit() == suit) {
                if (discard)
                    discardCard(card);
                return card;
            }
        }

        try {
            throw new Exception("Card not found: " + cardName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Gets a card from the pack based on the given card.
     * 
     * @param card the card to get
     * @param discard whether to discard the card
     * @return the card
     */
    public Card getCard(Card card, boolean discard) {
        for (Card packCard : pack.getCardList()) {
            if (card.getRank() == packCard.getRank() && card.getSuit() == packCard.getSuit()) {
                if (discard)
                    discardCard(packCard);
                return packCard;
            }
        }

        try {
            throw new Exception("Card not found: " + card);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Gets a random card from the pack.
     * 
     * @param discard whether to discard the card
     * @return the card
     */
    public Card getRandomCard(boolean discard) {
        // get random card from pack
        Card card = pack.get(LuckyThirdteen.RANDOM.nextInt(pack.getNumberOfCards()));
        if (card == null)
            return null;
        if (discard)
            discardCard(card);
        return card;
    }

    /*
     * Gets the rank from the given card name.
     * 
     * @param cardName the name of the card
     * @return the rank
     */
    public static Rank getRankFromString(String cardName) {
        // get rank from card name
        String rankString = cardName.substring(0, cardName.length() - 1);
        int rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }

        try {
            throw new Exception("Rank not found for card: " + cardName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Gets the suit from the given card name.
     * 
     * @param cardName the name of the card
     * @return the suit
     */
    public static Suit getSuitFromString(String cardName) {
        // get suit from card name
        String suitString = cardName.substring(cardName.length() - 1);

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }

        try {
            throw new Exception("Suit not found for card: " + cardName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
