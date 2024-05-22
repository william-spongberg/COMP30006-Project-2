/**
 * human.java
 *
 * contains the behaviour for the human player when discarding cards
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.player.controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

public class Human implements PlayerController {
    private Card selected = null;
    private static final int DELAY_INTERVAL = 50;
    private CardListener cardListener = new CardAdapter() {
        @Override
        public void leftDoubleClicked(Card card) {
            selected = card;
        }
    };

    public Card discardCard(Hand hand) {
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
