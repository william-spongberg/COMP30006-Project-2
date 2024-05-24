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
 * It contains information about the player's hand, controller, auto movements,
 * and cards.
 */
public class Player {
    // constants
    private static final int DRAW_CARD_INDEX = 0;
    private static final int DISCARD_CARD_INDEX = 1;

    private final Hand hand;
    private final PlayerController controller;
    private final List<List<Card>> autoMovements;
    private final List<Card> cards;

    // attributes
    private boolean isAuto = false;
    private boolean finishedAuto = false;
    private int autoIndex = 0;

    /**
     * Constructs a new Player object with the given initial cards, auto mode, auto
     * movements,
     * 
     * @param initialCards  The initial cards for the player.
     * @param isAuto        Whether the player is in auto mode.
     * @param autoMovements The auto movements for the player.
     * @param controller    The controller for the player.
     */
    public Player(List<Card> initialCards, boolean isAuto, List<List<Card>> autoMovements,
            PlayerController controller) {
        this.controller = controller;
        this.isAuto = isAuto;
        this.autoMovements = autoMovements;

        hand = new Hand(Dealer.INITIAL_DECK);
        cards = initialCards;

        sortHand();
        convertListToHand(initialCards);
    }

    /**
     * Converts a list of cards into a hand by inserting each card into the hand.
     * 
     * @param cards the list of cards to convert
     */
    public void convertListToHand(List<Card> cards) {
        if (cards.isEmpty() || cards.get(0) == null) {
            return;
        }
        for (Card card : cards) {
            hand.insert(card, false);
        }
    }

    /**
     * Checks if the auto movement at the specified index has finished.
     *
     * @param index the index of the auto movement to check
     */
    public void checkFinishedAuto(int index) {
        if (!autoMovements.isEmpty()) {
            finishedAuto = autoMovements.get(index).isEmpty();
        }
    }

    /**
     * Draws a card from the player's hand.
     * If the game is set to auto, the card is drawn from the auto movements list.
     * 
     * @return The card drawn from the player's hand, or null if not auto.
     */
    public Card drawCard() {
        checkFinishedAuto(autoIndex);

        if (isAuto && !finishedAuto) {
            return autoMovements.get(autoIndex).get(DRAW_CARD_INDEX);
        }

        // don't care which card is dealt otherwise
        return null;
    }

    /*
     * Discards a card from the player's hand.
     * If the game is set to auto, the card is discarded from the auto movements
     * list, else the card is discarded by the controller.
     * 
     * @return The card discarded from the player's hand.
     */
    public Card discardCard() {
        checkFinishedAuto(autoIndex);

        // if game is set to auto
        if (isAuto) {
            // get card and increment autoIndex
            return autoMovements.get(autoIndex++).get(DISCARD_CARD_INDEX);
        }

        // if game is not set to auto or if finishedAuto is true
        // fallback to controller
        return controller.discardCard(hand);
    }

    // getters

    public List<Card> getCards() {
        return cards;
    }

    // setters

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
}
