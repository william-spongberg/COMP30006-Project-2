package _player._bot;

import _player.Player;
import _player.controller.PlayerController;

import ch.aplu.jcardgame.Hand;

import java.util.List;

public abstract class Bot extends Player {
    
    protected Bot(PlayerController controller, Hand hand) {
        super(controller, hand);
    }

    protected Bot(boolean isAuto, String initialCards, List<String> autoMovements) {
        super(initialCards, isAuto, autoMovements);
    }
}
