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
    // attributes
    private int player;
    private List<Card> cards;
    private int roundNumber;
    private List<Integer> winners;
    private Player[] players;
    private List<Card> publicCards;

    /**
     * Default constructor for the StateData class.
     */
    public StateData() {
    }

    /**
     * Constructor for the StateData class according to the round number.
     * 
     * @param roundNumber the number of the round
     */
    public StateData(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * Constructor for the StateData class according to the player and cards.
     * 
     * @param player the player
     * @param cards the cards
     */
    public StateData(int player, List<Card> cards) {
        this.player = player;
        this.cards = cards;
    }

    /**
     * Constructor for the StateData class according to the winners, players, and public cards.
     * 
     * @param winners the list of winners
     * @param players the array of players
     * @param publicCards the list of public cards
     */
    public StateData(List<Integer> winners, Player[] players, List<Card> publicCards) {
        this.winners = winners;
        this.players = players;
        this.publicCards = publicCards;
    }

    /**
     * Constructor for the StateData class according to the players and public cards.
     * 
     * @param players the array of players
     * @param publicCards the list of public cards
     */
    public StateData(Player[] players, List<Card> publicCards) {
        this.players = players;
        this.publicCards = publicCards;
    }

    // getters

    public Player[] getPlayers() {
        return players;
    }

    public List<Card> getPublicCards() {
        return publicCards;
    }

    public int getPlayer() {
        return player;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public List<Integer> getWinners() {
        return winners;
    }

    // setters

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setWinners(List<Integer> winners) {
        this.winners = winners;
    }
}
