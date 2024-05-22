/**
 * LuckyThirdteen.java
 * 
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
import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import lucky.utils.dealer.Dealer;
import lucky.utils.dealer.DiscardPile;
import lucky.utils.observer.LoggerObserver;
import lucky.utils.player.Player;
import lucky.utils.player.PlayerFactory;
import lucky.utils.state.StateContext;
import lucky.utils.state.StateData;
import lucky.utils.state.States;

import static lucky.utils.observer.Logger.logResult;
import static lucky.utils.scorer.Scorer.getScores;
import static lucky.utils.scorer.Scorer.winner;

@SuppressWarnings("serial")
public class LuckyThirdteen extends CardGame {
    // TODO: move values put here to static finals in info expert?
    // TODO: move attributes into respective classes?

    // --------------------------- VARIABLE DECLARATIONS ---------------------------
    // finals

    // why do we still have this in two places?
    public static final int seed = 30008;
    public static final Random random = new Random(30008);
    private final int handWidth = 400;
    private final int trickWidth = 40;
    // TODO: increment version per major commit?
    private final String version = "1.0";
    static final int MAX_ROUNDS = 4;
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] scoreLocations = {
            new Location(25, 575),
            new Location(25, 25),
            new Location(575, 25),
            new Location(575, 575),
    };

    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);

    // primitives
    public int nbPlayers = 4;
    private int thinkingTime = 2000;
    private int delayTime = 600;
    private boolean isAuto = false;

    // structures
    List<Card> initSharedCards = new ArrayList<>();
    List<List<Card>> initPlayerHands = new ArrayList<>();
    List<List<List<Card>>> autoPlayerMovements = new ArrayList<>();
    Player[] players;
    private Actor[] scoreActors = { null, null, null, null };
    private int[] scores = new int[nbPlayers];

    private int[] autoIndexHands = new int[nbPlayers];
    private Hand[] hands;

    // classes
    Dealer dealer;
    Card selected;
    private Hand playingArea;
    private Hand pack;
    Font bigFont = new Font("Arial", Font.BOLD, 36);

    private Properties properties;

    // state initialisation (initialised in initgame)
    private StateContext state = new StateContext();
    private LoggerObserver loggerObserver = new LoggerObserver();

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

    // --------------------------- INITIALISATION FUNCTIONS
    // ---------------------------

    public String runApp() {
        setTitle("LuckyThirteen (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initialising...");
        logResult = new StringBuilder();
        // FIXME: create Score object here - or should it be inside the game? - No,
        // score stuff is static
        initScores();
        initScore();

        // Actor[] scoreActors = initScore(nbPlayers, scores, bgColor, bigFont);

        // for (int i = 0; i < nbPlayers; i++)
        // addActor(scoreActors[i], scoreLocations[i]);

        // setupPlayerAutoMovements();

        initGame();
        playGame();

        // what is this actually doing?
        scores = getScores(players, playingArea.getCardList());
        int maxScore = 0;
        for (int i = 0; i < nbPlayers; i++)
            if (scores[i] > maxScore)
                maxScore = scores[i];

        // replace with winners method
        final List<Integer> winners = winner(players, playingArea.getCardList());
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        addActor(new Actor("sprites/gameover.gif"), textLocation);
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

    // TODO: move scoring to new score class?
    private void initScore() {
        for (int i = 0; i < nbPlayers; i++) {
            String text = "P" + i + "[" + String.valueOf(scores[i]) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private void initScores() {
        Arrays.fill(scores, 0);
    }

    private void initGame() {
        // FIXME: each player should contain hand and score -- ?

        System.out.println("initialising game");

        // ADDED - SETUP OF OBSERVER
        state.addObservers(loggerObserver);

        // read properties file
        PropertiesReader pReader = new PropertiesReader(properties);
        nbPlayers = pReader.getNumPlayers();
        isAuto = pReader.isAuto();
        thinkingTime = pReader.getThinkingTime();
        delayTime = pReader.getDelayTime();
        List<String> playerTypes = pReader.getPlayerTypes();
        // print for debugging
        pReader.printProperties();

        // create players
        players = new Player[nbPlayers];
        PlayerFactory pFactory = new PlayerFactory(pReader.getStrInitPlayerHands(), pReader.getStrInitSharedCards(),
                pReader.getStrPlayerAutoMovements());
        // dealer created by player factory
        dealer = pFactory.getDealer();

        for (int i = 0; i < nbPlayers; i++) {
            players[i] = pFactory.createPlayer(playerTypes.get(i), i, isAuto);
            if (players[i].getCards().isEmpty()) {
                System.err.println("Player " + i + " has no starting cards");
                for (int j = 0; j < 2; j++) {
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
        playingArea.setView(this, new RowLayout(trickLocation, (playingArea.getNumberOfCards() + 2) * trickWidth));
        playingArea.draw();
        // init + draw player cards
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90.0 * i);
            players[i].setView(this, layouts[i]);
            players[i].setTargetArea(new TargetArea(trickLocation));
            players[i].hideCards();
            players[i].renderCards();
        }
    }

    // --------------------------- DURING GAME FUNCTIONS ---------------------------

    private void playGame() {
        // int winner = 0;
        int currPlayer = 0;
        Card drawnCard = null;
        Card discardCard = null;
        List<Card> cardsPlayed = new ArrayList<>();
        int roundNumber = 1;

        // call add round info to log
        stateData = new StateData(roundNumber);
        state.setCurrentState(States.START_GAME, stateData);

        // start game loop
        while (roundNumber <= MAX_ROUNDS) {
            // TODO, do I need to update state here?
            // state.setCurrentState(States.START_ROUND);

            // player behaviour: //
            // deal a card to the player
            // player selects a card from hand to discard
            // card is removed from hand
            // add to list of cards played (add to discard pile)

            // DEBUG //
            // check player's cards are not in dealer's deck
            Hand tmpHand = dealer.getPack();
            for (int i = 0; i < nbPlayers; i++) {
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
                cardsPlayed.add(drawnCard);
                delay(delayTime);
            }

            players[currPlayer].renderCards();

            discardCard = players[currPlayer].discardCard();

            if (discardCard == null) {
                System.err.println("Player " + currPlayer + " did not discard a card");
                // TODO: change to players[currPlayer].getRandomCard()?
                discardCard = players[currPlayer].getCards().get(random.nextInt(players[currPlayer].getCards().size()));
            }
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
            currPlayer = (currPlayer + 1) % nbPlayers;

            // if the next player is player 0, increment the round number and log the end of
            // round scores
            if (currPlayer == 0) {
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

            // FIXME: this is never reached?
            // if round is over, calculate round score
            // if (roundNumber > MAX_ROUNDS) {
            // scores = getScores(players, playingArea.getCardList());
            // }

            // delay before next round
            delay(delayTime);
        }
    }

    // TODO: I can't remember what this was used for. Delete?
    // public boolean rankGreater(Card card1, Card card2) {
    // Warning: Reverse rank order of cards (see comment on enum)
    // FIXME: why warning? - because it returns reverse
    // return card1.getRankId() < card2.getRankId();
    // }

    // -------------------------- 13 CHECKING --------------------------------------

    // --------------------------- GETTERS & SETTERS & UPDATERS --------------------

    public void setStatus(String string) {
        setStatusText(string);
    }
    // private Card selected;

    // theres some functionality we're missing I think.
    private void updateScore(int player) {
        // FIXME: why create new actor each time? just change text - cuz theres no
        // update in this right, we have to add and remove everythin
        removeActor(scoreActors[player]);
        // why is max used here? how can the score be negative?
        int displayScore = Math.max(scores[player], 0);
        String text = "P" + player + "[" + String.valueOf(displayScore) + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    // --------------------------- END GAME FUNCTIONS ---------------------------

}
