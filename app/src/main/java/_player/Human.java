package _player;

import _player.controller.PlayerController;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

public class Human extends Player {
    
    public Human(PlayerController controller, Hand hand) {
        super(controller, hand);
    }

    public Human(String initialCards, boolean isAuto, List<String> autoMovements) {
        super(isAuto, initialCards, autoMovements);
    }

    public Card getMove() { // give cards on table as param?
        return getController().getMove(getHand());
    }
    
}
