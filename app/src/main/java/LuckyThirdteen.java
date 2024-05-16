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
import game._player.controller.ManualController;

@SuppressWarnings("serial")
public class LuckyThirdteen extends CardGame {
    // TODO: move values put here to static finals in info expert?
    // TODO: move attributes into respective classes?
    final String trumpImage[] = { "bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif" };
    static public final int seed = 30008;
    static final Random random = new Random(seed);
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();
    //private List<List<String>> playerAutoMovements = new ArrayList<>();

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

    // private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

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

    // private Actor[] initScore(int nbPlayers, int[] scores, Color bgColor, Font
    // bigFont) {
    // for (int i = 0; i < nbPlayers; i++) {
    // String text = "[" + String.valueOf(scores[i]) + "]";
    // scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
    // //addActor(scoreActors[i], scoreLocations[i]);
    // }
    // return scoreActors;
    // }

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

    // TODO: move into new getter classes?
    // public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
    // // FIXME: method never used
    // int x = random.nextInt(clazz.getEnumConstants().length);
    // return clazz.getEnumConstants()[x];
    // }

    // return random Card from ArrayList
    // public static Card randomCard(ArrayList<Card> list) {
    // int x = random.nextInt(list.size());
    // return list.get(x);
    // }

    // public Card getRandomCard(Hand hand) {
    // dealACardToHand(hand);

    // delay(thinkingTime);

    // int x = random.nextInt(hand.getCardList().size());
    // return hand.getCardList().get(x);
    // }

    // TODO: combine getRankFromString and getSuitFromString into one method?
    // private Rank getRankFromString(String cardName) {
    // String rankString = cardName.substring(0, cardName.length() - 1);
    // Integer rankValue = Integer.parseInt(rankString);

    // for (Rank rank : Rank.values()) {
    // if (rank.getRankCardValue() == rankValue) {
    // return rank;
    // }
    // }

    // return Rank.ACE;
    // }

    // private Suit getSuitFromString(String cardName) {
    // String rankString = cardName.substring(0, cardName.length() - 1);
    // // FIXME: rankString is not used
    // String suitString = cardName.substring(cardName.length() - 1,
    // cardName.length());
    // // FIXME: rankValue is not used
    // Integer rankValue = Integer.parseInt(rankString);

    // for (Suit suit : Suit.values()) {
    // if (suit.getSuitShortHand().equals(suitString)) {
    // return suit;
    // }
    // }
    // return Suit.CLUBS;
    // }

    // private Card getCardFromList(List<Card> cards, String cardName) {
    // Rank cardRank = getRankFromString(cardName);
    // Suit cardSuit = getSuitFromString(cardName);
    // for (Card card : cards) {
    // if (card.getSuit() == cardSuit
    // && card.getRank() == cardRank) {
    // return card;
    // }
    // }

    // return null;
    // }

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

    // TODO: move into new dealer class?
    // private void dealingOut(Hand[] hands, int nbPlayers, int nbCardsPerPlayer,
    // int nbSharedCards) {
    // pack = deck.toHand(false);

    // String initialShareKey = "shared.initialcards";
    // String initialShareValue = properties.getProperty(initialShareKey);
    // if (initialShareValue != null) {
    // String[] initialCards = initialShareValue.split(",");
    // for (String initialCard : initialCards) {
    // if (initialCard.length() <= 1) {
    // continue;
    // }
    // Card card = getCardFromList(pack.getCardList(), initialCard);
    // if (card != null) {
    // card.removeFromHand(true);
    // playingArea.insert(card, true);
    // }
    // }
    // }
    // int cardsToShare = nbSharedCards - playingArea.getNumberOfCards();

    // for (int j = 0; j < cardsToShare; j++) {
    // if (pack.isEmpty())
    // return;
    // Card dealt = randomCard(pack.getCardList());
    // dealt.removeFromHand(true);
    // playingArea.insert(dealt, true);
    // }

