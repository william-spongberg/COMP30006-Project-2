package game._observer;

import Util.Logger;
import game._player.Player;
import game._scorer.Scorer;
import game._state.StateData;
import game._state.States;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScorerObserver implements Observer{

    public void onStateUpdate(States state, StateData stateData, Player[] newPlayers){
        // contact ScoreA

    }
}
