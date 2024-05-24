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
    // constants
    private static final int DELAY_INTERVAL = 50;

    // attributes
    private Card selected = null;
    private final CardListener cardListener = new CardAdapter() {
        /**
         * Sets the selected card to the card that was left double-clicked
         * 
         * @param card the card that was left double-clicked
         */
        @Override
        public void leftDoubleClicked(Card card) {
            selected = card;
        }
    };

    /*
     * Discards a card from the hand using manual input from a human
     * 
     * @param hand the hand of the player
     * @return the card to discard
     */
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
