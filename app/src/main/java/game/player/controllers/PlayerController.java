package game.player.controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface PlayerController {
    public Card discardCard(Hand hand);
}
