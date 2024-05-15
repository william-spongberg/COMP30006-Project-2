package _state;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class GameStart implements State {
    
    final String trumpImage[] = { "bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif" };
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();
    private List<List<String>> playerAutoMovements = new ArrayList<>();

    // TODO: increment version per major commit?
    private final String version = "1.0";

    public final int nbPlayers = 4;
    public final int nbStartCards = 2;
    public final int nbFaceUpCards = 2;

    
}
