package game;
import game._card.CardFactory;
import game._card.CardUtil;
import game._card.Rank;
import game._card.Suit;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.Location;

import java.util.List;
import java.util.Random;

public class Dealer {
    // TODO: only define once and use by all, need set random seed for testing
    private static final int SEED = 30008;
    private static final Random random = new Random(SEED);

    // testing
    public static final Deck DECK = new Deck(Suit.values(), Rank.values(), "cover");

    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

    // public Dealer () {
    //     shuffle();
    // }

    // deck converted into usable Hand (unshuffled)
    private Hand pack = deck.toHand(false);

    public void shuffle() {
        // shuffle the pack
        pack.shuffle(false);
    }

    public void discardCard(Card card) {
        // remove card from pack
        pack.remove(card, false);
    }

    public Card getCard(boolean discard) {
        // get top card from pack
        Card card = pack.get(0);
        if (card == null)
            return null;
        if (discard)
            discardCard(card);
        return card;
    }

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

    public Card getRandomCard(boolean discard) {
        // get random card from pack
        Card card = pack.get(random.nextInt(pack.getNumberOfCards()));
        if (card == null)
            return null;
        if (discard)
            discardCard(card);
        return card;
    }

    public Card dealCard() {
        // deals a random card from the pack
        if (pack.isEmpty()) {
            System.err.println("No more cards in deck");
            return null;
        }

        return getRandomCard(true);
    }

    public static Rank getRankFromString(String cardName) {
        // get rank from card name
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }
        // default return ACE
        System.err.println("Rank not found for card: " + cardName);
        return Rank.ACE;
    }

    public static Suit getSuitFromString(String cardName) {
        // get suit from card name
        String suitString = cardName.substring(cardName.length() - 1, cardName.length());

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        // default return CLUBS
        System.err.println("Suit not found for card: " + cardName);
        return Suit.CLUBS;
    }

    public Deck getDeck() {
        return deck;
    }
}