/**
 * PlayerController.java
 *
 * contains the interface PlayerController which states that all 3
 * player controllers must implement behaviour to discard cards
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.player.controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface PlayerController {
    public Card discardCard(Hand hand);
}
