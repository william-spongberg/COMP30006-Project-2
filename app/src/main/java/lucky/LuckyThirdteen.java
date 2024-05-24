/**
 * LuckyThirdteen.java
 * <p>
 * This is the main class used to house the game logic.
 * It contains many methods. The main ones are used to initialise the game, play the game, and end the game.
 * these are called runApp, initGame, playGame, and endGame.
 * 
 * @version 2.0
 * @see CardGame
 * @see JGameGrid
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import lucky.utils.dealer.Dealer;
import lucky.utils.dealer.DiscardPile;
import lucky.utils.observer.LoggerObserver;
import lucky.utils.player.Player;
import lucky.utils.player.PlayerFactory;
import lucky.utils.state.StateContext;
import lucky.utils.state.StateData;
import lucky.utils.state.States;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import static lucky.utils.observer.Logger.logResult;
import static lucky.utils.scorer.Scorer.getScores;
import static lucky.utils.scorer.Scorer.winner;

public class LuckyThirdteen extends CardGame {
    // --------------------------- VARIABLE DECLARATIONS ---------------------------
    // finals
    public static final int SEED = 30008;
    public static final Random RANDOM = new Random(SEED);
    public static final int NUM_PLAYERS = 4;

    private static final Location[] HAND_LOCATIONS = { new Location(350, 625), new Location(75, 350),
            new Location(350, 75), new Location(625, 350) };
    private static final Location[] SCORE_LOCATIONS = { new Location(25, 575), new Location(25, 25),
            new Location(575, 25), new Location(575, 575), };
    private static final Location TRICK_LOCATION = new Location(350, 350);
    private static final Location TEXT_LOCATION = new Location(350, 450);

    private static final String TITLE_A = "LuckyThirteen (V";
    private static final String TITLE_B = ") William Spongberg, Joshua Linehan, Ethan Hawkins for UofM SWEN30006";
    private static final String INITIALISING = "Initialising...";
    private static final String PLAYING_GAME = "Playing Game...";
    private static final String GAME_OVER_ONE_WINNER = "Game over. Winner is player: ";
    private static final String GAME_OVER_DRAW = "Game Over. Drawn winners are players: ";
    private static final String GAME_OVER_GIF = "sprites/gameover.gif";

    private static final String FONT_NAME = "Arial";
    private static final int FONT_STYLE = Font.BOLD;
    private static final int FONT_SIZE = 36;
    private static final Font BIG_FONT = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);

    private static final int MAX_ROUNDS = 4;
    private static final int HAND_WIDTH = 400;
    private static final int TRICK_WIDTH = 40;
    private static final int FIRST_PLAYER = 0;
    private static final int FIRST_ROUND = 1;
    private static final int ONE_WINNER = 1;
    private static final int NUM_PRIVATE_CARDS = 2;
    private static final double NINETY_DEGREES = 90.0;

    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 700;
    private static final int WINDOW_STATUS_HEIGHT = 30;
    private static final double VERSION = 2.0;

    private final Actor[] scoreActors = { null, null, null, null };
    private final Properties properties;

    // state initialisation (initialised in initgame)
    private final StateContext state = new StateContext();
    private final LoggerObserver loggerObserver = new LoggerObserver();

    // attributes
    private Player[] players;
    private int thinkingTime;
    private int delayTime;
    private boolean isAuto;
    private List<String> playerTypes = new ArrayList<>();
    private List<Integer> winners = new ArrayList<>();
    private int[] scores = new int[NUM_PLAYERS];

    // classes
    private PropertiesReader pReader;
    private PlayerFactory pFactory;
    private Dealer dealer;
    private Hand publicCards;
    private StateData stateData;
    private DiscardPile discardPile = DiscardPile.getInstance();

    // --------------------------- CONSTRUCTOR ---------------------------
    public LuckyThirdteen(Properties properties) {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_STATUS_HEIGHT);
        this.properties = properties;
    }

    // --------------------------- MAIN FUNCTION ---------------------------

    /**
     * Runs the application and returns the log result as a string.
     *
     * @return The result of the logs as a string.
     */
    public String runApp() {
        // initialise the application
        initApp();

        // run the game
        runGame();

        // log the end of the game
        logEndOfGame();

        return logResult.toString();
    }

    /**
     * Initialises the application by setting up the game window text and
     * initialising the game.
     */
    private void initApp() {
        // set up game window text
        setTitle(TITLE_A + String.valueOf(VERSION) + TITLE_B);
        setStatusText(INITIALISING);

        // initialise game
        initScores();
        initGame();
    }

    /**
     * Runs the game by setting the status text to "Playing Game", playing the game,
     * calculating the scores of the players, and displaying the winners.
     */
    private void runGame() {
        // play game
        setStatusText(PLAYING_GAME);
        playGame();

        // display winner(s)
        scores = getScores(players, publicCards.getCardList());
        displayWinners();
    }

    /**
     * Displays the winners of the game.
     * Determines the winners based on the players and the card list in the playing
     * area. If there is only one winner, the game over message will display the
     * winner's name. If there are multiple winners, the game over message will
     * display all the winners' names separated by commas.
     */
    private void displayWinners() {
        winners = winner(players, publicCards.getCardList());

        String winText;
        if (winners.size() == ONE_WINNER) {
            winText = GAME_OVER_ONE_WINNER + winners.iterator().next();
        } else {
            winText = GAME_OVER_DRAW + winners.stream().map(String::valueOf).collect(Collectors.joining(", "));
        }
        addActor(new Actor(GAME_OVER_GIF), TEXT_LOCATION);
        setStatusText(winText);
        refresh();
    }

    /**
     * Logs the end of the game and updates the scores of all players.
     */
    private void logEndOfGame() {
        // add end of game to log
        stateData = new StateData(winners, players, publicCards.getCardList());
        state.setCurrentState(States.END_GAME, stateData);
        for (int i = 0; i < players.length; i++) {
            updateScore(i);
        }
    }

    /**
     * Initialises the scores array and creates TextActors to display the scores on
     * the screen.
     * The scores are initially set to 0 for all players.
     */
    private void initScores() {
        // initialise scores to 0
        Arrays.fill(scores, 0);
        for (int i = 0; i < NUM_PLAYERS; i++) {
            String text = "P" + i + "[" + scores[i] + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, BIG_FONT);
            addActor(scoreActors[i], SCORE_LOCATIONS[i]);
        }
    }

    /**
     * Updates the score for the specified player.
     *
     * @param player the player whose score needs to be updated
     */
    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        int displayScore = scores[player];
        String text = "P" + player + "[" + displayScore + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, BIG_FONT);
        addActor(scoreActors[player], SCORE_LOCATIONS[player]);
    }

    // --------------------------- GAME INITIALISATION FUNCTIONS ----------------

    /**
     * Initialises the game by setting up logging, initialising properties, creating
     * players, initialising shared cards, and initialising and drawing player
     * cards.
     */
    private void initGame() {
        // set up logging
        logResult = new StringBuilder();
        state.addObservers(loggerObserver);

        // initialise properties
        initProperties();

        // create players
        createPlayers();

        // initialise shared cards
        initSharedCards();

        // initialise and draw player cards
        initPlayerCards();
    }

    /**
     * Initializes the properties for the LuckyThirdteen game.
     * Reads the properties file and sets the necessary variables.
     */
    private void initProperties() {
        pReader = new PropertiesReader(properties);
        isAuto = pReader.isAuto();
        thinkingTime = pReader.getThinkingTime();
        delayTime = pReader.getDelayTime();
        playerTypes = pReader.getPlayerTypes();
    }

    /**
     * Creates the players for the game.
     * 
     * This method initializes the players array and the player factory. It creates
     * the dealer using the player factory and then creates each player based on the
     * player types and auto movement settings. If a player has no cards, random
     * cards are added to their hand.
     */
    private void createPlayers() {
        // create players
        players = new Player[NUM_PLAYERS];
        pFactory = new PlayerFactory(pReader.getStrInitPlayerHands(), pReader.getStrInitSharedCards(),
                pReader.getStrPlayerAutoMovements());

        // dealer created by player factory
        dealer = pFactory.getDealer();
        for (int i = 0; i < NUM_PLAYERS; i++) {
            players[i] = pFactory.createPlayer(playerTypes.get(i), i, isAuto);
            if (players[i].getCards().isEmpty()) {
                // add random cards to player
                for (int j = 0; j < NUM_PRIVATE_CARDS; j++) {
                    players[i].addCard(dealer.getRandomCard(true));
                }
            }
        }
    }

    /**
     * Initializes the shared cards in the playing area.
     * This method creates a new hand for the playing area, inserts the shared cards
     * into it, and then draws the playing area on the screen.
     */
    private void initSharedCards() {
        // initialise shared cards
        publicCards = new Hand(Dealer.INITIAL_DECK);
        for (Card card : pFactory.getSharedCards()) {
            publicCards.insert(card, false);
        }
        publicCards.setView(this, new RowLayout(TRICK_LOCATION, (publicCards.getNumberOfCards() + 2) * TRICK_WIDTH));
        publicCards.draw();
    }

    /**
     * Initialises the player cards.
     * This method initialises and draws the player cards on the game board.
     * It creates row layouts for each player, sets rotation angles, and renders the
     * cards.
     */
    private void initPlayerCards() {
        // initialise + draw player cards
        RowLayout[] layouts = new RowLayout[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            layouts[i] = new RowLayout(HAND_LOCATIONS[i], HAND_WIDTH);
            layouts[i].setRotationAngle(NINETY_DEGREES * i);
            players[i].setView(this, layouts[i]);
            players[i].setTargetArea(new TargetArea(TRICK_LOCATION));
            players[i].hideCards();
            players[i].renderCards();
        }
    }

    // --------------------------- GAME FUNCTIONS ---------------------------

    /**
     * This method represents the main game loop where the players take turns and
     * the rounds progress.
     * It initialises the game state, starts the game loop, and updates the state
     * after each round.
     */
    private void playGame() {
        int currPlayer = FIRST_PLAYER;
        int roundNumber = FIRST_ROUND;

        // call add round info to log
        stateData = new StateData(roundNumber);
        state.setCurrentState(States.START_GAME, stateData);

        // start game loop
        while (roundNumber <= MAX_ROUNDS) {
            playerTurn(currPlayer);
            logTurn(currPlayer);
            currPlayer = nextPlayer(currPlayer);
            if (currPlayer == FIRST_PLAYER) {
                roundNumber++;
                endOfRound(roundNumber);
            }
            // delay before next round
            delay(delayTime);
        }
    }

    /**
     * Executes a player's turn in the game.
     *
     * This method displays the player's hand, draws a requested card from the deck
     * (or a random card if the player does not request one), adds the drawn card to
     * the player's hand, discards a card from the player's hand, adds the discarded
     * card to the discard pile, and finally hides the player's hand.
     *
     * @param currPlayer the index of the current player
     */
    private void playerTurn(int currPlayer) {
        // show player's hand
        players[currPlayer].showCards();
        players[currPlayer].renderCards();

        // draw card
        Card drawnCard = players[currPlayer].drawCard();
        if (drawnCard == null) {
            drawnCard = dealer.getRandomCard(false);
        }
        players[currPlayer].addCard(dealer.getCard(drawnCard, true));
        players[currPlayer].renderCards();

        // show drawn card
        if (drawnCard != null) {
            delay(thinkingTime);
        }
        players[currPlayer].renderCards();

        // discard card
        Card discardCard = players[currPlayer].discardCard();
        discardPile.addDiscardCard(discardCard);
        players[currPlayer].removeCard(discardCard);
        players[currPlayer].renderCards();

        delay(delayTime);

        // hide player hand
        players[currPlayer].hideCards();
        players[currPlayer].renderCards();
    }

    /**
     * Logs the end of a player's turn.
     *
     * @param currPlayer the current player's index
     */
    private void logTurn(int currPlayer) {
        // log end of turn
        stateData = new StateData(currPlayer, players[currPlayer].getCards());
        state.setCurrentState(States.END_TURN, stateData);
    }

    /**
     * Calculates the next player's turn.
     *
     * @param currPlayer The current player's index.
     * @return The index of the next player.
     */
    private int nextPlayer(int currPlayer) {
        // next player's turn
        return (currPlayer + 1) % NUM_PLAYERS;
    }

    /**
     * Ends the current round and performs necessary actions.
     *
     * @param roundNumber the number of the current round
     */
    private void endOfRound(int roundNumber) {
        // call addEndOfRoundToLog
        stateData = new StateData(players, publicCards.getCardList());
        state.setCurrentState(States.END_ROUND, stateData);

        // if more rounds, log the round information
        if (roundNumber <= MAX_ROUNDS) {
            stateData = new StateData(roundNumber);
            state.setCurrentState(States.START_ROUND, stateData);
        }
        scores = getScores(players, publicCards.getCardList());
    }

}
