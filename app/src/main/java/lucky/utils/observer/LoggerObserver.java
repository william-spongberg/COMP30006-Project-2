/**
 * LoggerObserver.java
 * Contains a Observer for the Logger. This observer has a logger within it, which is
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

    private Logger logger = new Logger();

    private Player[] players;
    private List<Card> publicCards;

    public void onStateUpdate(States state, StateData stateData) {

        // each observer keeps an instance of players
        // contact Logger

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
                players = stateData.getPlayers();
                publicCards = stateData.getPublicCards();
                logger.addEndOfRoundToLog(players, publicCards);
                break;

            case END_GAME:
                List<Integer> winners = stateData.getWinners();
                players = stateData.getPlayers();
                publicCards = stateData.getPublicCards();
                logger.addEndOfGameToLog(winners, players, publicCards);
                break;
            default:
                System.out.println("State not found");
                System.exit(1);
        }

    }
}
