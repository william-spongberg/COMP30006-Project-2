// LuckyThirteen.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.stream.Collectors;

import _card.Rank;
import _card.Suit;

@SuppressWarnings("serial")
public class LuckyThirdteen extends CardGame {
    // TODO: move values put here to static finals in info expert?
    // TODO: move attributes into respective classes?

    private static final int MAX_ROUNDS = 4;

    private static final int THIRTEEN_GOAL = 13;

    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            // FIXME: why is this location not used?
            // new Location(650, 575)
            new Location(575, 575)
    };

    private Actor[] scoreActors = { null, null, null, null };    

    private int thinkingTime = 2000;
    private int delayTime = 600;

    private Hand[] hands;

    private int[] scores = new int[nbPlayers];

    private int[] autoIndexHands = new int[nbPlayers];
    private boolean isAuto = false;

    private Hand playingArea;
    private Hand pack;

    Font bigFont = new Font("Arial", Font.BOLD, 36);
    
    private Card selected;

    // FIXME: never used, remove?
    public boolean rankGreater(Card card1, Card card2) {
        // Warning: Reverse rank order of cards (see comment on enum)
        
        return card1.getRankId() < card2.getRankId();
    }
    
    // TODO: update but keep this here? - Ethan
    public void setStatus(String string) {
        setStatusText(string);
    }

    Dealer dealer = new Dealer();

    private void initGame() {
        // FIXME: each player should contain hand and score?

        dealer.dealingOut(hands, nbPlayers, nbStartCards, nbFaceUpCards);

        hands = new Hand[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(deck);
        }

        playingArea = new Hand(deck);

        dealingOut(hands, nbPlayers, nbStartCards, nbFaceUpCards);

        playingArea.setView(this, new RowLayout(trickLocation, (playingArea.getNumberOfCards() + 2) * trickWidth));
        playingArea.draw();

        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, false);
        }

        // Set up human player for interaction
        // FIXME: move into Human class as child of Player class
        CardListener cardListener = new CardAdapter() // Human Player plays card
        {
            @Override
            public void leftDoubleClicked(Card card) {
                selected = card;
                hands[0].setTouchEnabled(false);
            }
        };
        // FIXME: player 0 is always human player, even if auto is true
        hands[0].addCardListener(cardListener);

        // graphics
        // TODO: move to new graphics class?
        
    }

    private void playGame() {
        // initialize winner + round number
        // FIXME: winner not used
        int winner = 0;
        int roundNumber = 1;

        // update the score for each player
        // FIXME: necessary to update score here? should already be initialised
        for (int i = 0; i < nbPlayers; i++)
            updateScore(i);

        // initialize list of cards played
        List<Card> cardsPlayed = new ArrayList<>();

        // log initial round number
        addRoundInfoToLog(roundNumber);

        // initialize next player
        int nextPlayer = 0;

        // start game loop
        while (roundNumber <= MAX_ROUNDS) {
            selected = null;
            boolean finishedAuto = false;

            // if game is set to auto
            if (isAuto) {
                // get next player's auto index and movements
                int nextPlayerAutoIndex = autoIndexHands[nextPlayer];
                List<String> nextPlayerMovement = playerAutoMovements.get(nextPlayer);
                String nextMovement = "";

                // if there are more movements
                if (nextPlayerMovement.size() > nextPlayerAutoIndex) {
                    // get next movement and increment the auto index
                    nextMovement = nextPlayerMovement.get(nextPlayerAutoIndex);
                    nextPlayerAutoIndex++;

                    // update the auto index for the player
                    autoIndexHands[nextPlayer] = nextPlayerAutoIndex;
                    Hand nextHand = hands[nextPlayer];

                    // apply player movement
                    selected = applyAutoMovement(nextHand, nextMovement);
                    delay(delayTime);

                    // if card was selected, remove from hand
                    if (selected != null) {
                        selected.removeFromHand(true);
                    } else {
                        // if no card was selected, get random card and remove from hand
                        // (default behaviour)
                        // TODO: move random card selection to Bot Random class
                        selected = getRandomCard(hands[nextPlayer]);
                        selected.removeFromHand(true);
                    }
                } else {
                    // if no more movements for player, set finishedAuto to true
                    finishedAuto = true;
                }
            }

            // if game is not set to auto or if finishedAuto is true
            if (!isAuto || finishedAuto) {
                // if the next player is player 0
                if (0 == nextPlayer) {
                    // enable touch for player 0
                    hands[0].setTouchEnabled(true);

                    // set the status message and deal a card to player 0
                    setStatus("Player 0 is playing. Please double click on a card to discard");
                    selected = null;
                    dealACardToHand(hands[0]);

                    // wait until a card is selected
                    while (null == selected) {
                        // FIXME: delay here is not necessary
                        delay(delayTime);
                    }

                    // remove selected card from the hand
                    selected.removeFromHand(true);
                } else {
                    // if the next player is not player 0 (human), set the status message
                    setStatusText("Player " + nextPlayer + " thinking...");

                    // get random card and remove it from the hand
                    // FIXME: doing random bot behaviour here, means if human is playing all bots
                    // will be random
                    selected = getRandomCard(hands[nextPlayer]);
                    selected.removeFromHand(true);
                }
            }

            // log cards played by the player
            addCardPlayedToLog(nextPlayer, hands[nextPlayer].getCardList());

            // if card was selected
            if (selected != null) {
                // add card to the list of cards played
                cardsPlayed.add(selected);
                // set face up
                selected.setVerso(false);
                delay(delayTime);
            }

            // next player's turn
            nextPlayer = (nextPlayer + 1) % nbPlayers;

            // if the next player is player 0, increment the round number and log the end of
            // round scores
            if (nextPlayer == 0) {
                roundNumber++;
                addEndOfRoundToLog();

                // if more rounds, log the round information
                // FIXME: 4 should be MAX_ROUNDS
                if (roundNumber <= 4) {
                    addRoundInfoToLog(roundNumber);
                }
            }

            // if game is over, calculate final score
            // FIXME: 4 should be MAX_ROUNDS
            if (roundNumber > 4) {
                calculateScoreEndOfRound();
            }

            // delay before next round
            delay(delayTime);
        }
    }

    public String runApp() {
        setTitle("LuckyThirteen (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initialising...");

        // FIXME: create Score object here - or should it be inside the game?
        initScores();
        initScore();

        // TODO: Scorer created and subscribed to each player?

        setupPlayerAutoMovements();

        initGame();
        playGame();

        for (int i = 0; i < nbPlayers; i++)
            updateScore(i);

        int maxScore = 0;
        for (int i = 0; i < nbPlayers; i++)
            if (scores[i] > maxScore)
                maxScore = scores[i];

        final List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++)
            if (scores[i] == maxScore)
                winners.add(i);

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
        addEndOfGameToLog(winners);

        return logResult.toString();
    }

    public LuckyThirdteen(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "50"));
    }

}
