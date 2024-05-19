package game._scorer;

import ch.aplu.jcardgame.Card;
import game._player.Player;
import game._scorer._scoringCases.ScoringCase;
import game._scorer._summingOptions.SummingOption;

import java.util.List;

public class Scorer {
    private static final int CASE_1_THRESHOLD = 1;
    private static final int CASE_2_THRESHOLD = 0;
    private static final int CASE_3_THRESHOLD = 2;
    private static List<SummingOption> summingOptions;
    private static List<ScoringCase> scoringCases;

    /**
     * Returns the winner of a game of LuckyThirteen
     * @param players an array list of players
     * @param publicCards the game's public cards
     * @return a list of players with the highest score; the winner or winners
     */
    public static List<Player> winner(Player[] players, List<Card> publicCards)
    {
        return null;
    }

    /**
     * returns the score of player at index in players
     * @param players an array of players
     * @param index the index of the player to return the score of
     * @param publicCards the game's public cards
     * @return the score of the player at index
     */
    public static int score(Player[] players, int index, List<Card> publicCards)
    {
        // find how many players have thirteen
        int numPlayersWithThirteen = 0;
        for (Player player: players)
        {
            if (hasThirteen(player, publicCards))
            {
                numPlayersWithThirteen++;
                if (numPlayersWithThirteen >= CASE_3_THRESHOLD)
                {
                    break;
                }
            }
        }

        // find what scoring case will be used
        switch (numPlayersWithThirteen)
        {
            case CASE_1_THRESHOLD:
                // case 1
                break;
            case CASE_2_THRESHOLD:
                // case 2
                break;
            default:
                // case 3
        }
        //placeholder
        return 0;
    }

    /**
     * finds whether a player can make thirteen with their cards
     * @param player the player in question
     * @param publicCards the game's public cards
     * @return true if player has a combination of cards that sum to thirteen, false otherwise
     */
    public static boolean hasThirteen(Player player, List<Card> publicCards)
    {
        List<Card> privateCards = player.getCards();
        for (SummingOption summingOption: summingOptions)
        {
            if (summingOption.isThirteen(privateCards, publicCards))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * gets the score of each player
     * @param players an array containing each player
     * @param publicCards the game's public cards
     * @return a list of integers representing the players' scores int the same order as players
     */
    public static int[] getScores(Player[] players, List<Card> publicCards)
    {
        return null;
    }
}
