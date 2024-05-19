package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;
import game._card.Rank;
import game._card.Suit;

import static game._scorer.CardEvaluator.getCardScore;

public class Basic implements PlayerController {

    Card smallestCard;

    @Override
    public Card discardCard(Hand hand) {
        ArrayList<Card> cardsInHand = hand.getCardList();
        Card smallestCard = cardsInHand.get(0);

        for (int i = 1; i < cardsInHand.size(); i++) {
            Card currentCard = cardsInHand.get(i);
            if (getCardScore(currentCard, false) < getCardScore(smallestCard, false)) {
                smallestCard = currentCard;
            }
        }
        return smallestCard;
    }
}
