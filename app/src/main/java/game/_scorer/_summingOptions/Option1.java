package game._scorer._summingOptions;

import ch.aplu.jcardgame.Card;

import java.util.List;

public class Option1 extends SummingOption {
    /**
     * finds if a player can make thirteen using option 1
     * @param privateCards a player's private cards
     * @param publicCards the game's public cards
     * @return true if the player can make thirteen with option 1, false otherwise
     */
    @Override
    public boolean isThirteen(List<Card> privateCards, List<Card> publicCards)
    {
        return false;
    }

    /**
     * finds the score of the given cards if scoring case 3 is used
     * @param privateCards a player's private cards
     * @param publicCards the game's public cards
     * @return the number of points for these particular cards if scoring case 3 and option 1 is used
     */
    @Override
    public int case3Score(List<Card> privateCards, List<Card> publicCards)
    {
        return 0;
    }
}
