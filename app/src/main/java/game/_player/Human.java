package game._player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jgamegrid.GameGrid;

import java.util.List;

public class Human extends Player {
    public static final String NAME = "human";

    public Human(List<Card> initialCards, List<Card> initialSharedCards, boolean isAuto, List<List<Card>> autoMovements) {
        super(NAME, initialCards, initialSharedCards, isAuto, autoMovements);
    }

    public Card drawCard() {
        return getController().drawCard();
    }

    public Card discardCard() {
        Card discardCard = null;
        resetSelected();
        if (!isAuto()) {
            startListening();
            while ((discardCard = getSelected()) == null) {
                GameGrid.delay(50);
            }
            return discardCard;
        }
        return getController().discardCard(getHand());
    }
    
}
