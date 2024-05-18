package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

public class Human implements PlayerController {
    private Card selected = null;
    private static final int DELAY_INTERVAL = 50;
    private CardListener cardListener = new CardAdapter()
    {
        @Override
        public void leftDoubleClicked(Card card) {
            selected = card;
        }
    };

    public Card discardCard(Hand hand) {
        //TODO: is it necessary to set the hand and add listener every time?
        hand.addCardListener(cardListener);
        selected = null;
        hand.setTouchEnabled(true);
        // wait for leftDoubleClicked to set selected
        while (selected == null) {
            GameGrid.delay(DELAY_INTERVAL);
        }
        hand.setTouchEnabled(false);
        return selected;
    }
}
