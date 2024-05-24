/**
 * Random.java
 *
 * contains the class Random
 * which implements the PlayerController interface which has the discardCard method
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.player.controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import lucky.LuckyThirdteen;

public class Random implements PlayerController {

    /**
     * Discards a random card from the hand.
     * 
     * @param hand the hand of the player
     * @return the card to discard
     */
    @Override
    public Card discardCard(Hand hand) {
        return hand.getCardList().get(LuckyThirdteen.RANDOM.nextInt(hand.getCardList().size()));
    }
}
