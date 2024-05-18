package game._scorer;

import ch.aplu.jcardgame.Card;

import java.util.List;

public abstract class SummingOption {
    public SummingOption()
    {
        Scorer.addSummingOption(this);
    }

    /**
     * finds if a player can make thirteen using a particular scoringOption
     * @param privateCards a player's private cards
     * @param publicCards the game's public cards
     * @return true if the player can make thirteen with a particular scoring option, false otherwise
     */
    public abstract boolean isThirteen(List<Card> privateCards, List<Card> publicCards);

    /**
     * finds the score of the given cards if scoring case 3 is used
     * @param privateCards a player's private cards
     * @param publicCards the game's public cards
     * @return the number of points for these particular cards if scoring case 3 and this summing option is used
     */
    public abstract int case3Score(List<Card> privateCards, List<Card> publicCards);
}
