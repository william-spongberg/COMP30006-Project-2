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
    // factory for player + playercontroller + hand

    Dealer dealer;
    List<Card> sharedCards;
    List<List<Card>> initCards;
    List<List<List<Card>>> autoMovements;

    public PlayerFactory(List<List<String>> strInitCards, List<String> strSharedCards,
            List<List<List<String>>> strAutoMovements) {
        dealer = new Dealer(false);
        convertAutoMovements(strAutoMovements);
        convertInitCards(strInitCards);
        convertSharedCards(strSharedCards);
    }

    public Player createPlayer(String playerType, int i, boolean isAuto) {
        PlayerController controller;
        switch (playerType) {
            case "human" -> controller = new Human();
            case "random" -> controller = new Random();
            case "basic" -> controller = new Basic();
            case "clever" -> controller = new Clever(sharedCards);

            default -> {
                System.exit(1);
                return null;
            }
        }
        return new Player(initCards.get(i), isAuto, autoMovements.get(i), controller);
    }

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
        System.out.println("Auto movements: " + autoMovements);
    }

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
        System.out.println("Initial cards: " + initCards);
    }

    private void convertSharedCards(List<String> strSharedCards) {
        sharedCards = new ArrayList<>();
        if (!(strSharedCards.isEmpty())) {
            for (String cardString : strSharedCards) {
                sharedCards.add(dealer.getCard(cardString, true));
            }
        }
        // else deal out two cards to shared cards
        else {
            System.out.println("No shared cards given, dealing out two random cards");
            for (int i = 0; i < 2; i++) {
                sharedCards.add(dealer.getRandomCard(true));
            }
            System.out.println("Shared cards after random: " + sharedCards);
        }
    }

    public Dealer getDealer() {
        return dealer;
    }

    public List<Card> getSharedCards() {
        return sharedCards;
    }

}
