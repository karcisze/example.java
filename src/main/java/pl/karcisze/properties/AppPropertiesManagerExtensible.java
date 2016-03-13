package pl.karcisze.properties;

/**
 * This interface extends AppPropertiesManager by adding a method to allow adding a Property to a given instance of TrialAppProperties
 *
 * @author Krzysztof Arciszewski
 */
public interface AppPropertiesManagerExtensible extends AppPropertiesManager {

    /**
     * Given instance of TrialAppProperties, puts there a Property of a given key and value.
     *
     * @param appProperties container to put a new Property into
     * @param key value of key that specifies Property to be put into appProperties container
     * @param value value of the Property to be put into appProperties container
     * @return a fully constructed TrialProperties object
     */
    public void addProperty(TrialAppProperties appProperties, String key, Object value);
}
