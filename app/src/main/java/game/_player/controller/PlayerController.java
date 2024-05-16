package game._player.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

public interface PlayerController {
    
    public abstract Card drawCard(); // done automatically by dealer if manual player
    public abstract Card discardCard(Hand hand);
}
