package game._player;

import game.Dealer;
import game._player._bot.BasicBot;
import game._player._bot.CleverBot;
import game._player._bot.RandomBot;
import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;

public class PlayerFactory {
    // factory for player + playercontroller + hand
    // return? can return player? or only player info?

    // List<String> playerTypes = pReader.getPlayerTypes();
    // List<List<List<String>>> strAutoPlayerMovements = pReader.getPlayerAutoMovements();
    // List<String> strInitSharedCards = pReader.getInitialSharedCards();
    // List<List<String>> strInitPlayerHands = pReader.getInitialPlayerHands();

    Dealer dealer = new Dealer();
    List<Card> sharedCards;
    List<Card> initCards;
    List<List<Card>> autoMovements;

    public Player createPlayer(String playerType, List<String> strInitCards, boolean isAuto, List<List<String>> strAutoMovements) {
        // convert strings to cards
        convertAutoMovements(isAuto, strAutoMovements);
        convertInitCards(strInitCards);

        if (playerType.equals(Human.NAME)) {
            System.out.println("Creating human player");
            return new Human(initCards, sharedCards, isAuto, autoMovements);
        } else if (playerType.equals(RandomBot.NAME)) {
            System.out.println("Creating random bot player");
            return new RandomBot(initCards, sharedCards, isAuto, autoMovements);
        } else if (playerType.equals(BasicBot.NAME)) {
            System.out.println("Creating basic bot player");
            return new BasicBot(initCards, sharedCards, isAuto, autoMovements);
        } else if (playerType.equals(CleverBot.NAME)) {
            System.out.println("Creating clever bot player");
            return new CleverBot(initCards, sharedCards, isAuto, autoMovements);
        } else {
            System.err.println("Invalid player type: " + playerType);
            System.exit(1);
            return null;
        }
    }

    private void convertAutoMovements(boolean isAuto, List<List<String>> strAutoMovements) {
        autoMovements = new ArrayList<>();
        if (isAuto && !(strAutoMovements.isEmpty())) {
            for (List<String> movement : strAutoMovements) {
                List<Card> cardMovement = new ArrayList<>();
                for (String cardString : movement) {
                    // needs to be false - not actually removing card from deck
                    cardMovement.add(dealer.getCard(cardString, false));
                }
                autoMovements.add(cardMovement);
            }
        }
    }

    private void convertInitCards(List<String> strInitCards) {
        initCards = new ArrayList<>();
        if (!strInitCards.isEmpty() && !strInitCards.get(0).isEmpty()) {
            System.out.println(strInitCards);

            for (String cardString : strInitCards) {
                initCards.add(dealer.getCard(cardString, true));
            }
        }
        // else deal out two cards to each player
        else {
            for (int j = 0; j < 2; j++) {
                initCards.add(dealer.getRandomCard(true));
            }
            System.out.println("Init cards after random: " + initCards);
        }
    }

    private void convertSharedCards(List<String> strInitSharedCards) {
        sharedCards = new ArrayList<>();
        if (!(strInitSharedCards.isEmpty())) {
            for (String cardString : strInitSharedCards) {
                sharedCards.add(dealer.getCard(cardString, true));
            }
        }
        // else deal out two cards to shared cards
        else {
            for (int i = 0; i < 2; i++) {
                sharedCards.add(dealer.getRandomCard(true));
            }
            System.out.println("Shared cards after random: " + sharedCards);
        }
    }

    public void setSharedCards(List<String> strInitSharedCards) {
        convertSharedCards(strInitSharedCards);
    }

    public Dealer getDealer() {
        return dealer;
    }
    

    public List<Card> getInitCards() {
        return initCards;
    }

    public List<Card> getSharedCards() {
        return sharedCards;
    }

    public List<List<Card>> getAutoMovements() {
        return autoMovements;
    }
}
