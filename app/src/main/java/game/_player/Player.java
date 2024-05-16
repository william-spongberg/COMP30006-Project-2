package game._player;

import game.Dealer;
import game._player.controller.*;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.HandLayout;
import ch.aplu.jcardgame.TargetArea;

import java.util.List;

public abstract class Player {
    public static final String PLAYER_NAME = "player";

    private String name;
    private boolean isAuto;
    private List<Card> cards;
    private Hand hand;
    private List<Card> sharedCards;
    private Card selected = null;
    private PlayerController controller;

    protected Player(String name, List<Card> initialCards, List<Card> initialSharedCards, boolean isAuto, List<List<Card>> autoMovements) {
        setHand(new Hand(Dealer.DECK));
        sortHand();
        setName(name);
        setCards(initialCards);
        convertListToHand(initialCards);
        setIsAuto(isAuto);
        setSharedCards(initialSharedCards);
        setController(autoMovements);
    }

    public abstract Card drawCard(); // done automatically by dealer if manual player
    public abstract Card discardCard();

    public void convertListToHand(List<Card> cards) {
        for (Card card : cards) {
            hand.insert(card, false);
        }
    }

    public void startListening() {
        hand.setTouchEnabled(true);
    }

    public void stopListening() {
        hand.setTouchEnabled(false);
    }

    /* getters */
    public String getName() {
        return name;
    }

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

    public Card getSelected() {
        Card card = selected;
        selected = null;
        return card;
    }

    public PlayerController getController() {
        return controller;
    }

    /* setters */
    public void setName(String name) {
        if (name == null) {
            this.name = PLAYER_NAME;
        }
        else {
            this.name = name;
        }
    }

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

    public void setSelected(Card selected) {
        this.selected = selected;
    }

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

    public void setController(PlayerController controller) {
        this.controller = controller;
    }

    public void setController(List<List<Card>> autoMovements) {
        if (isAuto()) {
            setController(new AutoController(autoMovements));
        } else {
            setController(new ManualController());
        }
    }
    
}
