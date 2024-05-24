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
    // attributes
    public static StringBuilder logResult = new StringBuilder();

    /**
     * Appends the played cards of a player to the log.
     *
     * @param player the player number
     * @param cards the list of cards played by the player
     */
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

    /**
     * Appends the round information to the log.
     *
     * @param roundNumber the number of the round
     */
    public void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    /**
     * Adds the end of round information to the log.
     * Calculates the scores for each player and appends them to the log.
     *
     * @param players      an array of Player objects representing the players in the game
     * @param publicCards  a List of Card objects representing the public cards in the game
     */
    public void addEndOfRoundToLog(Player[] players, List<Card> publicCards) {
        int[] scores = getScores(players, publicCards);
        logResult.append("Score:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
    }

    /**
     * Appends the end of game information to the log.
     *
     * @param winners      the list of integers representing the winners' indices
     * @param players      the array of Player objects representing the players in the game
     * @param publicCards  the list of Card objects representing the public cards in the game
     */
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
