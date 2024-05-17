package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

public class Human implements PlayerController {
    private Card selected = null;
    private Hand hand;
    private CardListener cardListener = new CardAdapter()
    {
        @Override
        public void leftDoubleClicked(Card card) {
            selected = card;
        }
    };

    public Card discardCard(Hand hand) {
        this.hand = hand;
        hand.addCardListener(cardListener);
        selected = null;
        hand.setTouchEnabled(true);
        while (selected == null) {
            GameGrid.delay(50);
        }
        hand.setTouchEnabled(false);
        return selected;
    }
}
