package pl.karcisze.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Parser for "*.properties" files. It loads Properties from the file and put it into a given AppProperties class instance.
 *
 * @author Krzysztof Arciszewski
 */
public class PropertyFileParserProperty extends PropertyFileParser {

	public final static Logger LOGGER = Logger.getLogger("AppProperties");

	/*
	 * Constructor to create Parser with a given AppPropertiesManagerExtensible instance.
	 * The instance is used to call back addProperty function.
	 *
	 * @param appPropertiesManagerExtensible
	 */
	public PropertyFileParserProperty(AppPropertiesManagerExtensible appPropertiesManagerExtensible){
		super(appPropertiesManagerExtensible);
	}

	/*
	 * Parses given InputStream in order to define Properties and to put them into TrialAppProperties instance
	 *
	 * @param fi InputStream of data to be parsed
	 * @param appProperties container to put Properties into
	 */
	@Override
	protected void parseInto(InputStream fi, TrialAppProperties appProperties) {
		Properties _properties = new Properties();
		try {
			_properties.load(fi);
		} catch (IOException e) {
			throw new AppPropertiesException("error parsing *.properties from input stream",e);
		}
		Set keys = _properties.keySet();
		for (Object _key : keys) {
			String key = _key.toString();
			String value = (String) _properties.get(key);

			LOGGER.log(Level.INFO, "Property to be added: key:["+key+"] value:["+value+"]");
			appPropertiesManagerExtensible.addProperty(appProperties, key, value);
		}
	}
}
