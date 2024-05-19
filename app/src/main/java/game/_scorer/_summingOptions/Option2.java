package game._scorer._summingOptions;

import ch.aplu.jcardgame.Card;
import game._scorer.CardEvaluator;

import java.util.List;

// Option 2: one public card and one private card
public class Option2 extends SummingOption {
    /**
     * finds if a player can make thirteen using option 2
     * @param privateCards a player's private cards
     * @param publicCards the game's public cards
     * @return true if the player can make thirteen with option 2, false otherwise
     */
    @Override
    public boolean containsThirteen(List<Card> privateCards, List<Card> publicCards)
    {
        for (Card privateCard: privateCards)
        {
            for (Card publicCard: publicCards)
            {
                if (isThirteen(privateCard, publicCard))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * finds the score of the given cards if scoring case 3 is used
     * @param privateCards a player's private cards
     * @param publicCards the game's public cards
     * @return the number of points for these particular cards if scoring case 3 and option 2 is used
     */
    @Override
    public int case3Score(List<Card> privateCards, List<Card> publicCards)
    {
        int max_score = 0;
        for (Card privateCard: privateCards)
        {
            for (Card publicCard: publicCards)
            {
                if (isThirteen(publicCard, privateCard))
                {
                    int score = CardEvaluator.getCardScore(privateCard, false) + CardEvaluator.getCardScore(publicCard, true);
                    if (score > max_score)
                    {
                        max_score = score;
                    }
                }
            }
        }
        return max_score;
    }
}
