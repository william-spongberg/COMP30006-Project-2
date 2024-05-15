package _player._bot;

import java.util.List;
import ch.aplu.jcardgame.Card;

public class RandomBot extends Bot {
    
    public RandomBot(boolean isAuto, String initialCards, List<String> autoMovements) {
        super(isAuto, initialCards, autoMovements);
    }

    public Card getMove() {
        // randomly pick a card from the hand to discard
        return getHand().get(random.nextInt(getHand().getNumberOfCards()));
    }
}
