/**
 * StateData.java
 * 
 * Represents the data needed for a state change in the game.
 * contains a class StateData which is used to store the data needed for a state change.
 * used to store the data needed for a state change.
 * 
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.state;

import ch.aplu.jcardgame.Card;
import lucky.utils.player.Player;

import java.util.List;


// contains all data we need for a state change.
// initialised in luckyThirdTeen, and set just before caling the state change.
public class StateData {

    private int player;
    private List<Card> cards;
    private int roundNumber;
    private List<Integer> winners;

    private Player[] players;

    private List<Card> publicCards;



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
    public StateData(List<Integer> winners, Player[] players, List<Card> publicCards) {
        this.winners = winners;
        this.players = players;
        this.publicCards = publicCards;
    }

    // for addEndOfRoundToLog
    public StateData(Player[] players, List<Card> publicCards) {
        this.players = players;
        this.publicCards = publicCards;
    }

    public Player[] getPlayers() {
        return players;
    }

    public List<Card> getPublicCards() {
        return publicCards;
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
