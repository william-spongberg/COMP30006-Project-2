import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Arrays;

public class PropertiesReader {
    // TODO: move static finals to info expert
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

    private String initialSharedCards;
    private List<String> initialPlayerHands = new ArrayList<>();
    private List<List<String>> playerAutoMovements = new ArrayList<>();

    public PropertiesReader(Properties properties) {
        setAuto(Boolean.parseBoolean(properties.getProperty("auto")));
        setThinkingTime(Integer.parseInt(properties.getProperty("thinkingTime")));
        setDelayTime(Integer.parseInt(properties.getProperty("delayTime")));

        setPlayerTypes(properties);

        setInitialSharedCards(properties.getProperty(SHARED + "." + INITIAL_CARDS));
        setInitialPlayerHands(properties);

        setAutoMovements(properties);

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
                playerAutoMovements.add(new ArrayList<>());
            } else {
                List<String> movements = Arrays.asList(playerAutoMovement.split(","));
                playerAutoMovements.add(movements);
            }
        }
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public int getThinkingTime() {
        return thinkingTime;
    }

    public void setThinkingTime(int thinkingTime) {
        this.thinkingTime = thinkingTime;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
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

    public String getInitialSharedCards() {
        return initialSharedCards;
    }

    public void setInitialSharedCards(String initialSharedCards) {
        this.initialSharedCards = initialSharedCards;
    }

    public List<String> getInitialPlayerHands() {
        return initialPlayerHands;
    }

    public void setInitialPlayerHands(List<String> initialPlayerHands) {
        this.initialPlayerHands = initialPlayerHands;
    }

    public void setInitialPlayerHands(Properties properties) {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            addInitialPlayerHand(properties.getProperty(PLAYERS + "." + i + "." + INITIAL_CARDS));
        }
    }

    public void addInitialPlayerHand(String initialPlayerHand) {
        this.initialPlayerHands.add(initialPlayerHand);
    }

    public List<List<String>> getPlayerAutoMovements() {
        return playerAutoMovements;
    }

    public void setPlayerAutoMovements(List<List<String>> playerAutoMovements) {
        this.playerAutoMovements = playerAutoMovements;
    }
}
