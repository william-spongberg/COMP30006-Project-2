package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import game.DiscardPile;
import game._card.Rank;
import game._card.Suit;
import game._scorer._scoringCases.ScoringCase;
import game._scorer._summingOptions.SummingOption;

import java.util.ArrayList;
import java.util.List;

import static game._scorer.CardEvaluator.getCardScore;
import static game._scorer.Scorer.hasThirteen;
import static game._scorer._summingOptions.SummingOption.isThirteen;

public class Clever implements PlayerController {

    private List<Card> sharedCards;

    public Clever(List<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }

    @Override
    public Card discardCard(Hand hand) {
        List<Card> cardsPlayed = DiscardPile.getDiscardCards();
        Integer indexToRemove = cleverCardToRemove(cardsPlayed, hand);

        if (indexToRemove != null && indexToRemove >= 0 && indexToRemove < hand.getCardList().size()) {
            return hand.getCardList().get(indexToRemove);
        }

        // Fallback: if cleverCardToRemove fails, return the first card as a default
        return hand.getCardList().isEmpty() ? null : hand.getCardList().get(0);
    }


    // THINGS TO NOTE:
        // EVERY PLAYER CAN SEE THE DISCARDED CARDs
        // so we know what cards aren't around anymore.
        // this can be used as an optimisation
        // we know how many cards are left
        // we know what cards we have (3) and what the two public cards are
        // the goal: cleverly decide which card we should remove
        // only four rounds
    // return index of card to discard


    private Integer cleverCardToRemove(List<Card> cardsPlayed, Hand hand) {
        List<Card> cardsInHand = new ArrayList<>(hand.getCardList());
        int bestCardIndex = 0; // just in case
        double bestAverageScore = 0;

        for (int i = 0; i < cardsInHand.size(); i++) {
            List<Card> newHand = new ArrayList<>(cardsInHand);
            // this is suspicious, lol
            Card removedCard = newHand.remove(i);

            double averageScore = maximiseScore(newHand, cardsPlayed);
            System.out.println("Removed card: " + removedCard); // Debug: Print the removed card
            System.out.println("Average score after removing " + removedCard + ": " + averageScore); // Debug: Print average score
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
            //score = 100;

        }
        else {
            // if we dont have 13, try a case1.
            score = scoringCases.get(0).score(cardsInHand, sharedCards);

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
