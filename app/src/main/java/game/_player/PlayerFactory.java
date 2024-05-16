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

    Dealer dealer;
    List<Card> sharedCards;
    List<List<Card>> initCards;
    List<List<List<Card>>> autoMovements;

    public PlayerFactory(List<List<String>> strInitCards, List<String> strSharedCards, 
            List<List<List<String>>> strAutoMovements) {
        dealer = new Dealer();
        convertInitCards(strInitCards);
        convertSharedCards(strSharedCards);
        convertAutoMovements(strAutoMovements);
    }

    public Player createPlayer(String playerType, int i, boolean isAuto) {
        // kinda crappy to use i to keep track but eh
        // java lists not linked lists, otherwise would use next()
        // TODO: change in future

        if (playerType.equals(Human.NAME)) {
            System.out.println("Creating human player");
            return new Human(initCards.get(i), sharedCards, isAuto, autoMovements.get(i));
        } else if (playerType.equals(RandomBot.NAME)) {
            System.out.println("Creating random bot player");
            return new RandomBot(initCards.get(i), sharedCards, isAuto, autoMovements.get(i));
        } else if (playerType.equals(BasicBot.NAME)) {
            System.out.println("Creating basic bot player");
            return new BasicBot(initCards.get(i), sharedCards, isAuto, autoMovements.get(i));
        } else if (playerType.equals(CleverBot.NAME)) {
            System.out.println("Creating clever bot player");
            return new CleverBot(initCards.get(i), sharedCards, isAuto, autoMovements.get(i));
        } else {
            System.err.println("Invalid player type: " + playerType);
            System.exit(1);
            return null;
        }
    }
    
    private void convertAutoMovements(List<List<List<String>>> strAutoMovements) {
        autoMovements = new ArrayList<>();
        if (!(strAutoMovements.isEmpty())) {
            for (List<List<String>> movement : strAutoMovements) {
                List<List<Card>> cardMovement = new ArrayList<>();
                for (List<String> cardStrings : movement) {
                    List<Card> cards = new ArrayList<>();
                    for (String cardString : cardStrings) {
                        // needs to be false - not actually removing card from deck
                        cards.add(dealer.getCard(cardString, false));
                    }
                    cardMovement.add(cards);
                }
                autoMovements.add(cardMovement);
            }
        }
    }
    
    private void convertInitCards(List<List<String>> strInitCards) {
        initCards = new ArrayList<>();
        if (!strInitCards.isEmpty() && !strInitCards.get(0).isEmpty()) {
            for (List<String> cardStrings : strInitCards) {
                List<Card> cards = new ArrayList<>();
                for (String cardString : cardStrings) {
                    cards.add(dealer.getCard(cardString, true));
                }
                initCards.add(cards);
            }
        }
        // else deal out two cards to each player
        else {
            for (int j = 0; j < 2; j++) {
                List<Card> cards = new ArrayList<>();
                cards.add(dealer.getRandomCard(true));
                initCards.add(cards);
            }
            System.out.println("Init cards after random: " + initCards);
        }
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
            for (int i = 0; i < 2; i++) {
                sharedCards.add(dealer.getRandomCard(false));
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
