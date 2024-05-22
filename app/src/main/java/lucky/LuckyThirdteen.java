/**
 * LuckyThirdteen.java
 * <p>
 * This is the main class used to house the game logic.
 * It contains many methods. The main ones are used to initialise the game, play the game, and end the game.
 * these are called runApp, initGame, playGame, and endGame.
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
    private static final int MAX_ROUNDS = 4;
    private static final int HAND_WIDTH = 400;
    private static final int TRICK_WIDTH = 40;
    private static final int FIRST_PLAYER = 0;
    private static final int FIRST_ROUND = 1;
    private static final int ONE_WINNER = 1;
    private static final int NUM_PRIVATE_CARDS = 2;
    private static final Location[] HAND_LOCATIONS = {new Location(350, 625), new Location(75, 350), new Location(350, 75), new Location(625, 350)};
    private static final Location[] SCORE_LOCATIONS = {new Location(25, 575), new Location(25, 25), new Location(575, 25), new Location(575, 575),};
    private static final Location TRICK_LOCATION = new Location(350, 350);
    private static final Location TEXT_LOCATION = new Location(350, 450);
    private final Actor[] scoreActors = {null, null, null, null};
    private final Properties properties;
    // state initialisation (initialised in initgame)
    private final StateContext state = new StateContext();
    private final LoggerObserver loggerObserver = new LoggerObserver();
    // primitives
    // structures
    private Player[] players;
    private final Font bigFont = new Font("Arial", Font.BOLD, 36);
    private int thinkingTime = 2000;
    private int delayTime = 600;
    private boolean isAuto;
    private int[] scores = new int[NUM_PLAYERS];
    // classes
    private Dealer dealer;
    private Hand playingArea;
    private StateData stateData;
    private DiscardPile discardPile = DiscardPile.getInstance();

    // --------------------------- CONSTRUCTOR ---------------------------
    public LuckyThirdteen(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "50"));
    }

    // --------------------------- INITIALISATION FUNCTIONS ---------------------------

    public String runApp() {
        String version = "1.0";
        setTitle("LuckyThirteen (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initialising...");
        logResult = new StringBuilder();
        initScores();

        initGame();
        playGame();

        scores = getScores(players, playingArea.getCardList());

        final List<Integer> winners = winner(players, playingArea.getCardList());
        String winText;
        if (winners.size() == ONE_WINNER) {
            winText = "Game over. Winner is player: " + winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " + winners.stream().map(String::valueOf).collect(Collectors.joining(", "));
        }
        addActor(new Actor("sprites/gameover.gif"), TEXT_LOCATION);
        setStatusText(winText);
        refresh();

        // call adding end of game to log
        stateData = new StateData(winners, players, playingArea.getCardList());
        state.setCurrentState(States.END_GAME, stateData);
        for (int i = 0; i < players.length; i++) {
            updateScore(i);
        }

        // TODO: DISCUSS IF THIS IS BAD
        return logResult.toString();
    }

    private void initScores() {
        Arrays.fill(scores, 0);
        for (int i = 0; i < NUM_PLAYERS; i++) {
            String text = "P" + i + "[" + scores[i] + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], SCORE_LOCATIONS[i]);
        }
    }

    private void initGame() {
        // FIXME: each player should contain hand and score -- ?

        System.out.println("initialising game");

        // ADDED - SETUP OF OBSERVER
        state.addObservers(loggerObserver);

        // read properties file
        PropertiesReader pReader = new PropertiesReader(properties);
        isAuto = pReader.isAuto();
        thinkingTime = pReader.getThinkingTime();
        delayTime = pReader.getDelayTime();
        List<String> playerTypes = pReader.getPlayerTypes();
        // print for debugging
        pReader.printProperties();

        // create players
        players = new Player[NUM_PLAYERS];
        PlayerFactory pFactory = new PlayerFactory(pReader.getStrInitPlayerHands(), pReader.getStrInitSharedCards(), pReader.getStrPlayerAutoMovements());
        // dealer created by player factory
        dealer = pFactory.getDealer();

        for (int i = 0; i < NUM_PLAYERS; i++) {
            players[i] = pFactory.createPlayer(playerTypes.get(i), i, isAuto);
            if (players[i].getCards().isEmpty()) {
                System.err.println("Player " + i + " has no starting cards");
                for (int j = 0; j < NUM_PRIVATE_CARDS; j++) {
                    players[i].addCard(dealer.getRandomCard(true));
                }
                System.out.println("Init cards after random: " + players[i].getCards());
            }
        }

        // lucky.UI stuff //
        // init shared cards
        playingArea = new Hand(Dealer.BASE_DECK);
        for (Card card : pFactory.getSharedCards()) {
            playingArea.insert(card, false);
        }
        // draw shared
        playingArea.setView(this, new RowLayout(TRICK_LOCATION, (playingArea.getNumberOfCards() + 2) * TRICK_WIDTH));
        playingArea.draw();
        // init + draw player cards
        RowLayout[] layouts = new RowLayout[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            layouts[i] = new RowLayout(HAND_LOCATIONS[i], HAND_WIDTH);
            layouts[i].setRotationAngle(90.0 * i);
            players[i].setView(this, layouts[i]);
            players[i].setTargetArea(new TargetArea(TRICK_LOCATION));
            players[i].hideCards();
            players[i].renderCards();
        }
    }

    // --------------------------- DURING GAME FUNCTIONS ---------------------------

    private void playGame() {
        int currPlayer = FIRST_PLAYER;
        Card drawnCard;
        Card discardCard;
        int roundNumber = FIRST_ROUND;

        // call add round info to log
        stateData = new StateData(roundNumber);
        state.setCurrentState(States.START_GAME, stateData);

        // start game loop
        while (roundNumber <= MAX_ROUNDS) {
            // DEBUG //
            // check player's cards are not in dealer's deck
            Hand tmpHand = dealer.getPack();
            for (int i = 0; i < NUM_PLAYERS; i++) {
                for (Card card : players[i].getCards()) {
                    for (Card deckCard : tmpHand.getCardList()) {
                        if (card.equals(deckCard)) {
                            System.err.println("Player " + i + " has card in dealer's deck");
                        }
                    }
                }
            }

            // show player's hand
            players[currPlayer].showCards();
            players[currPlayer].renderCards();

            // draw card
            drawnCard = players[currPlayer].drawCard();
            if (drawnCard == null) {
                System.out.println("Player " + currPlayer + " did not request a specific card to be drawn");
                drawnCard = dealer.getRandomCard(false);
            }
            players[currPlayer].addCard(dealer.getCard(drawnCard, true));

            System.out.println("Player " + currPlayer + " drew card: " + drawnCard);

            for (Card card : players[currPlayer].getCards()) {
                System.out.println("Player " + currPlayer + " has card: " + card);
            }

            players[currPlayer].renderCards();

            // log stuff //
            if (drawnCard != null) {
                // add card to the list of cards played
                delay(delayTime);
            }

            players[currPlayer].renderCards();

            discardCard = players[currPlayer].discardCard();
            
            System.out.println("Player " + currPlayer + " discarded " + discardCard);

            // for visibility to clever, add to discardPile.
            discardPile.addDiscardCard(discardCard);

            players[currPlayer].removeCard(discardCard);
            players[currPlayer].renderCards();

            delay(delayTime);

            // hide player's hand
            players[currPlayer].hideCards();
            players[currPlayer].renderCards();

            // log end of turn
            stateData = new StateData(currPlayer, players[currPlayer].getCards());
            state.setCurrentState(States.END_TURN, stateData);

            // next player's turn
            currPlayer = (currPlayer + 1) % NUM_PLAYERS;

            // if the next player is player 0, increment the round number and log the end of
            // round scores
            if (currPlayer == FIRST_PLAYER) {
                roundNumber++;

                // call addEndOfRoundToLog
                stateData = new StateData(players, playingArea.getCardList());
                state.setCurrentState(States.END_ROUND, stateData);

                // if more rounds, log the round information
                if (roundNumber <= MAX_ROUNDS) {
                    stateData = new StateData(roundNumber);
                    state.setCurrentState(States.START_ROUND, stateData);
                }
                scores = getScores(players, playingArea.getCardList());
            }

            // delay before next round
            delay(delayTime);
        }
    }

    // --------------------------- GETTERS & SETTERS & UPDATERS --------------------

    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        int displayScore = scores[player];
        String text = "P" + player + "[" + displayScore + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], SCORE_LOCATIONS[player]);
    }

    // --------------------------- END GAME FUNCTIONS ---------------------------

}
