package Util;

import game._card.Rank;
import game._card.Suit;
import ch.aplu.jcardgame.Card;
import java.util.List;
import java.util.stream.Collectors;

//TODO: DEAL WITH THE PACKAGE THIS IS IN. YUCK

public class Logger {

    //TODO: REMOVE THIS: THIS IS TEMP UNTIL I CAN GET SCORES FROM JOSH'S CODE
    private int[] scores = new int[4];
    public StringBuilder logResult = new StringBuilder();

     private void addCardPlayedToLog(int player, List<Card> cards) {
         if (cards.size() < 2) {
             return;
         }
         logResult.append("P" + player + "-");

         for (int i = 0; i < cards.size(); i++) {
             Rank cardRank = (Rank) cards.get(i).getRank();
             Suit cardSuit = (Suit) cards.get(i).getSuit();
             logResult.append(cardRank.getRankCardLog() + cardSuit.getSuitShortHand());
             if (i < cards.size() - 1) {
                 logResult.append("-");
             }
         }
         logResult.append(",");
     }

     private void addRoundInfoToLog(int roundNumber) {
         logResult.append("Round" + roundNumber + ":");
     }

     public void addEndOfRoundToLog() {
         logResult.append("Score:");
         for (int i = 0; i < scores.length; i++) {
             logResult.append(scores[i] + ",");
         }
         logResult.append("\n");
     }

     private void addEndOfGameToLog(List<Integer> winners) {
         logResult.append("EndGame:");
         for (int i = 0; i < scores.length; i++) {
             logResult.append(scores[i] + ",");
         }
         logResult.append("\n");
         logResult.append(
                 "Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
     }
}