    // for (int i = 0; i < nbPlayers; i++) {
    // String initialCardsKey = "players." + i + ".initialcards";
    // String initialCardsValue = properties.getProperty(initialCardsKey);
    // if (initialCardsValue == null) {
    // continue;
    // }
    // String[] initialCards = initialCardsValue.split(",");
    // for (String initialCard : initialCards) {
    // if (initialCard.length() <= 1) {
    // continue;
    // }
    // Card card = getCardFromList(pack.getCardList(), initialCard);
    // if (card != null) {
    // card.removeFromHand(false);
    // hands[i].insert(card, false);
    // }
    // }
    // }

    // for (int i = 0; i < nbPlayers; i++) {
    // int cardsToDealt = nbCardsPerPlayer - hands[i].getNumberOfCards();
    // for (int j = 0; j < cardsToDealt; j++) {
    // if (pack.isEmpty())
    // return;
    // Card dealt = randomCard(pack.getCardList());
    // dealt.removeFromHand(false);
    // hands[i].insert(dealt, false);
    // }
    // }
    // }

    // private void dealACardToHand(Hand hand) {
    // if (pack.isEmpty())
    // return;
    // Card dealt = randomCard(pack.getCardList());
    // dealt.removeFromHand(false);
    // hand.insert(dealt, true);
    // }

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

    // TODO: move to new auto move class?
    // private Card applyAutoMovement(Hand hand, String nextMovement) {
    // if (pack.isEmpty())
    // return null;
    // String[] cardStrings = nextMovement.split("-");
    // String cardDealtString = cardStrings[0];
    // Card dealt = getCardFromList(pack.getCardList(), cardDealtString);
    // if (dealt != null) {
    // dealt.removeFromHand(false);
    // hand.insert(dealt, true);
    // } else {
    // System.out.println("cannot draw card: " + cardDealtString + " - hand: " +
    // hand);
    // }

    // if (cardStrings.length > 1) {
    // String cardDiscardString = cardStrings[1];
    // return getCardFromList(hand.getCardList(), cardDiscardString);
    // } else {
    // return null;
    // }
    // }

    // private void setupPlayerAutoMovements() {
    // String player0AutoMovement = properties.getProperty("players.0.cardsPlayed");
    // String player1AutoMovement = properties.getProperty("players.1.cardsPlayed");
    // String player2AutoMovement = properties.getProperty("players.2.cardsPlayed");
    // String player3AutoMovement = properties.getProperty("players.3.cardsPlayed");

    // // FIXME: playerMovements should be immediately initialised with properties,
    // // remove if statements
    // String[] playerMovements = new String[] { "", "", "", "" };

    // if (player0AutoMovement != null) {
    // playerMovements[0] = player0AutoMovement;
    // }

    // if (player1AutoMovement != null) {
    // playerMovements[1] = player1AutoMovement;
    // }

    // if (player2AutoMovement != null) {
    // playerMovements[2] = player2AutoMovement;
    // }

    // if (player3AutoMovement != null) {
    // playerMovements[3] = player3AutoMovement;
    // }

    // for (int i = 0; i < playerMovements.length; i++) {
    // // FIXME: unnecessary to define here
    // String movementString = playerMovements[i];
    // if (movementString.equals("")) {
    // playerAutoMovements.add(new ArrayList<>());
    // continue;
    // }
    // List<String> movements = Arrays.asList(movementString.split(","));
    // playerAutoMovements.add(movements);
    // }
    // }

    // TODO: move variables to more appopriate spots
    Player[] players;
    List<Card> initSharedCards = new ArrayList<>();
    List<List<Card>> initPlayerHands = new ArrayList<>();
    List<List<List<Card>>> autoPlayerMovements = new ArrayList<>();

    static final int MAX_ROUNDS = 4;

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

