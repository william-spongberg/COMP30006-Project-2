package lucky.utils.player;

import ch.aplu.jcardgame.Card;
import lucky.utils.dealer.Dealer;
import lucky.utils.player.controllers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The PlayerFactory class is responsible for creating players and initializing
 * their properties.
 * It provides methods to convert string representations of cards and movements
 * into actual card objects.
 * The factory can create different types of players based on the player type
 * specified.
 */
public class PlayerFactory {
    // constants
    private static final String HUMAN = "human";
    private static final String RANDOM = "random";
    private static final String BASIC = "basic";
    private static final String CLEVER = "clever";

    // attributes
    Dealer dealer;
    List<Card> sharedCards;
    List<List<Card>> initCards;
    List<List<List<Card>>> autoMovements;

    /**
     * Constructs a new PlayerFactory object with the given properties.
     * 
     * @param strInitCards      The string-based initial cards for each player.
     * @param strSharedCards    The string-based shared cards.
     * @param strAutoMovements  The string-based auto movements for each player.
     */
    public PlayerFactory(List<List<String>> strInitCards, List<String> strSharedCards,
            List<List<List<String>>> strAutoMovements) {
        dealer = new Dealer(false);
        convertAutoMovements(strAutoMovements);
        convertInitCards(strInitCards);
        convertSharedCards(strSharedCards);
    }

    /**
     * Creates a player with the given player type, index, and auto mode.
     * 
     * @param playerType The type of player to create.
     * @param i          The index of the player.
     * @param isAuto     Whether the player is in auto mode.
     * @return The created player.
     */
    public Player createPlayer(String playerType, int i, boolean isAuto) {
        PlayerController controller;
        switch (playerType) {
            case HUMAN -> controller = new Human();
            case RANDOM -> controller = new Random();
            case BASIC -> controller = new Basic();
            case CLEVER -> controller = new Clever(sharedCards);

            default -> {
                System.exit(1);
                return null;
            }
        }
        return new Player(initCards.get(i), isAuto, autoMovements.get(i), controller);
    }

    /**
     * Converts the given string-based auto movements into a list of card movements.
     *
     * @param strAutoMovements The string-based auto movements to convert.
     */
    private void convertAutoMovements(List<List<List<String>>> strAutoMovements) {
        autoMovements = new ArrayList<>();
        for (List<List<String>> movement : strAutoMovements) {
            // Skip if all inner lists are empty
            List<List<Card>> cardMovement = new ArrayList<>();
            for (List<String> cardStrings : movement) {
                if (!cardStrings.get(0).isEmpty()) {
                    List<Card> cards = new ArrayList<>();
                    for (String cardString : cardStrings) {
                        cards.add(dealer.getCard(cardString, false));
                    }
                    cardMovement.add(cards);
                }
            }
            autoMovements.add(cardMovement);
        }
    }

    /**
     * Converts a list of string-based initial cards into a list of Card objects.
     * Each string in the input list represents a card, and the method uses the
     * dealer to convert the string into a Card object. The converted cards are then
     * added to the initCards list.
     *
     * @param strInitCards The list of string-based initial cards to convert.
     */
    private void convertInitCards(List<List<String>> strInitCards) {
        initCards = new ArrayList<>();
        for (List<String> cardStrings : strInitCards) {
            List<Card> cards = new ArrayList<>();
            if (!cardStrings.get(0).isEmpty()) {
                for (String cardString : cardStrings) {
                    cards.add(dealer.getCard(cardString, true));
                }
            }
            initCards.add(cards);
        }
    }

    /**
     * Converts a list of string representations of cards into a list of actual Card
     * objects for the shared cards.
     * If the input list is not empty, it converts each string into a Card object
     * using the dealer's getCard method. If the input list is empty, it deals out
     * two random cards to the shared cards.
     *
     * @param strSharedCards the list of string representations of cards
     */
    private void convertSharedCards(List<String> strSharedCards) {
        sharedCards = new ArrayList<>();
        if (!(strSharedCards.isEmpty())) {
            for (String cardString : strSharedCards) {
                sharedCards.add(dealer.getCard(cardString, true));
            }
        }
        // else deal out two cards to shared cards
        else {
            for (int i = 0; i < 2; i++) {
                sharedCards.add(dealer.getRandomCard(true));
            }
        }
    }

    // getters

    public Dealer getDealer() {
        return dealer;
    }

    public List<Card> getSharedCards() {
        return sharedCards;
    }

}
