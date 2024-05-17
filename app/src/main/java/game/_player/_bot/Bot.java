package game._player._bot;

import game._player.Player;
import ch.aplu.jcardgame.Card;

import java.util.List;

public abstract class Bot extends Player {
    
    protected Bot(String name, List<Card> initialCards, List<Card> initialSharedCards, boolean isAuto, List<List<Card>> autoMovements) {
        super(name, initialCards, initialSharedCards, isAuto, autoMovements);
    }
}
