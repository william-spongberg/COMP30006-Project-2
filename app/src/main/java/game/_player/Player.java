package game._player;

import game.Dealer;
import game._player._controllers.PlayerController;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.HandLayout;
import ch.aplu.jcardgame.TargetArea;

import java.util.List;

public class Player {
    public static final String PLAYER_NAME = "player";
    private boolean isAuto;
    private List<Card> cards;
    private Hand hand;
    private List<Card> sharedCards;
    private final PlayerController controller;
    private final List<List<Card>> autoMovements;
    private int autoIndex = 0;

    public boolean isMouseControlled() {
        return isMouseControlled;
    }

    private final boolean isMouseControlled;

    public Player(List<Card> initialCards, List<Card> initialSharedCards, boolean isAuto, List<List<Card>> autoMovements, PlayerController controller, boolean isMouseControlled) {
        this.controller = controller;
        this.isAuto = isAuto;
        this.autoMovements = autoMovements;
        this.isMouseControlled = isMouseControlled;
        hand = new Hand(Dealer.DECK);
        cards = initialCards;
        sharedCards = initialSharedCards;
        sortHand();
        convertListToHand(initialCards);
    }


    public void convertListToHand(List<Card> cards) {
        if (cards.isEmpty() || cards.get(0) == null) {
            return;
        }
        System.out.println("cards: " + cards);
        for (Card card : cards) {
            hand.insert(card, false);
        }
    }

    /* getters */

    public boolean isAuto() {
        return isAuto;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Hand getHand() {
        return hand;
    }

    public List<Card> getSharedCards() {
        return sharedCards;
    }

    public PlayerController getController() {
        return controller;
    }

    /* setters */

    public void setIsAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void sortHand() {
        hand.sort(Hand.SortType.SUITPRIORITY, false);
    }

    public void setView(CardGame view, HandLayout layout) {
        this.hand.setView(view, layout);
    }

    public void showCards() {
        hand.setVerso(false);
    }

    public void hideCards() {
        hand.setVerso(true);
    }

    public void renderCards() {
        hand.draw();
    }

    // public void startRendering() {
    //     hand.setDo
    // }



    public void setCardListener(CardListener cardListener) {
        hand.addCardListener(cardListener);
    }

    public void setTargetArea(TargetArea targetArea) {
        hand.setTargetArea(targetArea);
    }

    public void addCard(Card card) {
        cards.add(card);
        hand.insert(card, false);
    }

    public void removeCard(Card card) {
        cards.remove(card);
        hand.remove(card, false);
    }

    public void setSharedCards(List<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }

    public Card drawCard() {
        if (isAuto && !autoMovements.get(autoIndex).isEmpty()) {
            if (autoMovements.get(autoIndex).size() == 2) {
                System.out.println("auto moves: " + autoMovements);
                return autoMovements.get(autoIndex).get(0);
            }
        }

        // don't care which card is dealt otherwise
        // dealer with deal random card
        System.out.println("auto moves: " + autoMovements);
        return null;
    }
    public Card discardCard(){
        boolean finishedAuto = autoMovements.get(autoIndex).isEmpty();

        // if game is set to auto
        if (isAuto && !finishedAuto) {
            System.out.println("auto moves: " + autoMovements);
            if (!autoMovements.get(autoIndex).isEmpty()) {
                if (autoMovements.get(autoIndex).size() == 2) {
                    autoIndex++;
                    return autoMovements.get(autoIndex - 1).get(1);
                }
            }
        }

        // if game is not set to auto or if finishedAuto is true
        // fallback to controller
        return controller.discardCard(hand);
    }
}
