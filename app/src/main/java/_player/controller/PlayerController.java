package _player.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface PlayerController {
    
    public Card getMove(Hand hand);
}
