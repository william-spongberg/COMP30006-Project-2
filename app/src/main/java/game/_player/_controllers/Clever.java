package game._player._controllers;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game._card.Rank;
import game._card.Suit;

import java.util.ArrayList;

public class Clever implements PlayerController {

    @Override
    public Card discardCard(Hand hand) {

        //TODO: THIS IS A PLACERHOLDER! REPLACE WITH APPROPRIATE GETTER ONCE IMPLEMENTED
        Hand packRemaining = hand;
        ArrayList<Card> cardsPlayed = new ArrayList<>();

        switch (cleverCardToRemove(packRemaining, cardsPlayed, hand)) {
            case 0:
                return hand.getCardList().get(0);
            case 1:
                return hand.getCardList().get(1);
            case 2:
                return hand.getCardList().get(2);

        }
        return null;
    }

    // TODO: ACCESS THIS PROPERLY, ITS PRIVATE IN LUCKYTHIRDTEEN
    public int getScorePublicCard(Card card) {
        Rank rank = (Rank) card.getRank();
        return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
    }

    // TODO: ACCESS THIS PROPERLY, ITS PRIVATE IN LUCKYTHIRDTEEN
    private int getScorePrivateCard(Card card) {
        Rank rank = (Rank) card.getRank();
        Suit suit = (Suit) card.getSuit();

        return rank.getScoreCardValue() * suit.getMultiplicationFactor();
    }

    // THINGS TO NOTE:
        // EVERY PLAYER CAN SEE THE DISCARDED CARDs
        // so we know what cards aren't around anymore.
        // this can be used as an optimisation
        // we know how many cards are left
        // we know what card we have and what are private
        // the goal: cleverly decide which card we should remove
        // only four rounds
    // return index of card to discard
    private Integer cleverCardToRemove(Hand packRemaining, ArrayList<Card> cardsPlayed, Hand hand) {
        ArrayList<Card> cardsInHand = hand.getCardList();

        return 0;
    }
}