        // TODO: move conversions into PlayerFactory
        // convert strings to cards
        // if (isAuto && !(strAutoPlayerMovements.isEmpty())) {
        // for (List<List<String>> playerMovements : strAutoPlayerMovements) {
        // List<List<Card>> playerMovement = new ArrayList<>();
        // for (List<String> movement : playerMovements) {
        // List<Card> cardMovement = new ArrayList<>();
        // for (String cardString : movement) {
        // // needs to be false - not actually removing card from deck
        // cardMovement.add(dealer.getCard(cardString, false));
        // }
        // playerMovement.add(cardMovement);
        // }
        // autoPlayerMovements.add(playerMovement);
        // }
        // }

        // if (!(strInitPlayerHands.isEmpty())) {
        // for (List<String> playerCards : strInitPlayerHands) {
        // List<Card> playerHand = new ArrayList<>();
        // for (String cardString : playerCards) {

        // playerHand.add(dealer.getCard(cardString, true));
        // }
        // initPlayerHands.add(playerHand);
        // }
        // }
        // // else deal out two cards to each player
        // else {
        // for (int i = 0; i < nbPlayers; i++) {
        // List<Card> playerHand = new ArrayList<>();
        // for (int j = 0; j < 2; j++) {
        // playerHand.add(dealer.getRandomCard(true));
        // }
        // initPlayerHands.add(playerHand);
        // }
        // }

        // if (!(strInitSharedCards.isEmpty())) {
        // for (String cardString : strInitSharedCards) {
        // initSharedCards.add(dealer.getCard(cardString, true));
        // }
        // }
        // // else deal out two cards to shared cards
        // else {
        // for (int i = 0; i < 2; i++) {
        // initSharedCards.add(dealer.getRandomCard(true));
        // }
        // }

        // player cards (UI + manual control stuff)
        // hands = new Hand[nbPlayers];
        // for (int i = 0; i < nbPlayers; i++) {
        //     hands[i] = new Hand(Dealer.DECK);
        //     hands[i].sort(Hand.SortType.SUITPRIORITY, false);
        // }

        // create players
        players = new Player[nbPlayers];
        PlayerFactory pFactory = new PlayerFactory();
        pFactory.setSharedCards(strInitSharedCards);

        for (int i = 0; i < nbPlayers; i++) {
            players[i] = pFactory.createPlayer(playerTypes.get(i), strInitPlayerCards.get(i), isAuto,
                    strAutoPlayerMovements.get(i));

            // tmp for UI/manual control
            // for (Card card : pFactory.getInitCards()) {
            //     hands[i].insert(card, false);
            // }
        }

        // TODO: fix heavy coupling between pFactory and dealer

        // cards in dealer changed after being used by pFactory
        dealer = pFactory.getDealer();

        // basic manual controller setup
        // TODO: move into ManualController
        // CardListener cardListener = new CardAdapter() {
        //     @Override
        //     public void leftDoubleClicked(Card card) {
        //         selected = card;

        //         // enable manual movement for all manual players
        //         for (int i = 0; i < nbPlayers; i++) {
        //             if (players[i].isAuto()) {
        //                 players[i].stopListening();
        //             }
        //         }
        //     }
        // };

        // for (int i = 0; i < nbPlayers; i++) {
        //     if (!players[i].isAuto()) {
        //         // player[i].getHand().addCardListener(cardListener)
        //         // player[i].getController.addCardListener(cardListener);
        //         //players[i].setCardListener((ManualController) players[i].getController());
        //         //hands[i].addCardListener((ManualController)players[i].getController());
        //         //hands[i].addCardListener(cardListener);
        //     }
        // }

