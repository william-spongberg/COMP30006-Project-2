/**
 * Driver.java
 * 
 * this class is used to run the game.
 * 
 * @author Subject Staff
 */

package lucky;

import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/game1.properties";

    public static void main(String[] args) {
        // load properties file
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);

        // run the game
        String logResult = new LuckyThirdteen(properties).runApp();

        // print the result
        System.out.println("logResult = " + logResult);
    }
}
