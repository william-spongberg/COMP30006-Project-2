package game._state;

import ch.aplu.jcardgame.Card;
import java.util.List;


// contains all data we need for a state change.
// initialised in luckyThirdTeen, and set just before caling the state change.
public class StateData {

    private int player;
    private List<Card> cards;
    private int roundNumber;
    private List<Integer> winners;

    // different constructors depending on the state that is being changed to
    public StateData() {

    }

    // for function addRoundInfoToLog.
    public StateData(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    // for function addCardPlayedToLog
    public StateData(int player, List<Card> cards) {
        this.player = player;
        this.cards = cards;
    }

    // for function addEndOfGameToLog
    public StateData(List<Integer> winners) {
        this.winners = winners;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public List<Integer> getWinners() {
        return winners;
    }

    public void setWinners(List<Integer> winners) {
        this.winners = winners;
    }
}
