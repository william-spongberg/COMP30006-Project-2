package _player.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

public class ManualController implements PlayerController {
    
    public Card getMove(Hand hand) {
        hand.setTouchEnabled(true);

        // set the status message and deal a card to player 0
        //setStatus("Player 0 is playing. Please double click on a card to discard");
        System.out.println("Player 0 is playing. Please double click on a card to discard");
        
        Card selected = null;
        //dealACardToHand(hand); // TODO: remove, should be done already by dealer

        // wait until a card is selected
        while (selected == null) {
            GameGrid.delay(delayTime);
        }

        // remove selected card from the hand
        selected.removeFromHand(true);
    }
}
