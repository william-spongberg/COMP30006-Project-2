package game._player.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class AutoController implements PlayerController {
    // TODO: only define once and use by all, need set random seed for testing
    private static final int SEED = 30008;
    private static final Random random = new Random(SEED);

    private List<List<Card>> autoMovements = new ArrayList<>();
    private int autoIndex = 0;

    public AutoController(List<List<Card>> autoMovements) {
        setAutoMovements(autoMovements);
    }

    public Card drawCard() {
        if (getAutoMovements().get(autoIndex).size() == 2) {
            System.out.println("auto moves: " + getAutoMovements());
            return getAutoMovements().get(autoIndex).remove(0);
        } else {
            // don't care which card is dealt otherwise
            // dealer with deal random card
            System.out.println("auto moves: " + getAutoMovements());
            return null;
        }
    }

    public Card discardCard(Hand hand) {
        // if list within list is size 2, then it is card wanting to discard
        // else if list within list is size 1, then it is the card wanting to draw

        System.out.println("auto moves: " + getAutoMovements());
        if (getAutoMovements().get(autoIndex).size() == 1) {
            autoIndex++;
            return getAutoMovements().get(autoIndex - 1).remove(0);
        } else {
            // pick random card to discard otherwise
            return hand.get(random.nextInt(hand.getNumberOfCards()));
        }
    }

    public List<List<Card>> getAutoMovements() {
        return autoMovements;
    }

    public void setAutoMovements(List<List<Card>> autoMovements) {
        this.autoMovements = autoMovements;
    }
}
