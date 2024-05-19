package Util;

import game._card.Rank;
import game._card.Suit;
import ch.aplu.jcardgame.Card;
import game._player.Player;

import java.util.List;
import java.util.stream.Collectors;

import static game._scorer.Scorer.getScores;

//TODO: DEAL WITH THE PACKAGE THIS IS IN. YUCK

public class Logger {

    public static StringBuilder logResult = new StringBuilder();

     public void addCardPlayedToLog(int player, List<Card> cards) {
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

     public void addRoundInfoToLog(int roundNumber) {
         logResult.append("Round" + roundNumber + ":");
     }

     public void addEndOfRoundToLog(Player[] players, List<Card> publicCards) {
         int[] scores = getScores(players, publicCards);
         logResult.append("Score:");
         for (int i = 0; i < scores.length; i++) {
             logResult.append(scores[i] + ",");
         }
         logResult.append("\n");
     }

     public void addEndOfGameToLog(List<Integer> winners, Player[] players, List<Card> publicCards) {
         int[] scores = getScores(players, publicCards);
         logResult.append("EndGame:");
         for (int i = 0; i < scores.length; i++) {
             logResult.append(scores[i] + ",");
         }
         logResult.append("\n");
         logResult.append(
                 "Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
     }
}
