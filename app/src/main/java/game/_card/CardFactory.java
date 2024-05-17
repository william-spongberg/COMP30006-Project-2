package game._card;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.List;

// TODO: remove class, methods adopted into Dealer class
public class CardFactory {

    // private static final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

    // // TODO: combine getRankFromString and getSuitFromString into one method?
    // public static Rank getRankFromString(String cardName) {
    //     String rankString = cardName.substring(0, cardName.length() - 1);
    //     Integer rankValue = Integer.parseInt(rankString);

    //     for (Rank rank : Rank.values()) {
    //         if (rank.getRankCardValue() == rankValue) {
    //             return rank;
    //         }
    //     }
    //     // default return ACE
    //     System.err.println("Rank not found for card: " + cardName);
    //     return Rank.ACE;
    // }

    // public static Suit getSuitFromString(String cardName) {
    //     String suitString = cardName.substring(cardName.length() - 1, cardName.length());

    //     for (Suit suit : Suit.values()) {
    //         if (suit.getSuitShortHand().equals(suitString)) {
    //             return suit;
    //         }
    //     }
    //     // default return CLUBS
    //     System.err.println("Suit not found for card: " + cardName);
    //     return Suit.CLUBS;
    // }

    // public static Card getCardFromList(List<Card> cards, String cardName) {
    //     Rank cardRank = getRankFromString(cardName);
    //     Suit cardSuit = getSuitFromString(cardName);
    //     for (Card card : cards) {
    //         if (card.getSuit() == cardSuit
    //                 && card.getRank() == cardRank) {
    //             return card;
    //         }
    //     }

    //     return null;
    // }

    // public static Card getCardFromString(String cardName) {
    //     Rank rank = getRankFromString(cardName);
    //     Suit suit = getSuitFromString(cardName);

    //     for (Card card : deck.toHand(false).getCardList()) {
    //         if (card.getRank() == rank && card.getSuit() == suit) {
    //             return card;
    //         }
    //     }
    //     System.err.println("Card not found for card: " + cardName);
    //     return null;
    // }
}
