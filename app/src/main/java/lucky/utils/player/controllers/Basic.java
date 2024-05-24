/**
 * Basic.java
 * contains the behaviour for the basic player when discarding cards, which is to always
 * discard the lowest one
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.player.controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;

import static lucky.utils.scorer.CardEvaluator.getCardScore;

public class Basic implements PlayerController {

    /*
     * Discards the card with the lowest score
     * 
     * @param hand the hand of the player
     * @return the card to discard
     */
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
