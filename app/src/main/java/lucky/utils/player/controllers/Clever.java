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
import lucky.utils.card.Rank;
import lucky.utils.card.Suit;
import lucky.utils.scorer.scoringCases.ScoringCase;
import java.util.ArrayList;
import java.util.List;
import static lucky.utils.scorer.CardEvaluator.getCardScore;
import static lucky.utils.scorer.Scorer.hasThirteen;

public class Clever implements PlayerController {

    // create an instance to preserve singleton
    private DiscardPile discardPile;

    private List<Card> sharedCards;

    public Clever(List<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }

    @Override
    public Card discardCard(Hand hand) {
        Integer indexToRemove = 0;
        List<Card> cards = new ArrayList<>(hand.getCardList());
        // returns a copy, as is singleton
        List<Card> cardsPlayed = discardPile.getDiscardCards();


        List<Card> cardGroupToCheck1 = new ArrayList<>(hand.getCardList()).subList(0, 2);
        List<Card> cardGroupToCheck2 = new ArrayList<>(hand.getCardList()).subList(1, 3);
        List<Card> cardGroupToCheck3 = new ArrayList<>(hand.getCardList());
        cardGroupToCheck3.remove(1);
        if (hasThirteen(cardGroupToCheck1, sharedCards) || hasThirteen(cardGroupToCheck2, sharedCards)
                || hasThirteen(cardGroupToCheck3, sharedCards)) {
            indexToRemove = thirteenChecker(sharedCards, cards);
        } else {
            indexToRemove = cleverCardToRemove(cardsPlayed, hand);
        }
        return hand.getCardList().get(indexToRemove);
    }

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

    private Integer cleverCardToRemove(List<Card> cardsPlayed, Hand hand) {
        List<Card> cardsInHand = new ArrayList<>(hand.getCardList());
        int bestCardIndex = 0; // just in case
        double bestAverageScore = 0;

        for (int i = 0; i < cardsInHand.size(); i++) {
            List<Card> newHand = new ArrayList<>(cardsInHand);

            double averageScore = maximiseScore(newHand, cardsPlayed);
            // System.out.println("Removed card: " + removedCard); // Debug: Print the
            // removed card
            // System.out.println("Average score after removing " + removedCard + ": " +
            // averageScore); // Debug: Print average score
            if (averageScore > bestAverageScore) {
                bestAverageScore = averageScore;
                bestCardIndex = i;
            }
        }

        return bestCardIndex;
    }

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

    private double maximiseScore(List<Card> hand, List<Card> cardsPlayed) {
        double totalEval = 0;
        int evaluationCount = 0;

        for (Card card : hand) {
            ArrayList<Card> newHand = new ArrayList<>(hand);
            newHand.remove(card);

            for (Card possibleCard : getPossibleCardsToDraw(cardsPlayed, sharedCards, newHand)) {
                List<Card> tempHand = new ArrayList<>(newHand);
                tempHand.add(possibleCard);
                totalEval += evaluateHand(tempHand, sharedCards);
                evaluationCount++;
            }
        }

        // avoid dividing by zero
        if (evaluationCount == 0) {
            return 0;
        }
        return totalEval / evaluationCount;
    }

    // hypothesises what the deck looks like based on what cards are visible to the
    // bot
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
