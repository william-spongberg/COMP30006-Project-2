package _player.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

public class ManualController implements PlayerController {

    Card selected = null;
    CardListener cardListener = new CardAdapter() // Human Player plays card
    {
        @Override
        public void leftDoubleClicked(Card card) {
            this.selected = card;
            //hands[0].setTouchEnabled(false);
        }
    };
    
    public Card getMove(Hand hand) {
        hand.setTouchEnabled(true);

        // set the status message and deal a card to player 0
        //setStatus("Player 0 is playing. Please double click on a card to discard");
        System.out.println("Player 0 is playing. Please double click on a card to discard");

        //dealACardToHand(hand); // TODO: remove, should be done already by dealer
        selected = null;

        // wait until a card is selected
        while (selected == null) {
            GameGrid.delay(delayTime);
        }

        // remove selected card from the hand
        selected.removeFromHand(true);
    }

    
}
