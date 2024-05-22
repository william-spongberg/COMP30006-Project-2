/**
 * Logger.java
 * 
 * the Logger class is used to log the results of the game.
 * has the methods addCardPlayedToLog, addRoundInfoToLog, addEndOfRoundToLog, addEndOfGameToLog.
 * which are used to log the results of the game.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.observer;

import lucky.utils.card.Rank;
import lucky.utils.card.Suit;
import ch.aplu.jcardgame.Card;
import lucky.utils.player.Player;

import java.util.List;
import java.util.stream.Collectors;

import static lucky.utils.scorer.Scorer.getScores;


// contains all the methods for logging to be used by the Observer
public class Logger {

    // this must be static, as it needs to be used by the LuckyThirdteen class. If it were not static, it would
    // not be possible to return from here. Given the log is globally visible, this is not beleived to be a problem.
    public static StringBuilder logResult = new StringBuilder();

    // used at the end of a turn
    public void addCardPlayedToLog(int player, List<Card> cards) {
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

    // used at start of the round
    public void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    // used at the end of a round
    public void addEndOfRoundToLog(Player[] players, List<Card> publicCards) {
        int[] scores = getScores(players, publicCards);
        logResult.append("Score:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
    }

    // used at the end of the game
    public void addEndOfGameToLog(List<Integer> winners, Player[] players, List<Card> publicCards) {
        int[] scores = getScores(players, publicCards);
        logResult.append("EndGame:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
        logResult.append(
                "Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }
}
