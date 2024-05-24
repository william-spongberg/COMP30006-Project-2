/**
 * LoggerObserver.java
 * Contains an observer for the Logger. This observer has a logger within it, which is
 * updated on state change.
 *
 * @author William Spongberg
 * @author Joshua Linehan
 * @author Ethan Hawkins
 */

package lucky.utils.observer;

import ch.aplu.jcardgame.Card;
import lucky.utils.player.Player;
import lucky.utils.state.StateData;
import lucky.utils.state.States;

import java.util.List;

public class LoggerObserver implements Observer {
    // attributes
    private final Logger logger = new Logger();

    /**
     * Updates the state of the observer based on the given state and state data.
     *
     * @param state     the current state of the game
     * @param stateData the data associated with the current state
     */
    public void onStateUpdate(States state, StateData stateData) {
        // switch statement to determine what to do based on the state
        // and update the logger accordingly
        switch (state) {
            case START_GAME:
            case START_ROUND:
                int roundNumber = stateData.getRoundNumber();
                logger.addRoundInfoToLog(roundNumber);
                break;

            case END_TURN:
                int player = stateData.getPlayer();
                List<Card> cards = stateData.getCards();
                logger.addCardPlayedToLog(player, cards);
                break;

            case END_ROUND:
                Player[] players = stateData.getPlayers();
                List<Card> publicCards = stateData.getPublicCards();
                logger.addEndOfRoundToLog(players, publicCards);
                break;

            case END_GAME:
                List<Integer> winners = stateData.getWinners();
                players = stateData.getPlayers();
                publicCards = stateData.getPublicCards();
                logger.addEndOfGameToLog(winners, players, publicCards);
                break;
            default:
                System.err.println("State not found");
                System.exit(1);
        }
    }
}
