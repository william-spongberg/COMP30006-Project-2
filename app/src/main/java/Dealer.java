import _card.CardFactory;
import _card.CardUtil;
import _card.Rank;
import _card.Suit;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.Location;

import java.util.List;

public class Dealer {

    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

    private void dealingOut(Hand[] hands, Hand playingArea, String initialSharedCards, List<String> initialPlayerCards,
            int nbPlayers, int nbCardsPerPlayer, int nbSharedCards) {
        Hand pack = deck.toHand(false);

        if (initialSharedCards != null) {
            String[] initialCards = initialSharedCards.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = CardFactory.getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(true);
                    playingArea.insert(card, true);
                }
            }
        }
        int cardsToShare = nbSharedCards - playingArea.getNumberOfCards();

        for (int j = 0; j < cardsToShare; j++) {
            if (pack.isEmpty())
                return;
            Card dealt = CardUtil.randomCard(pack.getCardList());
            dealt.removeFromHand(true);
            playingArea.insert(dealt, true);
        }

        for (int i = 0; i < nbPlayers; i++) {
            String initialCardsValue = initialPlayerCards.get(i);
            if (initialCardsValue == null) {
                continue;
            }
            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = CardFactory.getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(false);
                    hands[i].insert(card, false);
                }
            }
        }

        for (int i = 0; i < nbPlayers; i++) {
            int cardsToDealt = nbCardsPerPlayer - hands[i].getNumberOfCards();
            for (int j = 0; j < cardsToDealt; j++) {
                if (pack.isEmpty())
                    return;
                Card dealt = CardUtil.randomCard(pack.getCardList());
                dealt.removeFromHand(false);
                hands[i].insert(dealt, false);
            }
        }
    }

    private void dealACardToHand(Hand hand, Hand pack) {
        if (pack.isEmpty())
            return;
        Card dealt = CardUtil.randomCard(pack.getCardList());
        dealt.removeFromHand(false);
        hand.insert(dealt, true);
    }
}
