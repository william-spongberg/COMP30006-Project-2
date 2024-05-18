package game._observer;

import game._state.States;

import Util.Logger;


public class LoggerObserver implements Observer {

    private Logger logger = new Logger();

    public void onStateUpdate(States changedState) {
        // contact Logger

        switch(changedState) {
            case END_ROUND:
                logger.addEndOfRoundToLog();

        }

    }   
}
