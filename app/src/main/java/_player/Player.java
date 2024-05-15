package _player;

import _player.controller.*;

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;

import java.util.List;

public abstract class Player {

    private PlayerController controller;
    private Hand hand; // copy and keep track of own hand
    // TODO: better to have list of cards so can use cardFactory method??

    private List<Card> cards;
    
    // can subscribe to the player for info?

    protected Player(PlayerController controller, Hand hand) {
        setController(controller);
        setHand(hand);
    }

    protected Player(String initialCards, boolean isAuto, List<String> autoMovements) {

        // convert initialCards to Hand/Deck/List of cards
        // use cardFactory method to create cards?

        setHand(new Hand(deck)); // create hand from initial cards
        // TODO: use new CardFactory().createCards(initialCards) instead of new Hand(deck)
        setController(isAuto, autoMovements); // set controller based on isAuto
    }

    public abstract Card getMove(); // give cards on table as param?

    /* getters */
    public PlayerController getController() {
        return controller;
    }

    public Hand getHand() {
        return hand;
    }

    /* setters */
    public void setController(PlayerController controller) {
        this.controller = controller;
    }

    public void setController(boolean isAuto, List<String> autoMovements) {
        if (isAuto) {
            setController(new AutoController(autoMovements));
        } else {
            setController(new ManualController());
        }
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
