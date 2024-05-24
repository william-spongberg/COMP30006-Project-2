/**
 * Clever.java
 * contains the behaviour for the clever player when discarding a card, which is:
 * when we have thirteen - remove whatever allows us to keep that thirteen.
 * if all cards contribute to a thirteen, dicard the lowest value -
 * if we dont have thirteen, simulate the removal of each card from hand and the subsequent
 * consequence by averaging the result when having obtained every card that is *likely* to be in the deck
 * after removing discard cards, public cards, and the clever players private cards from said deck
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.player.controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import lucky.utils.dealer.DiscardPile;
import lucky.utils.scorer.scoring_cases.ScoringCase;
import lucky.utils.card.Rank;
import lucky.utils.card.Suit;

import java.util.ArrayList;
import java.util.List;
import static lucky.utils.scorer.CardEvaluator.getCardScore;
import static lucky.utils.scorer.Scorer.hasThirteen;

public class Clever implements PlayerController {
    // attributes
    private DiscardPile discardPile = DiscardPile.getInstance();
    private List<Card> sharedCards;

    /*
     * Constructor for the Clever class.
     * Is given a list of shared cards on creation
     */
    public Clever(List<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }

    /*
     * Discards a card from the hand based on the current state of the game
     * Picks the card that maximises the score of the hand after the card is removed
     * 
     * @param the hand of the player
     * @return the card to discard
     */
    @Override
    public Card discardCard(Hand hand) {
        Integer indexToRemove = 0;
        List<Card> cards = new ArrayList<>(hand.getCardList());
        // returns a copy, as is singleton
        List<Card> cardsPlayed = discardPile.getDiscardCards();

        // this is done as at the start of this method, we have three cards, and to
        // check if we have thirteen, we use
        // two cards.
        List<Card> cardGroupToCheck1 = new ArrayList<>(hand.getCardList()).subList(0, 2);
        List<Card> cardGroupToCheck2 = new ArrayList<>(hand.getCardList()).subList(1, 3);
        List<Card> cardGroupToCheck3 = new ArrayList<>(hand.getCardList());
        cardGroupToCheck3.remove(1);

        // check if we have thirteen from all possible combos
        if (hasThirteen(cardGroupToCheck1, sharedCards) || hasThirteen(cardGroupToCheck2, sharedCards)
                || hasThirteen(cardGroupToCheck3, sharedCards)) {
            // if we do, remove whatever doesnt contribute to 13 or the lowest value card
            indexToRemove = thirteenChecker(sharedCards, cards);
        } else {
            // else, we estimate what the best card is. we remove whatever maximises the
            // score.
            indexToRemove = cleverCardToRemove(cardsPlayed, hand);
        }
        return hand.getCardList().get(indexToRemove);
    }

    /**
     * Finds the index of the card to be removed from the hand that results in a sum
     * of 13 when combined with the shared cards.
     * If no such card is found, the index of the card with the lowest value is
     * returned.
     *
     * @param sharedCards the list of shared cards
     * @param hand        the list of cards in the hand
     * @return the index of the card to be removed from the hand
     */
    private Integer thirteenChecker(List<Card> sharedCards, List<Card> hand) {

        for (int i = 0; i < hand.size() - 1; i++) {
            List<Card> tempHand = new ArrayList<>(hand);
            tempHand.remove(i);
            if ((hasThirteen(tempHand, sharedCards))) {
                return i;
            }
        }

        // if all cards result in 13, resort to basic, remove the lowest value
        // contributor.
        int worstCardIndex = 0;
        Card smallestCard = hand.get(0);
        for (int i = 1; i < hand.size(); i++) {
            Card currentCard = hand.get(i);
            if (getCardScore(currentCard, false) < getCardScore(smallestCard, false)) {
                worstCardIndex = i;
            }
        }
        // just remove zero since we already have thirteen anyway
        return worstCardIndex;
    }

    /**
     * Returns the index of the clever card to remove from the hand.
     *
     * @param cardsPlayed the list of cards played by other players
     * @param hand        the hand of the player
     * @return the index of the clever card to remove
     */
    private Integer cleverCardToRemove(List<Card> cardsPlayed, Hand hand) {
        List<Card> cardsInHand = new ArrayList<>(hand.getCardList());
        int bestCardIndex = 0;
        double bestAverageScore = 0;

        // iterate over each card in hand
        for (int i = 0; i < cardsInHand.size(); i++) {
            List<Card> newHand = new ArrayList<>(cardsInHand);
            newHand.remove(i); // Remove the card at index i
            double averageScore = maximiseScore(newHand, cardsPlayed);
            if (averageScore > bestAverageScore) {
                bestAverageScore = averageScore;
                bestCardIndex = i;
            }
        }

        return bestCardIndex;
    }

    /**
     * Calculates the maximum score for a given hand and cards played.
     * Uses a simulation akin to Mini-Max to maximise the score of the hand after
     * removing a card.
     * 
     * @param hand        The list of cards in the player's hand.
     * @param cardsPlayed The list of cards already played in the game.
     * @return The maximum score calculated for the given hand and cards played.
     */
    private double maximiseScore(List<Card> hand, List<Card> cardsPlayed) {
        double totalEval = 0;
        int evaluationCount = 0;

        // List of possible cards to draw
        ArrayList<Card> possibleCards = getPossibleCardsToDraw(cardsPlayed, sharedCards, new ArrayList<>(hand));

        // Simulate every possible pair in the future for the two cards in our hand
        for (int i = 0; i < hand.size(); i++) {
            List<Card> tempHand = new ArrayList<>(hand);
            tempHand.remove(i);

            // Evaluate all possible two-card hands formed by drawing a new card
            for (Card possibleCard : possibleCards) {
                List<Card> simulatedHand = new ArrayList<>(tempHand);
                simulatedHand.add(possibleCard);
                totalEval += evaluateHand(simulatedHand, sharedCards); // Evaluate the hand
                evaluationCount++;
            }
        }
        if (evaluationCount == 0) {
            return 0;
        }
        return totalEval / evaluationCount;
    }

    /**
     * Evaluates the score of a hand based on the given cards in hand and shared
     * cards.
     *
     * @param cardsInHand the list of cards in hand
     * @param sharedCards the list of shared cards
     * @return the score of the hand
     */
    private int evaluateHand(List<Card> cardsInHand, List<Card> sharedCards) {

        int score;
        List<ScoringCase> scoringCases = ScoringCase.getScoringCases();

        if (hasThirteen(cardsInHand, sharedCards)) {
            // always evaluate as a case3.
            score = scoringCases.get(2).score(cardsInHand, sharedCards);

        } else {
            // if we dont have 13, try a case2.
            score = scoringCases.get(1).score(cardsInHand, sharedCards);

        }

        return score;
    }

    /**
     * Returns a list of possible cards that can be drawn from the deck, given the
     * cards already played,
     * the shared cards, and the player's hand.
     *
     * @param cardsPlayed The list of cards already played.
     * @param sharedCards The list of shared cards.
     * @param hand        The player's hand.
     * @return The list of possible cards to draw from the deck.
     */
    private ArrayList<Card> getPossibleCardsToDraw(List<Card> cardsPlayed, List<Card> sharedCards,
            ArrayList<Card> hand) {
        Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
        Hand pack = deck.toHand();
        ArrayList<Card> deckAltered = pack.getCardList();
        for (Card card : cardsPlayed) {
            deckAltered.remove(card);
        }
        for (Card card : sharedCards) {
            deckAltered.remove(card);
        }
        for (Card card : hand) {
            deckAltered.remove(card);
        }

        return deckAltered;
    }

}
