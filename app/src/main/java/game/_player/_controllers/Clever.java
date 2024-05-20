package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import game.DiscardPile;
import game._card.Rank;
import game._card.Suit;

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
        int worstCardIndex = 0; // just in case
        double worstAverageScore = 0;

        for (int i = 0; i < cardsInHand.size(); i++) {
            List<Card> newHand = new ArrayList<>(cardsInHand);
            // this is suspicious, lol
            newHand.remove(i);

            double averageScore = maximiseScore(newHand, cardsPlayed, 0);
            if (averageScore > worstAverageScore) {
                worstAverageScore = averageScore;
                worstCardIndex = i;
            }
        }

        return worstCardIndex;
    }


    // we need to evaluate hands to see if they have 13 in them
    // if they do, we optimise for score. (i.e, look for higher scoring 13s)
    // TODO: this doesn't account for the case where 2 CARDS IN PUBLIC and 2 CARDS IN PRIVATE make a thirteen! which is the best case!
    private int evaluateHand(List<Card> cardsInHand, List<Card> sharedCards) {
        List<Card[]> thirteensInHand = new ArrayList<>();
        for (Card card1: cardsInHand) {
            for (Card card2: sharedCards) {
                if (isThirteen(card1, card2)) {
                    Card[] thirteenCombo = {card1, card2};
                    thirteensInHand.add(thirteenCombo);
                }
            }
        }
        ArrayList<Integer> scores = new ArrayList<>();

        // TODO: THIS TREATS SCORE AS ALWAYS PRIVATE CARD IN HAND!
        if (!thirteensInHand.isEmpty()) {
            for (Card[] thirteenMatch : thirteensInHand) {
                int scoreVal = getCardScore(thirteenMatch[0], false) + getCardScore(thirteenMatch[1], false);
                scores.add(scoreVal);
            }
        }
        // if no thirteens, choose highest scoring card
        else {
            for (Card card: cardsInHand) {
                scores.add(getCardScore(card, false));
            }
        }

        Integer bestScore = 0;
        // now get the top scoring card.
        for (Integer tempScore: scores) {
            if (tempScore > bestScore) {
                bestScore = tempScore;
            }
        }

        return bestScore;
    }

    private double maximiseScore(List<Card> hand, List<Card> cardsPlayed, int depth) {
        // Base case: stop recursion at depth 4 (end of the game)
        if (depth == 4) {
            return evaluateHand(hand, sharedCards);
        }

        double totalEval = 0;
        int evaluationCount = 0;

        for (Card card : new ArrayList<>(hand)) {
            ArrayList<Card> newHand = new ArrayList<>(hand);
            newHand.remove(card);

            for (Card possibleCard : getPossibleCardsToDraw(cardsPlayed, sharedCards, newHand)) {
                newHand.add(possibleCard);
                totalEval += maximiseScore(newHand, cardsPlayed, depth + 1);
                newHand.remove(possibleCard);
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
