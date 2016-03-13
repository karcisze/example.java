package pl.karcisze.properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Parser for "*.json" files. It loads Properties from the file and put it into a given AppProperties class instance.
 *
 * @author Krzysztof Arciszewski
 */
public class PropertyFileParserJSON extends PropertyFileParser {

	public final static Logger LOGGER = Logger.getLogger("AppProperties");

	/*
	 * Constructor to create Parser with a given AppPropertiesManagerExtensible instance.
	 * The instance is used to call back addProperty function.
	 *
	 * @param appPropertiesManagerExtensible
	 */
	public PropertyFileParserJSON(AppPropertiesManagerExtensible appPropertiesManagerExtensible){
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
		InputStreamReader reader = new InputStreamReader(fi);
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(reader);
		} catch (IOException e) {
			throw new AppPropertiesException("error parsing *.json from input stream",e);
		} catch (ParseException e) {
			throw new AppPropertiesException("error parsing *.json from input stream",e);
		}
		JSONObject jsonObj = (JSONObject) obj;
		Set keys = jsonObj.keySet();
		for (Object _key: keys) {
			String key = _key.toString();
			Object value = jsonObj.get(key);

			LOGGER.log(Level.INFO, "Property to be added: key:["+key+"] value:["+value+"]");
			appPropertiesManagerExtensible.addProperty(appProperties, key, value);
		}
	}
}
