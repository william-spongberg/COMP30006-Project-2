package game._player._controllers;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import game.DiscardPile;
import game._card.Rank;
import game._card.Suit;

import java.util.ArrayList;
import java.util.List;

import static game._scorer.Scorer.hasThirteen;

public class Clever implements PlayerController {

    public List<Card> sharedCards;

    public Clever(List<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }

    @Override
    public Card discardCard(Hand hand) {

        // get all discardCards
        List<Card> cardsPlayed = DiscardPile.getDiscardCards();

        switch (cleverCardToRemove(cardsPlayed, hand)) {
            case 0:
                return hand.getCardList().get(0);
            case 1:
                return hand.getCardList().get(1);
            case 2:
                return hand.getCardList().get(2);

        }
        return null;
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
        ArrayList<Card> cardsInHand = hand.getCardList();
        int bestCardIndex = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < cardsInHand.size(); i++) {
            Card cardToDiscard = cardsInHand.get(i);
            ArrayList<Card> newHand = cardsInHand;
            newHand.remove(cardToDiscard);

            int score = minimax(newHand, cardsPlayed, 0, true);
            if (score > bestScore) {
                bestScore = score;
                bestCardIndex = i;
            }
        }

        return bestCardIndex;
    }

    // we need to evaluate hands to see if they have 13 in them
    // if they do, we optimise for score. (i.e, look for higher scoring 13s)
    // TODO: FIGURE OUT HOW TO DO THIS BETTER, RIGHT NOW IT JUST RETURNS TRUE OR FALSE
    private int evaluateHand(List<Card> cardsInHand) {
        int score = 0;
        boolean thirteenCombination = hasThirteen(cardsInHand, sharedCards);
        if (thirteenCombination) {
            //for (Card card : thirteenCombination) {
                //score += checkScore(card);
            //}
            score = 100;
        }
        return score;
    }


    private int minimax(ArrayList<Card> hand, List<Card> cardsPlayed, int depth, boolean isMaximizingPlayer) {
        // Base case: stop recursion at depth 4 (end of the game)
        if (depth == 4) {
            return evaluateHand(hand);
        }

        if (isMaximizingPlayer) {
            int maxEval = 0;
            for (Card card : hand) {
                ArrayList<Card> newHand = hand;
                newHand.remove(card);

                for (Card possibleCard : getPossibleCardsToDraw(cardsPlayed, sharedCards, hand)) {
                    newHand.add(possibleCard);
                    int eval = minimax(newHand, cardsPlayed, depth + 1, false);
                    maxEval = Math.max(maxEval, eval);
                    newHand.remove(possibleCard);
                }
            }
            return maxEval;
        }
        return 0;
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


    // what other helper functions do we need?
}
