package game._player.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class ManualController implements PlayerController, CardListener {
    private Card selected = null;
    private Hand hand = null;

    public ManualController (Hand hand) {
        this.hand = hand;
        //this.hand.addCardListener(this);
    }

    public Card drawCard() {
        return null;
    }

    public Card discardCard(Hand hand) {
        selected = null;
        this.hand.insert(hand, false);
        //this.hand.addCardListener(this);

        this.hand.setTouchEnabled(true);
        System.out.println("Player 0 is playing. Please double click on a card to discard");

        // wait until a card is selected
        while (selected == null) {
            GameGrid.delay(50);
        }

        //this.hand.setTouchEnabled(false);

        return selected;
    }

    // public void initCardListener(Hand hand) {
    //     this.cardListener = new CardAdapter() {
    //         @Override
    //         public void leftDoubleClicked(Card card) {
    //             System.out.println("Player selected: " + card);
    //             selected = card;
    //             hand.setTouchEnabled(false);
    //         }
    //     };
    // }

    @Override
    public void rightDoubleClicked(Card card) {
        System.out.println("WOOOOO Player selected: " + card);
        this.selected = card;
        this.hand.setTouchEnabled(false);
    }

    // unused methods

    @Override
    public void leftClicked(Card card) {
        // not used
    }

    @Override
    public void leftPressed(Card card) {
        // not used
    }

    @Override
    public void leftReleased(Card card) {
        // not used
    }

    @Override
    public void leftDoubleClicked(Card card) {
        // not used
    }

    @Override
    public void rightClicked(Card card) {
        // not used
    }

    @Override
    public void rightPressed(Card card) {
        // not used
    }

    @Override
    public void rightReleased(Card card) {
        // not used
    }

    @Override
    public void atTarget(Card card, Location location) {
        // not used
    }
}
