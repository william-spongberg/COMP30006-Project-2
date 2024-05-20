package game._player;

import game.Dealer;

import ch.aplu.jcardgame.Card;
import game._player._controllers.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerFactory {
    // factory for player + playercontroller + hand
    // return? can return player? or only player info?

    Dealer dealer;
    List<Card> sharedCards;
    List<List<Card>> initCards;
    List<List<List<Card>>> autoMovements;

    public PlayerFactory(List<List<String>> strInitCards, List<String> strSharedCards, 
            List<List<List<String>>> strAutoMovements) {
        dealer = new Dealer();
        convertAutoMovements(strAutoMovements);
        convertInitCards(strInitCards);
        convertSharedCards(strSharedCards);
    }

    public Player createPlayer(String playerType, int i, boolean isAuto) {
        // kinda crappy to use i to keep track but eh
        // java lists not linked lists, otherwise would use next()
        // TODO: change in future
        PlayerController controller;
        boolean mouseControlled = false;
        switch (playerType) {
            case "human" -> {
                controller = new Human();
                mouseControlled = true;
            }
            case "random" -> controller = new Random();
            case "basic" -> controller = new Basic();
            case "clever" -> controller = new Clever(sharedCards);

            // should this be throwing an error instead?
            default -> {
                System.exit(1);
                return null;
            }
        }
        return new Player(initCards.get(i), sharedCards, isAuto, autoMovements.get(i), controller, mouseControlled);
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
        // System.out.println("Auto movements size: " + autoMovements.size());
    }

    private void convertInitCards(List<List<String>> strInitCards) {
        initCards = new ArrayList<>();
        for (List<String> cardStrings : strInitCards) {
            List<Card> cards = new ArrayList<>();
            if (!cardStrings.get(0).isEmpty()) {
                //System.out.println("cardStrings: " + cardStrings);
                for (String cardString : cardStrings) {
                    cards.add(dealer.getCard(cardString, true));
                }
            }
            initCards.add(cards);
        }
        System.out.println("Initial cards: " + initCards);
        // System.out.println("Init cards size: " + initCards.size());
    }

    private void convertSharedCards(List<String> strSharedCards) {
        sharedCards = new ArrayList<>();
        if (!(strSharedCards.isEmpty())) {
            for (String cardString : strSharedCards) {
                sharedCards.add(dealer.getCard(cardString, true));
            }
        }
        //else deal out two cards to shared cards
        else {
            System.out.println("No shared cards given, dealing out two random cards");
            for (int i = 0; i < 2; i++) {
                sharedCards.add(dealer.getRandomCard(true));
            }
            System.out.println("Shared cards after random: " + sharedCards);
        }
    }

    // public void setSharedCards(List<String> strInitSharedCards) {
    // convertSharedCards(strInitSharedCards);
    // }

    public Dealer getDealer() {
        return dealer;
    }

    public List<List<Card>> getInitCards() {
        return initCards;
    }

    public List<Card> getSharedCards() {
        return sharedCards;
    }

    public List<List<List<Card>>> getAutoMovements() {
        return autoMovements;
    }

    public void setSharedCards(List<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }
}
