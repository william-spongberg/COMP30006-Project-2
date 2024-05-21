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

public class Random implements PlayerController {

    private final java.util.Random random = new java.util.Random();

    @Override
    public Card discardCard(Hand hand) {
        // TODO: resolve whether to have bot as seperate controller or player class
        return hand.getCardList().get(random.nextInt(hand.getCardList().size()));
    }
}
