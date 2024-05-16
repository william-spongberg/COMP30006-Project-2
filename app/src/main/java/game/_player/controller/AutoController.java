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
        //GameGrid.delay(delayTime);
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
        //GameGrid.delay(delayTime);

        // if list within list is size 2, then it is card wanting to discard
        // else if list within list is size 1, then it is the card wanting to draw

        System.out.println("auto moves: " + getAutoMovements());

        //GameGrid.delay(delayTime);
        if (getAutoMovements().get(autoIndex).size() == 1) {
            autoIndex++;
            System.out.println("auto moves: " + getAutoMovements());
            return getAutoMovements().get(autoIndex-1).remove(0);
        } else {
            // DEBUG: print size
            System.out.println("auto movements size: " + getAutoMovements().get(autoIndex).size());
            
            // pick random card to discard otherwise
            System.out.println("auto moves: " + getAutoMovements());
            return hand.get(random.nextInt(hand.getNumberOfCards()));
        }
    }

    public List<List<Card>> getAutoMovements() {
        return autoMovements;
    }

    public void setAutoMovements(List<List<Card>> autoMovements) {
        this.autoMovements = autoMovements;
    }

    // public Card applyAutoMovement(Hand hand, String nextMovement) {
    //     // if (pack.isEmpty())
    //     //     return null;

    //     String[] cardStrings = nextMovement.split("-");
    //     String cardDealtString = cardStrings[0];
    //     Card dealt = CardUtil.getCardFromList(pack.getCardList(), cardDealtString);

    //     if (dealt != null) {
    //         dealt.removeFromHand(false);
    //         hand.insert(dealt, true);
    //     } else {
    //         System.out.println("cannot draw card: " + cardDealtString + " - hand: " + hand);
    //     }

    //     if (cardStrings.length > 1) {
    //         String cardDiscardString = cardStrings[1];
    //         return CardUtil.getCardFromList(hand.getCardList(), cardDiscardString);
    //     } else {
    //         return null;
    //     }
    // }
}
