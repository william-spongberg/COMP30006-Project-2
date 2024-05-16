
import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/game4.properties";

    public static void main(String[] args) {
        final Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        String logResult = new LuckyThirdteen(properties).runApp();
        // hate this, should create game, then run, then get result
        // forced to do this way for testing
        System.out.println("logResult = " + logResult);
    }

}
