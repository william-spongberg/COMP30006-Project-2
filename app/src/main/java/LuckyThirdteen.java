import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import game.Dealer;
import game._card.Rank;
import game._card.Suit;
import game._player.Player;
import game._player.PlayerFactory;

@SuppressWarnings("serial")
public class LuckyThirdteen extends CardGame {
    // TODO: move values put here to static finals in info expert?
    // TODO: move attributes into respective classes?
    public static final String trumpImage[] = { "bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif" };
    public static final int seed = 30008;
    public static final Random random = new Random(seed);
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();

    public boolean rankGreater(Card card1, Card card2) {
        // Warning: Reverse rank order of cards (see comment on enum)
        // FIXME: why warning?
        return card1.getRankId() < card2.getRankId();
    }

    // TODO: increment version per major commit?
    private final String version = "1.0";

    public int nbPlayers = 4;
    public final int nbStartCards = 2;
    public final int nbFaceUpCards = 2;

    private final int handWidth = 400;
    private final int trickWidth = 40;

    private static final int THIRTEEN_GOAL = 13;

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

    private Actor[] scoreActors = { null, null, null, null };

    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);

    private int thinkingTime = 2000;
    private int delayTime = 600;

    private Hand[] hands;

    public void setStatus(String string) {
        setStatusText(string);
    }

    private int[] scores = new int[nbPlayers];

    private int[] autoIndexHands = new int[nbPlayers];
    private boolean isAuto = false;

    private Hand playingArea;
    private Hand pack;

    Font bigFont = new Font("Arial", Font.BOLD, 36);

    // private Card selected;

    // TODO: move scoring to new score class?
    private void initScore() {
        for (int i = 0; i < nbPlayers; i++) {
            String text = "[" + String.valueOf(scores[i]) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private void initScores() {
        Arrays.fill(scores, 0);
    }

    private void updateScore(int player) {
        // FIXME: why create new actor each time? just change text
        removeActor(scoreActors[player]);
        // why is max used here? how can the score be negative?
        int displayScore = Math.max(scores[player], 0);
        String text = "P" + player + "[" + String.valueOf(displayScore) + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private Actor[] initScore(int nbPlayers, int[] scores, Color bgColor, Font bigFont) {
        for (int i = 0; i < nbPlayers; i++) {
            String text = "[" + String.valueOf(scores[i]) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
        return scoreActors;
    }

    private int getScorePrivateCard(Card card) {
        Rank rank = (Rank) card.getRank();
        Suit suit = (Suit) card.getSuit();

        return rank.getScoreCardValue() * suit.getMultiplicationFactor();
    }

    private int getScorePublicCard(Card card) {
        Rank rank = (Rank) card.getRank();
        return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
    }

    private int calculateMaxScoreForThirteenPlayer(int playerIndex) {// , Hand[] hands, Hand playingArea) {
        List<Card> privateCards = hands[playerIndex].getCardList();
        List<Card> publicCards = playingArea.getCardList();
        Card privateCard1 = privateCards.get(0);
        Card privateCard2 = privateCards.get(1);
        Card publicCard1 = publicCards.get(0);
        Card publicCard2 = publicCards.get(1);

        int maxScore = 0;

        // TODO: refactor to use a list of cards instead of multiple if statements
        if (isThirteenCards(privateCard1, privateCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePrivateCard(privateCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard1, publicCard1)) {
            int score = getScorePrivateCard(privateCard1) + getScorePublicCard(publicCard1);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard1, publicCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard2, publicCard1)) {
            int score = getScorePrivateCard(privateCard2) + getScorePublicCard(publicCard1);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard2, publicCard2)) {
            int score = getScorePrivateCard(privateCard2) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        return maxScore;
    }

    private void calculateScoreEndOfRound() {
        List<Boolean> isThirteenChecks = Arrays.asList(false, false, false, false);
        for (int i = 0; i < hands.length; i++) {
            isThirteenChecks.set(i, isThirteen(i));
        }
        List<Integer> indexesWithThirteen = new ArrayList<>();
        for (int i = 0; i < isThirteenChecks.size(); i++) {
            if (isThirteenChecks.get(i)) {
                indexesWithThirteen.add(i);
            }
        }
        long countTrue = indexesWithThirteen.size();
        Arrays.fill(scores, 0);
        if (countTrue == 1) {
            int winnerIndex = indexesWithThirteen.get(0);
            scores[winnerIndex] = 100;
        } else if (countTrue > 1) {
            for (Integer thirteenIndex : indexesWithThirteen) {
                scores[thirteenIndex] = calculateMaxScoreForThirteenPlayer(thirteenIndex);
            }

        } else {
            for (int i = 0; i < scores.length; i++) {
                scores[i] = getScorePrivateCard(hands[i].getCardList().get(0)) +
                        getScorePrivateCard(hands[i].getCardList().get(1));
            }
        }
    }

    // TODO: move game referee methods into seperate class?
    private boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2) {
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                if (value1 + value2 == THIRTEEN_GOAL) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThirteenCards(Card card1, Card card2) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();
        return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues());
    }

    private boolean isThirteenMixedCards(List<Card> privateCards, List<Card> publicCards) {
        for (Card privateCard : privateCards) {
            for (Card publicCard : publicCards) {
                if (isThirteenCards(privateCard, publicCard)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isThirteen(int playerIndex) {
        List<Card> privateCards = hands[playerIndex].getCardList();
        List<Card> publicCards = playingArea.getCardList();
        boolean isThirteenPrivate = isThirteenCards(privateCards.get(0), privateCards.get(1));
        boolean isThirteenMixed = isThirteenMixedCards(privateCards, publicCards);
        return isThirteenMixed || isThirteenPrivate;
    }

    // TODO: move to new log class?
    private void addCardPlayedToLog(int player, List<Card> cards) {
        if (cards.size() < 2) {
            return;
        }
        logResult.append("P" + player + "-");

        for (int i = 0; i < cards.size(); i++) {
            Rank cardRank = (Rank) cards.get(i).getRank();
            Suit cardSuit = (Suit) cards.get(i).getSuit();
            logResult.append(cardRank.getRankCardLog() + cardSuit.getSuitShortHand());
            if (i < cards.size() - 1) {
                logResult.append("-");
            }
        }
        logResult.append(",");
    }

    private void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    private void addEndOfRoundToLog() {
        logResult.append("Score:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
    }

    private void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
        logResult.append(
                "Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }

    // TODO: move variables to more appopriate spots
    List<Card> initSharedCards = new ArrayList<>();
    List<List<Card>> initPlayerHands = new ArrayList<>();
    List<List<List<Card>>> autoPlayerMovements = new ArrayList<>();

    CardListener cardListener = new CardAdapter() {
        @Override
        public void leftDoubleClicked(Card card) {
            System.out.println("Player selected: " + card);

            // tell players listening that card has been selected
            // not good to do? all players that are not auto can see card that has been
            // selected?
            // idk it works for now, probably don't need to change
            // cardListener for whatever reason has to be created here in CardGame for this
            // to work
            // doesn't work if created in different class e.g. manualController
            // docs say nothing about this or how CardListener/CardAdapter works, super
            // frustrating
            for (int i = 0; i < nbPlayers; i++) {
                if (!players[i].isAuto()) {
                    players[i].setSelected(card);
                    players[i].stopListening();
                }
            }
        }
    };

    static final int MAX_ROUNDS = 4;

    Player[] players;
    Dealer dealer;
    Card selected;

    private void initGame() {
        // FIXME: each player should contain hand and score

        System.out.println("initialising game");

        // read properties file
        PropertiesReader pReader = new PropertiesReader(properties);
        nbPlayers = pReader.getNumPlayers();
        isAuto = pReader.isAuto();
        thinkingTime = pReader.getThinkingTime();
        delayTime = pReader.getDelayTime();
        List<String> playerTypes = pReader.getPlayerTypes();
        List<List<List<String>>> strAutoPlayerMovements = pReader.getStrPlayerAutoMovements();
        List<List<String>> strInitPlayerCards = pReader.getStrInitPlayerHands();
        List<String> strInitSharedCards = pReader.getStrInitSharedCards();

        pReader.printProperties();

        // create players
        players = new Player[nbPlayers];
        PlayerFactory pFactory = new PlayerFactory();
        pFactory.setSharedCards(strInitSharedCards);

        for (int i = 0; i < nbPlayers; i++) {
            players[i] = pFactory.createPlayer(playerTypes.get(i), strInitPlayerCards.get(i), isAuto,
                    strAutoPlayerMovements.get(i));
        }

        // TODO: fix heavy coupling between pFactory and dealer??
        // cards in dealer changed after being used by pFactory
        dealer = pFactory.getDealer();

        // UI stuff //
        // init shared cards
        playingArea = new Hand(Dealer.DECK);
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
            players[i].setCardListener(cardListener);
            players[i].hideCards();
            players[i].renderCards();
        }
    }

    private void playGame() {
        // int winner = 0;
        int currPlayer = 0;
        Card drawnCard = null;
        Card discardCard = null;
        List<Card> cardsPlayed = new ArrayList<>();
        int roundNumber = 1;
        addRoundInfoToLog(roundNumber);

        // start game loop
        while (roundNumber <= MAX_ROUNDS) {
            // player behaviour: //
            // deal a card to the player
            // player selects a card from hand to discard
            // card is removed from hand
            // add to list of cards played (add to discard pile)

            // DEBUG //
            // check player's cards are not in dealer's deck
            // Hand tmpHand = new Hand(dealer.getDeck());
            // for (int i = 0; i < nbPlayers; i++) {
            //     for (Card card : players[i].getCards()) {
            //         for (Card deckCard: tmpHand.getCardList()) {
            //             if (card.equals(deckCard)) {
            //                 System.err.println("Player " + i + " has card in dealer's deck");
            //             }
            //         }
            //     }
            // }

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
            // hands[currPlayer].insert(drawnCard, false);

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

            players[currPlayer].removeCard(discardCard);
            players[currPlayer].renderCards();

            delay(delayTime);

            // hide player's hand
            players[currPlayer].hideCards();
            players[currPlayer].renderCards();

            // log cards played by the player
            addCardPlayedToLog(currPlayer, players[currPlayer].getCards());

            // next player's turn
            currPlayer = (currPlayer + 1) % nbPlayers;

            // if the next player is player 0, increment the round number and log the end of
            // round scores
            if (currPlayer == 0) {
                roundNumber++;
                addEndOfRoundToLog();

                // if more rounds, log the round information
                if (roundNumber <= MAX_ROUNDS) {
                    addRoundInfoToLog(roundNumber);
                }
                // calculateScoreEndOfRound();
            }

            // FIXME: this is never reached?
            // if round is over, calculate round score
            // if (roundNumber > MAX_ROUNDS) {
            // calculateScoreEndOfRound();
            // }

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

        // Actor[] scoreActors = initScore(nbPlayers, scores, bgColor, bigFont);

        // for (int i = 0; i < nbPlayers; i++)
        //     addActor(scoreActors[i], scoreLocations[i]);

        // setupPlayerAutoMovements();

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