        // UI stuff //
        // shared cards
        playingArea = new Hand(Dealer.DECK);
        for (Card card : pFactory.getSharedCards()) {
            playingArea.insert(card, false);
        }
        // draw shared + player cards
        playingArea.setView(this, new RowLayout(trickLocation, (playingArea.getNumberOfCards() + 2) * trickWidth));
        playingArea.draw();
        // RowLayout[] layouts = new RowLayout[nbPlayers];
        // for (int i = 0; i < nbPlayers; i++) {
        //     layouts[i] = new RowLayout(handLocations[i], handWidth);
        //     layouts[i].setRotationAngle(90.0 * i);
        //     hands[i].setView(this, layouts[i]);
        //     hands[i].setTargetArea(new TargetArea(trickLocation));
        //     hands[i].setVerso(true);
        //     hands[i].draw();
        // }
    }

    CardListener cardListener = new CardAdapter() {
        @Override
        public void leftDoubleClicked(Card card) {
            System.out.println("Player selected: " + card);

            // tell players listening that card has been selected
            // not good to do? all players that are not auto can see card that has been selected?
            // idk it works for now, probably don't need to change
            // cardListener for whatever reason has to be created here in CardGame for this to work
            // doesn't work if created in different class e.g. manualController
            // docs say nothing about this or how CardListener/CardAdapter works, super frustrating
            for (int i = 0; i < nbPlayers; i++) {
                if (!players[i].isAuto()) {  
                    players[i].setSelected(card);
                    players[i].stopListening();
                }
            }
        }
    };

    private void playGame() {
        // int winner = 0;
        int currPlayer = 0;
        Card drawnCard = null;
        Card discardCard = null;
        List<Card> cardsPlayed = new ArrayList<>();
        int roundNumber = 1;
        addRoundInfoToLog(roundNumber);

        // draw player cards
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90.0 * i);
            players[i].setView(this, layouts[i]);
            players[i].setTargetArea(new TargetArea(trickLocation));
            players[i].setCardListener(cardListener);
            players[i].hideCards();
            players[i].renderCards();

            // if (!players[i].isAuto()) {
            //     players[i].setCardListener((ManualController) players[i].getController());
            // }

            //addComponentListener()
        }

        // start game loop
        while (roundNumber <= MAX_ROUNDS) {
            // boolean finishedAuto = false;

            // player behaviour:

            // deal a card to the player
            // player selects a card from hand to discard
            // card is removed from hand
            // add to list of cards played (add to discard pile)

            // show player's hand
            // hands[currPlayer].setVerso(false);
            // hands[currPlayer].draw();

            players[currPlayer].showCards();
            players[currPlayer].renderCards();

            // draw card
            drawnCard = players[currPlayer].drawCard();
            if (drawnCard == null) {
                System.out.println("Player " + currPlayer + " did not request a specific card to be drawn");
                drawnCard = dealer.getRandomCard(false);
            }
            players[currPlayer].addCard(dealer.getCard(drawnCard, true));
            //hands[currPlayer].insert(drawnCard, false);

            System.out.println("Player " + currPlayer + " drew card: " + drawnCard);

            for (Card card : players[currPlayer].getCards()) {
                System.out.println("Player " + currPlayer + " has card: " + card);
            }

            players[currPlayer].renderCards();
            //hands[currPlayer].draw();

            // log stuff //
            if (drawnCard != null) {
                // add card to the list of cards played
                cardsPlayed.add(drawnCard);
                delay(delayTime);
            }

            players[currPlayer].renderCards();
            //hands[currPlayer].draw();

            // if (!isAuto) {
            // // updating player hand to reflect drawn card
            // RowLayout handLayout = new RowLayout(handLocations[currPlayer], handWidth);
            // handLayout.setRotationAngle(90.0 * currPlayer);
            // // new hand based on deck
            // Hand tmp = new Hand(Dealer.DECK);
            // for (Card card : hands[currPlayer].getCardList()) {
            // tmp.insert(card, false);
            // }
            // tmp.setView(this, handLayout);
            // tmp.setTargetArea(new TargetArea(trickLocation));
            // tmp.draw();

            // players[currPlayer].setHand(tmp);
            // }

            // // working but shit manual play
            // if (!isAuto) {
            //     hands[currPlayer].setTouchEnabled(true);
            //     setStatus("Player " + currPlayer + " is playing. Please double click on a card to discard");
            //     selected = null;
            //     // dealACardToHand(hands[currPlayer]);
            //     while (selected == null) {
            //         delay(delayTime);
            //     }
            //     discardCard = selected;
            // } else {
            //     // normal discard card behaviour
            //     discardCard = players[currPlayer].discardCard();
            // }

            //players[currPlayer].setHand(hands[currPlayer]);

            if (!players[currPlayer].isAuto()) {
                players[currPlayer].startListening();
                while ((discardCard = players[currPlayer].getSelected()) == null) {
                    delay(delayTime);
                }
            }
            else {
                discardCard = players[currPlayer].discardCard();
            }

            if (discardCard == null) {
                System.err.println("Player " + currPlayer + " did not discard a card");
                // TODO: change to players[currPlayer].getRandomCard()?
                discardCard = players[currPlayer].getCards().get(random.nextInt(players[currPlayer].getCards().size()));
            }
            System.out.println("Player " + currPlayer + " discarded " + discardCard);

            players[currPlayer].removeCard(discardCard);
            players[currPlayer].renderCards();
            //hands[currPlayer].remove(discardCard, false);
            //hands[currPlayer].draw();

            delay(delayTime);

            // hide player's hand
            players[currPlayer].hideCards();
            players[currPlayer].renderCards();
            //hands[currPlayer].setVerso(true);
            //hands[currPlayer].draw();

            // put card in discard pile
            // playingArea.insert(card, true);

            // selected = dealer.getCard(card);

            // // if game is set to auto
            // if (isAuto) {
            // // get next player's auto index and movements
            // int nextPlayerAutoIndex = autoIndexHands[currPlayer];
            // List<String> nextPlayerMovement = playerAutoMovements.get(currPlayer);
            // String nextMovement = "";

            // // if there are more movements
            // if (nextPlayerMovement.size() > nextPlayerAutoIndex) {
            // // get next movement and increment the auto index
            // nextMovement = nextPlayerMovement.get(nextPlayerAutoIndex);
            // nextPlayerAutoIndex++;

            // // update the auto index for the player
            // autoIndexHands[currPlayer] = nextPlayerAutoIndex;
            // Hand nextHand = hands[currPlayer];

            // // apply player movement
            // selected = applyAutoMovement(nextHand, nextMovement);
            // delay(delayTime);

            // // if card was selected, remove from hand
            // if (selected != null) {
            // selected.removeFromHand(true);
            // } else {
            // // if no card was selected, get random card and remove from hand
            // // (default behaviour)
            // selected = getRandomCard(hands[currPlayer]);
            // selected.removeFromHand(true);
            // }
            // } else {
            // // if no more movements for player, set finishedAuto to true
            // finishedAuto = true;
            // }
            // }

            // // if game is not set to auto or if finishedAuto is true
            // if (!isAuto || finishedAuto) {
            // // if the next player is player 0
            // if (0 == currPlayer) {
            // // enable touch for player 0
            // hands[0].setTouchEnabled(true);

            // // set the status message and deal a card to player 0
            // setStatus("Player 0 is playing. Please double click on a card to discard");
            // selected = null;
            // dealACardToHand(hands[0]);

            // // wait until a card is selected
            // while (null == selected) {
            // delay(delayTime);
            // }

            // // remove selected card from the hand
            // selected.removeFromHand(true);
            // } else {
            // // if the next player is not player 0 (human), set the status message
            // setStatusText("Player " + currPlayer + " thinking...");

            // // get random card and remove it from the hand
            // // FIXME: doing random bot behaviour here - if manual all bots will be random
            // selected = getRandomCard(hands[currPlayer]);
            // selected.removeFromHand(true);
            // }
            // }

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
                //calculateScoreEndOfRound();
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
        // addActor(scoreActors[i], scoreLocations[i]);

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
