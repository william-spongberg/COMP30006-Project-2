package game.scorer;

import ch.aplu.jcardgame.Card;
import game.player.Player;
import game.scorer.scoringCases.ScoringCase;
import game.scorer.summingOptions.SummingOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scorer {
    private static final int CASE_1_THRESHOLD = 1;
    private static final int CASE_2_THRESHOLD = 0;
    private static final int CASE_3_THRESHOLD = 2;
    private static final int CASE_1_INDEX = 0;
    private static final int CASE_2_INDEX = 1;
    private static final int CASE_3_INDEX = 2;
    private static final List<SummingOption> SUMMING_OPTIONS = SummingOption.getSummingOptions();
    private static final List<ScoringCase> SCORING_CASES = ScoringCase.getScoringCases();

    /**
     * Returns the winner of a game of LuckyThirteen
     *
     * @param players     an array list of players
     * @param publicCards the game's public cards
     * @return a list of players with the highest score; the winner or winners
     */

    // why are we returning this as a list of players?
    public static List<Integer> winner(Player[] players, List<Card> publicCards) {
        // find the highest score
        int[] scores = getScores(players, publicCards);
        int maxScore = Arrays.stream(scores).max().getAsInt();

        // build list of players with that score
        List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            if (scores[i] == maxScore) {
                winners.add(i);
            }
        }
        return winners;
    }

    /**
     * returns the score of player at index in players
     *
     * @param players     an array of players
     * @param index       the index of the player to return the score of
     * @param publicCards the game's public cards
     * @return the score of the player at index
     */
    public static int score(Player[] players, int index, List<Card> publicCards) {
        // find how many players have thirteen
        int numPlayersWithThirteen = 0;
        for (Player player : players) {
            if (hasThirteen(player, publicCards)) {
                numPlayersWithThirteen++;
                // break early if we're past the case 3 threshold
                if (numPlayersWithThirteen >= CASE_3_THRESHOLD) {
                    break;
                }
            }
        }

        // find what scoring case will be used
        return switch (numPlayersWithThirteen) {
            case CASE_1_THRESHOLD -> SCORING_CASES.get(CASE_1_INDEX).score(players[index].getCards(), publicCards);
            case CASE_2_THRESHOLD -> SCORING_CASES.get(CASE_2_INDEX).score(players[index].getCards(), publicCards);
            default -> SCORING_CASES.get(CASE_3_INDEX).score(players[index].getCards(), publicCards);
        };
    }

    /**
     * finds whether a player can make thirteen with their cards
     *
     * @param player      the player in question
     * @param publicCards the game's public cards
     * @return true if player has a combination of cards that sum to thirteen, false otherwise
     */
    public static boolean hasThirteen(Player player, List<Card> publicCards) {
        List<Card> privateCards = player.getCards();
        for (SummingOption summingOption : SUMMING_OPTIONS) {
            if (summingOption.containsThirteen(privateCards, publicCards)) {
                return true;
            }
        }
        return false;
    }

    /**
     * finds if thirteen can be made with the given private and public cards
     *
     * @param privateCards the player's private cards
     * @param publicCards  the game's public cards
     * @return true if player has a combination of cards that sum to thirteen, false otherwise
     */
    public static boolean hasThirteen(List<Card> privateCards, List<Card> publicCards) {
        for (SummingOption summingOption : SUMMING_OPTIONS) {
            if (summingOption.containsThirteen(privateCards, publicCards)) {
                return true;
            }
        }
        return false;
    }

    /**
     * gets the score of each player
     *
     * @param players     an array containing each player
     * @param publicCards the game's public cards
     * @return a list of integers representing the players' scores int the same order as players
     */
    public static int[] getScores(Player[] players, List<Card> publicCards) {
        int[] scores = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            scores[i] = score(players, i, publicCards);
        }
        return scores;
    }
}
