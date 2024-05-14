import _card.Rank;
import ch.aplu.jcardgame.Card;

public class Referee {
    
    private boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2) {
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                if (value1 + value2 == THIRTEEN_GOAL) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThirteenCards(Card card1, Card card2) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();
        return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues());
    }

    private boolean isThirteenMixedCards(List<Card> privateCards, List<Card> publicCards) {
        for (Card privateCard : privateCards) {
            for (Card publicCard : publicCards) {
                if (isThirteenCards(privateCard, publicCard)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isThirteen(int playerIndex) {
        List<Card> privateCards = hands[playerIndex].getCardList();
        List<Card> publicCards = playingArea.getCardList();
        boolean isThirteenPrivate = isThirteenCards(privateCards.get(0), privateCards.get(1));
        boolean isThirteenMixed = isThirteenMixedCards(privateCards, publicCards);
        return isThirteenMixed || isThirteenPrivate;
    }
}
