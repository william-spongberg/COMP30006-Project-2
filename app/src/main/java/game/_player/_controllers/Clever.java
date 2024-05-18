package game._player._controllers;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;

public class Clever implements PlayerController {

    @Override
    public Card discardCard(Hand hand) {

        //TODO: THIS IS A PLACERHOLDER! REPLACE WITH APPROPRIATE GETTER ONCE IMPLEMENTED
        Hand packRemaining = hand;
        ArrayList<Card> cardsPlayed = new ArrayList<>();

        switch (monteCarloMoveEstimation(packRemaining, cardsPlayed, hand)) {
            case 0:
                return hand.getCardList().get(0);
            case 1:
                return hand.getCardList().get(1);
            case 2:
                return hand.getCardList().get(2);
            return null;
        }
    }




    // THINGS TO NOTE:
        // EVERY PLAYER CAN SEE THE DISCARDED CARD
        // so we know what cards aren't around anymore.
        // this can be used as an optimisation
        // we know how many cards are left
        // we know what card we have and what are private
        // the goal: estimate which card we should remove using monte carlo

    // return index of card to discard
    // apply monte carlo estimation
    private Integer monteCarloMoveEstimation(Hand packRemaining, ArrayList<Card> cardsPlayed, Hand hand) {
        return 0;
    }
}
