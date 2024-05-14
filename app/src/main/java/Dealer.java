import _card.Rank;
import _card.Suit;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.Location;

public class Dealer {

    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

    

    private void dealingOut(Hand[] hands, int nbPlayers, int nbCardsPerPlayer, int nbSharedCards) {
        pack = deck.toHand(false);

        String initialShareKey = "shared.initialcards";
        String initialShareValue = properties.getProperty(initialShareKey);
        if (initialShareValue != null) {
            String[] initialCards = initialShareValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCard);
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
            Card dealt = randomCard(pack.getCardList());
            dealt.removeFromHand(true);
            playingArea.insert(dealt, true);
        }

        for (int i = 0; i < nbPlayers; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = properties.getProperty(initialCardsKey);
            if (initialCardsValue == null) {
                continue;
            }
            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCard);
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
                Card dealt = randomCard(pack.getCardList());
                dealt.removeFromHand(false);
                hands[i].insert(dealt, false);
            }
        }
    }

    private void dealACardToHand(Hand hand) {
        if (pack.isEmpty())
            return;
        Card dealt = randomCard(pack.getCardList());
        dealt.removeFromHand(false);
        hand.insert(dealt, true);
    }
}
