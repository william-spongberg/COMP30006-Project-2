package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;
import game._card.Rank;
import game._card.Suit;

public class Basic implements PlayerController {

    Card smallestCard;

    // TODO: ACCESS THIS PROPERLY, ITS PRIVATE IN LUCKYTHIRDTEEN
    private int getScorePrivateCard(Card card) {
        Rank rank = (Rank) card.getRank();
        Suit suit = (Suit) card.getSuit();

        return rank.getScoreCardValue() * suit.getMultiplicationFactor();
    }


    @Override
    public Card discardCard(Hand hand) {
        ArrayList<Card> cardsInHand = hand.getCardList();
        Card smallestCard = cardsInHand.get(0);

        for (int i = 1; i < cardsInHand.size(); i++) {
            Card currentCard = cardsInHand.get(i);
            if (getScorePrivateCard(currentCard) < getScorePrivateCard(smallestCard)) {
                smallestCard = currentCard;
            }
        }
        return smallestCard;
    }
}
