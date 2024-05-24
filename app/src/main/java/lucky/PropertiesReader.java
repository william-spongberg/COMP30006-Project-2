/**
 * PropertiesReader.java
 * 
 * This class is used to read properties files.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Arrays;

public class PropertiesReader {
    // constants
    private static final String IS_AUTO = "isAuto";
    private static final String THINKING_TIME = "thinkingTime";
    private static final String DELAY_TIME = "delayTime";
    private static final String PLAYERS = "players";
    private static final String SHARED = "shared";
    private static final String INITIAL_CARDS = "initialcards";
    private static final String CARDS_PLAYED = "cardsPlayed";
    private static final int MAX_PLAYERS = LuckyThirdteen.NUM_PLAYERS;
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

    /**
     * Constructs a new PropertiesReader object with the given properties.
     * This constructor initializes the properties for auto mode, thinking time,
     * delay time, player types, initial shared cards, initial player hands, and
     * auto movements.
     *
     * @param properties the properties object containing the configuration values
     */
    public PropertiesReader(Properties properties) {
        setAuto(properties);
        setThinkingTime(properties);
        setDelayTime(properties);
        setPlayerTypes(properties);
        setInitialSharedCards(properties);
        setInitPlayerHands(properties);
        setAutoMovements(properties);
    }

    // *DEBUG* print properties
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

    /**
     * Sets the player types based on the properties provided.
     * Each player type is retrieved from the properties using the key format
     * "players.i", where i is the index of the player.
     * If a player type is found, it is added to the list of player types and the
     * number of players is incremented.
     *
     * @param properties the properties containing the player types
     */
    private void setPlayerTypes(Properties properties) {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            String playerType = properties.getProperty(PLAYERS + "." + i);
            if (playerType != null) {
                addPlayerType(playerType);
                incrementNumPlayers();
            }
        }
    }

    /**
     * Sets the initial shared cards based on the provided properties.
     *
     * @param properties the properties containing the initial shared cards
     */
    public void setInitialSharedCards(Properties properties) {
        String initialCards = properties.getProperty(SHARED + "." + INITIAL_CARDS);
        if (initialCards != null) {
            this.strInitSharedCards = Arrays.asList(properties.getProperty(SHARED + "." + INITIAL_CARDS).split(","));
        }
    }

    /**
     * Sets the initial hands of the players based on the provided properties.
     *
     * @param properties the properties containing the initial cards for each player
     */
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

    /**
     * Sets the auto movements for the players based on the provided properties.
     * Each player's auto movements are stored in the strPlayerAutoMovements list.
     * If a player does not have any auto movements, an empty list is added for that player.
     *
     * @param properties the properties containing the auto movements for each player
     */
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

    // getters

    public boolean isAuto() {
        return isAuto;
    }

    public int getThinkingTime() {
        return thinkingTime;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public List<String> getPlayerTypes() {
        return playerTypes;
    }

    public List<String> getStrInitSharedCards() {
        return strInitSharedCards;
    }

    public List<List<String>> getStrInitPlayerHands() {
        return strInitPlayerHands;
    }

    public List<List<List<String>>> getStrPlayerAutoMovements() {
        return strPlayerAutoMovements;
    }

    // setters

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public void setAuto(Properties properties) {
        if (properties.getProperty(IS_AUTO) != null) {
            this.isAuto = Boolean.parseBoolean(properties.getProperty(IS_AUTO));
        }
    }

    public void setThinkingTime(int thinkingTime) {
        this.thinkingTime = thinkingTime;
    }

    public void setThinkingTime(Properties properties) {
        if (properties.getProperty(THINKING_TIME) != null) {
            this.thinkingTime = Integer.parseInt(properties.getProperty(THINKING_TIME));
        }
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setDelayTime(Properties properties) {
        if (properties.getProperty(DELAY_TIME) != null) {
            this.delayTime = Integer.parseInt(properties.getProperty(DELAY_TIME));
        }
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void incrementNumPlayers() {
        this.numPlayers++;
    }

    public void setPlayerTypes(List<String> playerTypes) {
        this.playerTypes = playerTypes;
    }

    public void addPlayerType(String playerType) {
        this.playerTypes.add(playerType);
    }

    public void setStrPlayerAutoMovements(List<List<List<String>>> playerAutoMovements) {
        this.strPlayerAutoMovements = playerAutoMovements;
    }
}
