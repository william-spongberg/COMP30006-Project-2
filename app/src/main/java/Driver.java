import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/game2.properties";

    public static void main(String[] args) {
        final Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        // TODO: check if properties is null, if so, print error message and return
        String logResult = new LuckyThirdteen(properties).runApp();
        // hate this, should create game, then run, then get result
        System.out.println("logResult = " + logResult);
    }

}
