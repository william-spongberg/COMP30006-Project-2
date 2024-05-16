package game._card;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO: remove class, methods have been adopted into Dealer class
public class CardUtil {

    // private static final int SEED = 30008; // TODO: only define once and use by all, needed for testing
    // private static final Random random = new Random(SEED);

    // public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
    //     // FIXME: method never used
    //     int x = random.nextInt(clazz.getEnumConstants().length);
    //     return clazz.getEnumConstants()[x];
    // }

    // // return random Card from ArrayList
    // public static Card randomCard(ArrayList<Card> list) {
    //     int x = random.nextInt(list.size());
    //     return list.get(x);
    // }

    // public Card getRandomCard(Hand hand, int thinkingTime) {
    //     dealACardToHand(hand);

    //     GameGrid.delay(thinkingTime);

    //     int x = random.nextInt(hand.getCardList().size());
    //     return hand.getCardList().get(x);
    // }

    // public static Card getCardFromString(String cardName) {
    // return new Card(getSuitFromString(cardName), getRankFromString(cardName));
    // }    
}
