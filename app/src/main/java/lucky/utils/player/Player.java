/**
 * Player.java
 *
 * Player class: Represents a player in the game.
 * Contains information about the player's hand, controller, auto movements, and cards.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */
package lucky.utils.player;

import ch.aplu.jcardgame.*;
import lucky.utils.dealer.Dealer;
import lucky.utils.player.controllers.PlayerController;

import java.util.List;

/**
 * The Player class represents a player in the game.
 * It contains information about the player's hand, controller, auto movements, and cards.
 */
public class Player {
    private final boolean isAuto;
    private final Hand hand;
    private final PlayerController controller;
    private final List<List<Card>> autoMovements;
    boolean finishedAuto = false;
    private final List<Card> cards;
    private int autoIndex = 0;

    private static final int DRAW_CARD_INDEX = 0;
    private static final int DISCARD_CARD_INDEX = 1;

    public Player(List<Card> initialCards, boolean isAuto, List<List<Card>> autoMovements, PlayerController controller) {
        this.controller = controller;
        this.isAuto = isAuto;
        this.autoMovements = autoMovements;
        hand = new Hand(Dealer.DECK);
        cards = initialCards;
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

    public List<Card> getCards() {
        return cards;
    }

    /* setters */

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


    public void checkFinishedAuto(int index) {
        if (!autoMovements.isEmpty()) {
            finishedAuto = autoMovements.get(index).isEmpty();
        }
    }

    public Card drawCard() {
        checkFinishedAuto(autoIndex);

        if (isAuto) {
            System.out.println("auto moves: " + autoMovements);
            return autoMovements.get(autoIndex).get(DRAW_CARD_INDEX);
        }

        // don't care which card is dealt otherwise
        // dealer with deal random card
        System.out.println("auto moves: " + autoMovements);
        return null;
    }

    public Card discardCard() {
        checkFinishedAuto(autoIndex);


        // if game is set to auto
        if (isAuto) {
            System.out.println("auto moves: " + autoMovements);
            // get card and increment autoIndex
            return autoMovements.get(autoIndex++).get(DISCARD_CARD_INDEX);
        }

        // if game is not set to auto or if finishedAuto is true
        // fallback to controller
        return controller.discardCard(hand);
    }
}
