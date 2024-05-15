package _player;

import _player._bot.BasicBot;
import _player._bot.CleverBot;
import _player._bot.RandomBot;
import _player.Human;

import ch.aplu.jcardgame.Hand;

import java.util.List;

public class PlayerFactory {
    // factory for player + playercontroller + hand
    // return? can return player? or only player info?

    public Player createPlayer(String playerType, String initialCards, String initialSharedCards, boolean isAuto, List<String> autoMovements) {

        if (playerType.equals("human")) {
            return new Human(initialCards, initialSharedCards, isAuto, autoMovements);
        } else if (playerType.equals("random")) {
            return new RandomBot(initialCards, initialSharedCards, isAuto, autoMovements);
        } else if (playerType.equals("basic")) {
            return new BasicBot(initialCards, initialSharedCards, isAuto, autoMovements);
        } else if (playerType.equals("clever")) {
            return new CleverBot(initialCards, initialSharedCards, isAuto, autoMovements);
        } else {
            System.err.println("Invalid player type: " + playerType);
            return null;
        }
    }
}
