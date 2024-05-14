import _card.Rank;
import _card.Suit;
import ch.aplu.jcardgame.Card;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.TextActor;

public class Scorer {

    private Actor[] scoreActors = { null, null, null, null };

    private Actor[] initScore(int nbPlayers) {
        for (int i = 0; i < nbPlayers; i++) {
            String text = "[" + String.valueOf(scores[i]) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
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

    private int calculateMaxScoreForThirteenPlayer(int playerIndex) {
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

    private void updateScore(int player) {
        // FIXME: why create new actor each time? just change text
        removeActor(scoreActors[player]);
        int displayScore = Math.max(scores[player], 0);
        String text = "P" + player + "[" + String.valueOf(displayScore) + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private void initScores() {
        Arrays.fill(scores, 0);
    }
}
