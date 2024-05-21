package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import game.DiscardPile;
import game._card.Rank;
import game._card.Suit;
import game._scorer._scoringCases.ScoringCase;


import java.util.ArrayList;
import java.util.List;

import static game._scorer.CardEvaluator.getCardScore;
import static game._scorer.Scorer.hasThirteen;

public class Clever implements PlayerController {

    private List<Card> sharedCards;
    public Clever(List<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }

    @Override
    public Card discardCard(Hand hand) {
        Integer indexToRemove = 0;
        List<Card> cards = new ArrayList<>(hand.getCardList());
        List<Card> cardsPlayed = DiscardPile.getDiscardCards();
        List<Card> cardGroupToCheck1 = new ArrayList<>(hand.getCardList()).subList(0, 2);
        List<Card> cardGroupToCheck2 = new ArrayList<>(hand.getCardList()).subList(1, 3);
        List<Card> cardGroupToCheck3 = new ArrayList<>(hand.getCardList());
        cardGroupToCheck3.remove(1);
        if (hasThirteen(cardGroupToCheck1, sharedCards) || hasThirteen(cardGroupToCheck2, sharedCards) || hasThirteen(cardGroupToCheck3, sharedCards)) {
            //System.out.println("We have thirteen!");
            indexToRemove = thirteenChecker(sharedCards, cards);
        }
        else {
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

        // if all cards result in 13, resort to basic, remove the lowest value contributor.
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
            // this is suspicious, lol
            Card removedCard = newHand.remove(i);

            double averageScore = maximiseScore(newHand, cardsPlayed);
            //System.out.println("Removed card: " + removedCard); // Debug: Print the removed card
            //System.out.println("Average score after removing " + removedCard + ": " + averageScore); // Debug: Print average score
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
            //System.out.println(counter_per_card);
            // always evaluate as a case3.
            score = scoringCases.get(2).score(cardsInHand, sharedCards);
            //score = 100;

        }
        else {
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

        return totalEval / evaluationCount;
    }



    // hypothesises what the deck looks like based on what cards are visible to the bot
    private ArrayList<Card> getPossibleCardsToDraw(List<Card> cardsPlayed, List<Card> sharedCards, ArrayList<Card> hand) {
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
