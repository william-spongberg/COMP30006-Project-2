package game._scorer;

import ch.aplu.jcardgame.Card;
import game._player.Player;

import java.util.List;

public class Scorer {
    private static List<SummingOption> summingOptions;

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
        return false;
    }

    /**
     * adds a SummingOption to the summingOptions list
     * @param summingOption a SummingOption to be added to summingOptions
     */
    public static void addSummingOption(SummingOption summingOption)
    {
        return;
    }
}
