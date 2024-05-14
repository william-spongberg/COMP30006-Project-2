package _player.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class AutoController implements PlayerController{
    
    private Card applyAutoMovement(Hand hand, String nextMovement) {
        if (pack.isEmpty())
            return null;
        String[] cardStrings = nextMovement.split("-");
        String cardDealtString = cardStrings[0];
        Card dealt = getCardFromList(pack.getCardList(), cardDealtString);
        if (dealt != null) {
            dealt.removeFromHand(false);
            hand.insert(dealt, true);
        } else {
            System.out.println("cannot draw card: " + cardDealtString + " - hand: " + hand);
        }

        if (cardStrings.length > 1) {
            String cardDiscardString = cardStrings[1];
            return getCardFromList(hand.getCardList(), cardDiscardString);
        } else {
            return null;
        }
    }

    private void setupPlayerAutoMovements() {
        String player0AutoMovement = properties.getProperty("players.0.cardsPlayed");
        String player1AutoMovement = properties.getProperty("players.1.cardsPlayed");
        String player2AutoMovement = properties.getProperty("players.2.cardsPlayed");
        String player3AutoMovement = properties.getProperty("players.3.cardsPlayed");

        // FIXME: playerMovements should be immediately initialised with properties,
        // remove if statements
        String[] playerMovements = new String[] { "", "", "", "" };

        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }

        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }

        if (player2AutoMovement != null) {
            playerMovements[2] = player2AutoMovement;
        }

        if (player3AutoMovement != null) {
            playerMovements[3] = player3AutoMovement;
        }

        for (int i = 0; i < playerMovements.length; i++) {
            // FIXME: unnecessary to define here
            String movementString = playerMovements[i];
            if (movementString.equals("")) {
                playerAutoMovements.add(new ArrayList<>());
                continue;
            }
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
    }
}
