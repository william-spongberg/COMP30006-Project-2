package game._player._bot;

import java.util.List;
import java.util.Random;

import game._player.controller.AutoController;
import ch.aplu.jcardgame.Card;

public class RandomBot extends Bot {
    // TODO: only define once and use by all, need set random seed for testing
    private static final int SEED = 30008;
    private static final Random random = new Random(SEED);

    public static final String NAME = "random";
    
    public RandomBot(List<Card> initialCards, List<Card> initialSharedCards, boolean isAuto, List<List<Card>> autoMovements) {
        super(NAME, initialCards, initialSharedCards, isAuto, autoMovements);
    }

    public Card drawCard() {
        return getController().drawCard();
    }

    public Card discardCard() {
        // TODO: resolve whether to have bot as seperate controller or player class
        if ((getController() instanceof AutoController)) {
            return getController().discardCard(getHand());
        }
        return getCards().get(random.nextInt(getCards().size()));
    }
}
