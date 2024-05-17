package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

public class Human implements PlayerController {
    private Card selected = null;

    public Card discardCard(Hand hand) {
        Card discardCard = null;
        startListening(hand);
        while ((discardCard = getSelected()) == null) {
            GameGrid.delay(50);
        }
        return discardCard;
    }
    public void startListening(Hand hand) {
        hand.setTouchEnabled(true);
    }

    public void stopListening(Hand hand) {
        hand.setTouchEnabled(false);
    }
    public Card getSelected() {
        return selected;
    }
    public void resetSelected() {
        selected = null;
    }

    public void setSelected(Card selected) {
        this.selected = selected;
    }
}
