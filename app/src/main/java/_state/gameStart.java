package _state;

public class gameStart implements State {
    
    final String trumpImage[] = { "bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif" };
    static public final int seed = 30008;
    static final Random random = new Random(seed);
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();
    private List<List<String>> playerAutoMovements = new ArrayList<>();

    // TODO: increment version per major commit?
    private final String version = "1.0";

    public final int nbPlayers = 4;
    public final int nbStartCards = 2;
    public final int nbFaceUpCards = 2;

    
}
