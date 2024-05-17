

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Arrays;

public class PropertiesReader {
    // TODO: move static finals to info expert
    private static final String IS_AUTO = "isAuto";
    private static final String THINKING_TIME = "thinkingTime";
    private static final String DELAY_TIME = "delayTime";
    private static final String PLAYERS = "players";
    private static final String SHARED = "shared";
    private static final String INITIAL_CARDS = "initialcards";
    private static final String CARDS_PLAYED = "cardsPlayed";
    private static final int MAX_PLAYERS = 4;
    private static final int DEFAULT_THINKING_TIME = 2000;
    private static final int DEFAULT_DELAY_TIME = 600;

    // attributes
    private boolean isAuto = false;
    private int thinkingTime = DEFAULT_THINKING_TIME;
    private int delayTime = DEFAULT_DELAY_TIME;

    private int numPlayers = 0;
    private List<String> playerTypes = new ArrayList<>();

    private List<String> strInitSharedCards = new ArrayList<>();
    private List<List<String>> strInitPlayerHands = new ArrayList<>();
    private List<List<List<String>>> strPlayerAutoMovements = new ArrayList<>();

    public PropertiesReader(Properties properties) {
        setAuto(properties);
        setThinkingTime(properties);
        setDelayTime(properties);
        setPlayerTypes(properties);
        setInitialSharedCards(properties);
        setInitPlayerHands(properties);
        setAutoMovements(properties);

        // TODO: throw exceptions if certain properties are not set properly
    }

    // print properties for debugging
    public void printProperties() {
        System.out.println("isAuto: " + isAuto);
        System.out.println("thinkingTime: " + thinkingTime);
        System.out.println("delayTime: " + delayTime);
        System.out.println("numPlayers: " + numPlayers);
        System.out.println("playerTypes: " + playerTypes);
        System.out.println("initialSharedCards: " + strInitSharedCards);
        System.out.println("initialPlayerHands: " + strInitPlayerHands);
        System.out.println("playerAutoMovements: " + strPlayerAutoMovements);
    }

    private void setPlayerTypes(Properties properties) {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            String playerType = properties.getProperty(PLAYERS + "." + i);
            if (playerType != null) {
                addPlayerType(playerType);
                incrementNumPlayers();
            }
        }
    }

    public void setAutoMovements(Properties properties) {
        for (int i = 0; i < 4; i++) {
            String playerAutoMovement = properties.getProperty(PLAYERS + "." + i + "." + CARDS_PLAYED);
            if (playerAutoMovement == null) {
                strPlayerAutoMovements.add(new ArrayList<>());
            } else {
                // split up rounds by ","
                List<String> movements = Arrays.asList(playerAutoMovement.split(","));
                // split up movements by "-"
                List<List<String>> playerMovements = new ArrayList<>();
                for (String movement : movements) {
                    playerMovements.add(Arrays.asList(movement.split("-")));
                }
                strPlayerAutoMovements.add(playerMovements);
            }
        }
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public void setAuto(Properties properties) {
        if (properties.getProperty(IS_AUTO) != null) {
            this.isAuto = Boolean.parseBoolean(properties.getProperty(IS_AUTO));
        }
    }

    public int getThinkingTime() {
        return thinkingTime;
    }

    public void setThinkingTime(int thinkingTime) {
        this.thinkingTime = thinkingTime;
    }

    public void setThinkingTime(Properties properties) {
        if (properties.getProperty(THINKING_TIME) != null) {
            this.thinkingTime = Integer.parseInt(properties.getProperty(THINKING_TIME));
        }
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setDelayTime(Properties properties) {
        if (properties.getProperty(DELAY_TIME) != null) {
            this.delayTime = Integer.parseInt(properties.getProperty(DELAY_TIME));
        }
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void incrementNumPlayers() {
        this.numPlayers++;
    }

    public List<String> getPlayerTypes() {
        return playerTypes;
    }

    public void setPlayerTypes(List<String> playerTypes) {
        this.playerTypes = playerTypes;
    }

    public void addPlayerType(String playerType) {
        this.playerTypes.add(playerType);
    }

    public List<String> getStrInitSharedCards() {
        return strInitSharedCards;
    }

    public void setInitialSharedCards(Properties properties) {
        String initialCards = properties.getProperty(SHARED + "." + INITIAL_CARDS);
        if (initialCards != null) {
            this.strInitSharedCards = Arrays.asList(properties.getProperty(SHARED + "." + INITIAL_CARDS).split(","));
        }
    }

    public List<List<String>> getStrInitPlayerHands() {
        return strInitPlayerHands;
    }

    public void setInitPlayerHands(Properties properties) {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            String initialCards = properties.getProperty(PLAYERS + "." + i + "." + INITIAL_CARDS);
            List<String> cardsList = new ArrayList<>();
            if (initialCards != null) {
                cardsList = Arrays.asList(initialCards.split(","));
            }
            strInitPlayerHands.add(cardsList);
        }
    }

    public List<List<List<String>>> getStrPlayerAutoMovements() {
        return strPlayerAutoMovements;
    }

    public void setStrPlayerAutoMovements(List<List<List<String>>> playerAutoMovements) {
        this.strPlayerAutoMovements = playerAutoMovements;
    }
}
