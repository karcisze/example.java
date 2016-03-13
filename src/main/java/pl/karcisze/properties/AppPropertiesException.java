package pl.karcisze.properties;

/**
 * Runtime exception for reporting errors and abnormal behaviour inside the AppProperties class infrastructure.
 *
 * @author Krzysztof Arciszewski
 */
public class AppPropertiesException extends RuntimeException {

    public AppPropertiesException(String s){
        super(s);
    }

    public AppPropertiesException(String s, Throwable t){
        super(s,t);
    }
}
