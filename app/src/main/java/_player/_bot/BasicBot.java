package _player._bot;

import java.util.List;
import ch.aplu.jcardgame.Card;

public class BasicBot extends Bot {

    public BasicBot(boolean isAuto, String initialCards, List<String> autoMovements) {
        super(isAuto, initialCards, autoMovements);
    }

    public Card getMove() {
        return getHand().get(random.nextInt(getHand().getNumberOfCards()));
    }

}
