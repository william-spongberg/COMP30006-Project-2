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

        }
        return null;
    }




    // THINGS TO NOTE:
        // EVERY PLAYER CAN SEE THE DISCARDED CARD
        // so we know what cards aren't around anymore.
        // this can be used as an optimisation
        // we know how many cards are left
        // we know what card we have and what are private
        // the goal: estimate which card we should remove using monte carlo

    // THE PROCESS:
        // using the cardsPlayed, which contains all discarded cards, and the amount of cards left, simulate all possible
        // hands and moves of other players through Monte Carlo estimation
        // decide what card we should discard of the up to three in our hand (always at least two) based on probability of getting 13
        // we can get thirteen from 3 different ways:
            // two private cards in hand add to thirteen
            // one private card in hand and one in from two public card
            // two public cards and two private cards
        // only four rounds
    // return index of card to discard
    // apply monte carlo estimation
    private Integer monteCarloMoveEstimation(Hand packRemaining, ArrayList<Card> cardsPlayed, Hand hand) {
        ArrayList<Card> cardsInHand = hand.getCardList();

        // set up simulation params
        int simulations = 1000;

        // track successes for each time we discard in the sims
        int[] discardSucceses = new int[cardsInHand.size()];

        // simulate
        for (int i = 0; i < simulations; i++) {
            // get what we know is left in the deck
            ArrayList<Card> remainingDeck = new ArrayList<>(packRemaining.getCardList());
            remainingDeck.removeAll(cardsPlayed);
            remainingDeck.removeAll(cardsInHand);

            for (int j = 0; j < cardsInHand.size(); j++) {
                // simulate for each card in your hand

                // draw cards for other players

                // check if any combo sums to 13
            }
        }
        // find the card with the highest success rate, discard



        return 0;
    }
}
