package game._player;

import ch.aplu.jcardgame.Card;

import java.util.List;

public class Human extends Player {
    public static final String NAME = "human";

    public Human(List<Card> initialCards, List<Card> initialSharedCards, boolean isAuto, List<List<Card>> autoMovements) {
        super(NAME, initialCards, initialSharedCards, isAuto, autoMovements);
    }

    public Card drawCard() {
        return getController().drawCard();
    }

    public Card discardCard() { // give cards on table as param?
        return getController().discardCard(getHand());
    }
    
}
