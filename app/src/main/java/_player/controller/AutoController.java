package _player.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

import java.util.Properties;

import _card.CardUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class AutoController implements PlayerController {

    private List<String> autoMovements = new ArrayList<>();

    public AutoController(List<String> autoMovements) {
        this.autoMovements = autoMovements;
    }

    public Card getMove(Hand hand) {
        // TODO: convert to Card?
        //return autoMovements.remove(0);
        Card card = autoMovements.remove(0);

        // pretend to think
        GameGrid.delay(delayTime);

        // if auto move available, return card
        if (card != null) {
            card.removeFromHand(false);
        }
        else {
            // if no auto move available, get random card and remove from hand
            card = getRandomCard(hand);
            card.removeFromHand(true);
        }
        
        return card;
    }

    // public Card applyAutoMovement(Hand hand, String nextMovement) {
    //     // if (pack.isEmpty())
    //     //     return null;

    //     String[] cardStrings = nextMovement.split("-");
    //     String cardDealtString = cardStrings[0];
    //     Card dealt = CardUtil.getCardFromList(pack.getCardList(), cardDealtString);

    //     if (dealt != null) {
    //         dealt.removeFromHand(false);
    //         hand.insert(dealt, true);
    //     } else {
    //         System.out.println("cannot draw card: " + cardDealtString + " - hand: " + hand);
    //     }

    //     if (cardStrings.length > 1) {
    //         String cardDiscardString = cardStrings[1];
    //         return CardUtil.getCardFromList(hand.getCardList(), cardDiscardString);
    //     } else {
    //         return null;
    //     }
    // }
}
