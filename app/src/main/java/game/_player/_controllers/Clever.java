package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class Clever implements PlayerController {

    @Override
    public Card discardCard(Hand hand) {

        //TODO: THIS IS A PLACERHOLDER! REPLACE WITH APPROPRIATE GETTER ONCE IMPLEMENTED
        // we need to get the count of how many cards are left in the pack. We can use this for probability purposes.
        // Hand packRemaining = hand;
        ArrayList<Card> cardsPlayed = new ArrayList<>();

        switch (cleverCardToRemove(cardsPlayed, hand)) {
            case 0:
                return hand.getCardList().get(0);
            case 1:
                return hand.getCardList().get(1);
            case 2:
                return hand.getCardList().get(2);

        }
        return null;
    }

    // THINGS TO NOTE:
        // EVERY PLAYER CAN SEE THE DISCARDED CARDs
        // so we know what cards aren't around anymore.
        // this can be used as an optimisation
        // we know how many cards are left
        // we know what cards we have (3) and what the two public cards are
        // the goal: cleverly decide which card we should remove
        // only four rounds
    // return index of card to discard
    private Integer cleverCardToRemove(ArrayList<Card> cardsPlayed, Hand hand) {
        ArrayList<Card> cardsInHand = hand.getCardList();
        // worth noting, all face cards have a higher chance of making thirteen. Each face card can be 10, 11, 12, or 13
        // additionally, ace can be 0 or 1



        return 0;
    }
}
